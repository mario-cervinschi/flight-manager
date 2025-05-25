package travel.client.validator;


import travel.model.Ticket;

public class TicketValidator implements Validator<Ticket> {

    @Override
    public void validate(Ticket entity) throws ValidatorException {
        String errors = "";
        if(entity.getNoOfSeats() <= 0){
            errors += "No of seats must be greater than 0. ";
        }
        if(entity.getTouristNames().isEmpty()){
            errors += "Tourist names cannot be empty. ";
        }
        if(entity.getTouristNames().size() != entity.getNoOfSeats()){
            errors += "Tourist names must be the same number of seats. ";
        }
        if(entity.getAgency() == null){
            errors += "Agency cannot be null. ";
        }
        if(entity.getFlight() == null){
            errors += "Flight cannot be null. ";
        }
        if(!errors.isEmpty()) {
            throw new ValidatorException(errors);
        }
    }
}
