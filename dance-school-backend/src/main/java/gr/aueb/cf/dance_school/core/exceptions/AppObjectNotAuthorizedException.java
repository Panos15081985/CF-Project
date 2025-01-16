package gr.aueb.cf.dance_school.core.exceptions;

import jakarta.xml.bind.annotation.XmlType;

public class AppObjectNotAuthorizedException extends AppGenericException{

    private static final String DEFAULT_CODE = "Not Authorized";
    public AppObjectNotAuthorizedException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
