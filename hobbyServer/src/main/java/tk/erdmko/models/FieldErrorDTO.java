package tk.erdmko.models;

/**
 * Created by dmitryeroshenko on 1/21/18.
 */

public class FieldErrorDTO {
    public String field;
    public String message;

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
