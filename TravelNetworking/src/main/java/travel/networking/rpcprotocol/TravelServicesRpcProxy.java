package travel.networking.rpcprotocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;
import travel.networking.dto.DTOUtils;
import travel.networking.dto.SearchFlightDTO;
import travel.services.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.random.RandomGenerator;

public class TravelServicesRpcProxy implements ITravelServices {
    private String host;
    private int port;

    private static Logger logger = LogManager.getLogger(TravelServicesRpcProxy.class);

    private ITravelObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TravelServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
        logger.info("ServicesRpcProxy started");
    }

    @Override
    public Agency login(Agency agency, ITravelObserver client) throws TravelException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(agency).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            this.client = client;
            Agency agencyR = (Agency) response.data();
            return agencyR;
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            closeConnection();
            throw new TravelException(err);
        }
        return null;
    }

    @Override
    public Agency saveAgency(Agency agency) throws TravelException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.SAVE_AGENCY).data(agency).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TravelException(err);
        }
        return (Agency) response.data();
    }

    @Override
    public Flight[] getAllFlights() throws TravelException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_FLIGHTS).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TravelException(err);
        }
        Flight[] flights = (Flight[]) response.data();
        return flights;
    }

    @Override
    public Flight[] getAllFlightsDestinationDate(String destination, LocalDate date) throws TravelException {
        SearchFlightDTO sfdto = DTOUtils.getDTO(new Flight(destination, date));
        Request req = new Request.Builder().type(RequestType.GET_ALL_FLIGHTS_DESTINATION_DATE).data(sfdto).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TravelException(err);
        }
        Flight[] flights = (Flight[]) response.data();
        return flights;
    }

    @Override
    public Flight updateFlight(Flight flight) throws TravelException {
        Request req = new Request.Builder().type(RequestType.UPDATE_FLIGHT).data(flight).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TravelException(err);
        }
        return (Flight) response.data();
    }

    @Override
    public Ticket addTicket(Ticket ticket) throws TravelException {
        Request req = new Request.Builder().type(RequestType.ADD_TICKET).data(ticket).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new TravelException(err);
        }
        return (Ticket) response.data();
    }

    @Override
    public void logout(Agency agency, ITravelObserver client) throws TravelException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(agency).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TravelException(err);
        }
    }

    private void closeConnection(){
        logger.debug("closing connection");
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client=null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws TravelException {
        logger.debug("sending request {}", request);
        try{
            output.writeObject(request);
            output.flush();
        }catch (Exception e){
            throw new TravelException("Error sending request " + e);
        }
    }

    private Response readResponse() throws TravelException {
        Response response = null;
        try{
            response = qresponses.take();
        }catch(InterruptedException e){
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }

    private void initializeConnection() throws TravelException {
        try{
            System.out.println("Connectiong to server on port: " + port + " and host " + host);
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        }catch (IOException e){
            logger.error("error initializing connection " + e);
            logger.error(e.getStackTrace());
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if(response.type() == ResponseType.ADD_TICKET_SUCCESS){
            Ticket ticket = (Ticket) response.data();
            logger.debug("New ticket " + ticket);
            try{
                client.boughtTicket(ticket);
            }catch (TravelException e){
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.ADD_TICKET_SUCCESS;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    logger.debug("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }

}
