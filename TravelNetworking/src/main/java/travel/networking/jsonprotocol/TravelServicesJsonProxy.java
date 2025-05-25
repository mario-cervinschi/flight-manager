package travel.networking.jsonprotocol;

import com.google.gson.*;
import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;
import travel.services.ITravelObserver;
import travel.services.ITravelServices;
import travel.services.TravelException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TravelServicesJsonProxy implements ITravelServices {
    private String host;
    private int port;

    private ITravelObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TravelServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;

        gsonFormatter = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .create();
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public Agency login(Agency agency, ITravelObserver client) throws TravelException {
        initializeConnection();

        Request req = JsonProtocolUtils.createLoginRequest(agency);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            this.client = client;
        }

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            closeConnection();
            throw new TravelException(err);
        }

        return response.getAgency();
    }

    @Override
    public Agency saveAgency(Agency agency) throws TravelException {
        initializeConnection();

        Request req = JsonProtocolUtils.createSaveAgencyRequest(agency);
        sendRequest(req);
        Response response = readResponse();

        closeConnection();

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new TravelException(err);
        }

        return response.getAgency();
    }

    @Override
    public Flight[] getAllFlights() throws TravelException {
        Request req = JsonProtocolUtils.createGetAllFlightsRequest();
        sendRequest(req);
        Response response = readResponse();

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new TravelException(err);
        }

        Flight[] flights = response.getFlights();
        return flights;
    }

    @Override
    public Flight[] getAllFlightsDestinationDate(String destination, LocalDate date) throws TravelException {
        Request req = JsonProtocolUtils.createGetAllFlightsDestinationDateRequest(destination, date);
        sendRequest(req);
        Response response = readResponse();

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new TravelException(err);
        }

        Flight[] flights = response.getFlights();
        return flights;
    }

    @Override
    public Flight updateFlight(Flight flight) throws TravelException {
        Request req = JsonProtocolUtils.createUpdateFlightRequest(flight);
        sendRequest(req);
        Response response = readResponse();

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new TravelException(err);
        }

        return response.getFlight();
    }

    @Override
    public Ticket addTicket(Ticket ticket) throws TravelException {
        Request req = JsonProtocolUtils.createAddTicketRequest(ticket);
        sendRequest(req);
        Response response = readResponse();

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new TravelException(err);
        }

        return response.getTicket();
    }

    @Override
    public void logout(Agency agency, ITravelObserver client) throws TravelException {
        Request req = JsonProtocolUtils.createLogOutRequest(agency);
        sendRequest(req);
        Response response = readResponse();
        closeConnection();

        if(response.getType() == ResponseType.ERROR){
            String err = response.getErrorMessage();
            throw new TravelException(err);
        }
    }

    public void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    private void sendRequest(Request request) throws TravelException {
        String requestJson = gsonFormatter.toJson(request);
        System.out.println("Sending request " + requestJson);
        try {
            output.println(requestJson);
            output.flush();
        } catch (Exception e) {
            throw new TravelException("Error sending object " + e);
        }
    }

    private Response readResponse() throws TravelException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            throw new TravelException("Error reading response " + e);
        }
        if (response == null) {
            throw new TravelException("Received null response from server.");
        }
        return response;
    }

    private void initializeConnection() throws TravelException {
        try {
            gsonFormatter = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .create();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new TravelException("Error initializing connection " + e);
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) {
        if (response.getType() == ResponseType.ADD_TICKET_SUCCESS) {
            Ticket ticket = response.getTicket();
            try {
                client.boughtTicket(ticket);
            } catch (TravelException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.ADD_TICKET_SUCCESS;
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    System.out.println("Received response " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (response == null) {
                        System.out.println("Invalid response from the server (Proxy)!");
                        continue;
                    }
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

}
