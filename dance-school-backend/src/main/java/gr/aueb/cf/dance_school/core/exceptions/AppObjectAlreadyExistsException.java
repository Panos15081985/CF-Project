package gr.aueb.cf.dance_school.core.exceptions;

public class AppObjectAlreadyExistsException extends AppGenericException{

    private static final String DEFAULT_CODE ="Already Exists";

    public AppObjectAlreadyExistsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
