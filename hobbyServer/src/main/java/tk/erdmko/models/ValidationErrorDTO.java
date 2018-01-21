package tk.erdmko.models;

import java.util.ArrayList;

/**
 * Created by dmitryeroshenko on 1/21/18.
 */

public class ValidationErrorDTO {

    public ArrayList<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    public ValidationErrorDTO(ArrayList<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    private ArrayList <FieldErrorDTO> fieldErrors = new ArrayList<FieldErrorDTO>();

    public ValidationErrorDTO() {
    }

    public void addFieldError(String path, String message) {
        fieldErrors.add(new FieldErrorDTO(path, message));
    }
    public void addFieldError(FieldErrorDTO errorDTO) {
        fieldErrors.add(errorDTO);
    }
    public ValidationErrorDTO concat (ValidationErrorDTO errors) {
        ArrayList<FieldErrorDTO> out = new ArrayList<FieldErrorDTO>();
        out.addAll(this.fieldErrors);
        out.addAll(errors.getFieldErrors());
        return new ValidationErrorDTO(out);
    }
}
