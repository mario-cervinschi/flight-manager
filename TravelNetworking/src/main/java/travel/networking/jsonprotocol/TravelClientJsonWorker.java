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
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TravelClientJsonWorker implements Runnable, ITravelObserver {
    private ITravelServices server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    public TravelClientJsonWorker(Socket connection, ITravelServices server) {
        this.connection = connection;
        this.server = server;

        gsonFormatter = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .create();

        try{
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try{
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if(response != null){
                    sendResponse(response);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        try{
            input.close();
            output.close();
            connection.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void boughtTicket(Ticket ticket) {
        Response response = JsonProtocolUtils.createSuccesTicketResponse(ticket);
        System.out.println("Ticket added: " + ticket);
        try{
            sendResponse(response);
        }
        catch (IOException e){
            throw new TravelException("Sending error " + e);
        }
    }

    private static Response okResponse = JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request){
        Response response = null;
        if (request.getType() == RequestType.LOGIN)
        {
            Agency agency = request.getAgency();
            try
            {
                Agency agencyR = server.login(agency, this);
                return JsonProtocolUtils.createLoggedInResponse(agencyR);
            }
            catch (TravelException e)
            {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.LOGOUT)
        {
            System.out.println("Logout request..." + request.getType());
            Agency agency = request.getAgency();
            try
            {
                server.logout(agency, this);
                connected = false;
                return okResponse;
            }
            catch (TravelException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.SAVE_AGENCY)
        {
            System.out.println("Saving agency " + request.getType());
            Agency agency = request.getAgency();
            try
            {
                agency = server.saveAgency(agency);
                return JsonProtocolUtils.createNewAgencyResponse(agency);
            }
            catch (TravelException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.GET_ALL_FLIGHTS)
        {
            System.out.println("Get reservations by trip request..." + request.getType());
            try
            {
                return JsonProtocolUtils.createGetAllFlightsResponse(server.getAllFlights());
            }
            catch (TravelException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.GET_ALL_FLIGHTS_DESTINATION_DATE)
        {
            System.out.println("Get seats by trip request..." + request.getType());
            String destination = request.getDestination();
            LocalDate date = request.getDate();
            try
            {
                return JsonProtocolUtils.createGetAllFlightsDestinationDateResponse(server.getAllFlightsDestinationDate(destination, date));
            }
            catch (TravelException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.UPDATE_FLIGHT)
        {
            Flight flight = request.getFlight();
            try
            {
                return JsonProtocolUtils.createUpdateFlightResponse(server.updateFlight(flight));
            }
            catch (TravelException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.ADD_TICKET)
        {
            Ticket ticket = request.getTicket();
            try
            {
                return JsonProtocolUtils.createNewTicketResponse(server.addTicket(ticket));
            }
            catch (TravelException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        String responseLine = gsonFormatter.toJson(response);
        System.out.println("Sending response: " + responseLine);
        synchronized (output){
            output.println(responseLine);
            output.flush();
        }
    }
}
