package gr.aueb.cf.dance_school.core.exceptions;

public class AppObjectNotFoundException extends AppGenericException{

    private static final String Default_Code = "Coach Not Found";

    public AppObjectNotFoundException(String code, String message) {
        super(code + Default_Code, message);
    }
}
