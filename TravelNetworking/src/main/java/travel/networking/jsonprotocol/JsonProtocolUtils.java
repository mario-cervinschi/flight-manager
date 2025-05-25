package travel.networking.jsonprotocol;

import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class JsonProtocolUtils {
    public static Response createNewAgencyResponse(Agency agency) {
        Response response = new Response();
        response.setAgency(agency);
        response.setType(ResponseType.NEW_AGENCY);
        return response;
    }

    public static Response createOkResponse() {
        Response response = new Response();
        response.setType(ResponseType.OK);
        return response;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response response = new Response();
        response.setErrorMessage(errorMessage);
        response.setType(ResponseType.ERROR);
        return response;
    }

    public static Response createGetAllFlightsResponse(Flight[] flights) {
        Response response = new Response();
        response.setFlights(flights);
        response.setType(ResponseType.GET_ALL_FLIGHTS);
        return response;
    }

    public static Response createGetAllFlightsDestinationDateResponse(Flight[] flights) {
        Response response = new Response();
        response.setFlights(flights);
        response.setType(ResponseType.GET_ALL_FLIGHTS_DESTINATION_DATE);
        return response;
    }

    public static Response createUpdateFlightResponse(Flight flight) {
        Response response = new Response();
        response.setFlight(flight);
        response.setType(ResponseType.UPDATE_FLIGHT);
        return response;
    }

    public static Response createLoggedInResponse(Agency agency) {
        Response response = new Response();
        response.setAgency(agency);
        response.setType(ResponseType.OK);
        return response;
    }

    public static Response createSuccesTicketResponse(Ticket ticket) {
        Response response = new Response();
        response.setTicket(ticket);
        response.setType(ResponseType.ADD_TICKET_SUCCESS);
        return response;
    }

    public static Response createNewTicketResponse(Ticket ticket) {
        Response response = new Response();
        response.setTicket(ticket);
        response.setType(ResponseType.NEW_TICKET);
        return response;
    }

    public static Request createLoginRequest(Agency agency) {
        Request request = new Request();
        request.setAgency(agency);
        request.setType(RequestType.LOGIN);
        return request;
    }


    public static Request createSaveAgencyRequest(Agency agency) {
        Request request = new Request();
        request.setAgency(agency);
        request.setType(RequestType.SAVE_AGENCY);
        return request;
    }

    public static Request createGetAllFlightsRequest() {
        Request request = new Request();
        request.setType(RequestType.GET_ALL_FLIGHTS);
        return request;
    }

    public static Request createGetAllFlightsDestinationDateRequest(String destination, LocalDate date) {
        Request request = new Request();
        request.setDestination(destination);
        request.setDate(date);
        request.setType(RequestType.GET_ALL_FLIGHTS_DESTINATION_DATE);
        return request;
    }

    public static Request createUpdateFlightRequest(Flight flight) {
        Request request = new Request();
        request.setFlight(flight);
        request.setType(RequestType.UPDATE_FLIGHT);
        return request;
    }

    public static Request createAddTicketRequest(Ticket ticket) {
        Request request = new Request();
        request.setTicket(ticket);
        request.setType(RequestType.ADD_TICKET);
        return request;
    }

    public static Request createLogOutRequest(Agency agency) {
        Request request = new Request();
        request.setAgency(agency);
        request.setType(RequestType.LOGOUT);
        return request;
    }
}
