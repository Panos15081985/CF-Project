package gr.aueb.cf.dance_school.core.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationException extends Exception{

    private BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
