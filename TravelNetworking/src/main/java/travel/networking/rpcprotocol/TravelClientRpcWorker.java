package travel.networking.rpcprotocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;
import travel.networking.dto.SearchFlightDTO;
import travel.services.ITravelObserver;
import travel.services.ITravelServices;
import travel.services.TravelException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;

public class TravelClientRpcWorker implements Runnable, ITravelObserver {
    private ITravelServices server;
    private Socket connection;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(TravelClientRpcWorker.class);

    public TravelClientRpcWorker(ITravelServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch(IOException e){
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException|ClassNotFoundException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }

    @Override
    public void boughtTicket(Ticket ticket) {
        Response resp = new Response.Builder().type(ResponseType.ADD_TICKET_SUCCESS).data(ticket).build();
        logger.debug("Ticket bought: " + ticket);
        try{
            sendResponse(resp);
        }catch(IOException e){
            throw new TravelException("Error "+e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response=null;
        String handlerName = "handle"+(request).type();
        logger.debug("HandlerName" + handlerName);
        try{
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            logger.debug("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException| InvocationTargetException|IllegalAccessException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }

    private Response handleLOGIN(Request request){
        logger.debug("Login request ..."+request.type());
        Agency agency = (Agency)request.data();
        try {
            Agency agencyR = server.login(agency, this);
            return new Response.Builder().type(ResponseType.OK).data(agencyR).build();
        } catch (TravelException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        logger.debug("Logout request...");
        Agency agency = (Agency)request.data();
        try {
            server.logout(agency, this);
            connected=false;
            return okResponse;

        } catch (TravelException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSAVE_AGENCY(Request request){
        logger.debug("Save agency request...");
        Agency agency = (Agency)request.data();
        try{
            agency = server.saveAgency(agency);
            return new Response.Builder().type(ResponseType.NEW_AGENCY).data(agency).build();
        }catch(TravelException e){
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_FLIGHTS(Request request){
        logger.debug("GetAllFlights request...");
        try{
            return new Response.Builder().type(ResponseType.GET_ALL_FLIGHTS).data(server.getAllFlights()).build();
        }catch(TravelException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_FLIGHTS_DESTINATION_DATE(Request request){
        logger.debug("GetAllFlightsDestinationDate request...");
        SearchFlightDTO sfdto = (SearchFlightDTO) request.data();
        String destination = sfdto.getDestination();
        LocalDate date = sfdto.getDepartureDate();
        try{
            return new Response.Builder().type(ResponseType.GET_ALL_FLIGHTS_DESTINATION_DATE).data(server.getAllFlightsDestinationDate(destination, date)).build();
        }catch(TravelException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleUPDATE_FLIGHT(Request request){
        logger.debug("UpdateFlight request...");
        Flight flight = (Flight)request.data();
        try{
            return new Response.Builder().type(ResponseType.UPDATE_FLIGHT).data(server.updateFlight(flight)).build();
        }catch(TravelException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_TICKET(Request request){
        logger.debug("AddTicket request...");
        Ticket ticket = (Ticket)request.data();
        try{
            return new Response.Builder().type(ResponseType.NEW_TICKET).data(server.addTicket(ticket)).build();
        }catch(TravelException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private void sendResponse(Response response) throws IOException {
        logger.debug("sending response {} " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
