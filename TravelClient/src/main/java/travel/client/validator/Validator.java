package travel.client.validator;


public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
