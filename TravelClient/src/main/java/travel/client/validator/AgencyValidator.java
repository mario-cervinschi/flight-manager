package travel.client.validator;


import travel.model.Agency;

public class AgencyValidator implements Validator<Agency> {

    @Override
    public void validate(Agency entity) throws ValidatorException {
        String errors = "";
        if(entity.getAgencyName().isEmpty()) {
            errors += "Agency name is required! ";
        }
        if(entity.getEmail().isEmpty()) {
            errors += "Email is required! ";
        }
        if(!entity.getEmail().contains("@") || !entity.getEmail().contains(".")) {
            errors += "Invalid email address! ";
        }
        if(entity.getPassword().isEmpty()) {
            errors += "Password is required! ";
        }
        if(entity.getPassword().length() < 6) {
            errors += "Password must be at least 6 characters! ";
        }
        if(!errors.isEmpty()) {
            throw new ValidatorException(errors);
        }
    }

    public void validate(String name, String email, String password) throws ValidatorException {
        String errors = "";
        if(name.isEmpty()) {
            errors += "Agency name is required! ";
        }
        if(email.isEmpty()) {
            errors += "Email is required! ";
        }
        if(!email.contains("@") || !email.contains(".")) {
            errors += "Invalid email address! ";
        }
        if(password.isEmpty()) {
            errors += "Password is required! ";
        }
        if(password.length() < 6) {
            errors += "Password must be at least 6 characters! ";
        }
        if(!errors.isEmpty()) {
            throw new ValidatorException(errors);
        }
    }
}
