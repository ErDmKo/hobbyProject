package tk.erdmko.conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import tk.erdmko.models.FieldErrorDTO;
import tk.erdmko.models.ValidationErrorDTO;

/**
 * Created by dmitryeroshenko on 1/21/18.
 */
@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleError404() {
        return "index";
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO proccessValidationError(BadCredentialsException ex) {
        ValidationErrorDTO out = new ValidationErrorDTO();
        out.addFieldError("Auth", ex.getMessage());
        return out;
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO proccessValidationError(org.hibernate.exception.ConstraintViolationException ex) {
        ValidationErrorDTO out = new ValidationErrorDTO();
        out.addFieldError(ex.getConstraintName(), ex.getSQLException().getMessage());
        return out;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO proccessValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ValidationErrorDTO out = new ValidationErrorDTO();
        for (FieldError fieldError : fieldErrors) {
            out.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return out;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO proccessValidationError(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> res = ex.getConstraintViolations();
        return res.stream().map(x -> {
            String mess = x.getMessage();
            String patch = x.getPropertyPath().toString();
            return new FieldErrorDTO(patch, mess);
        }).collect(
                Collector.of(
                        ValidationErrorDTO::new,
                        ValidationErrorDTO::addFieldError,
                        ValidationErrorDTO::concat
                ));
    }

}
