package tk.erdmko.hobbyclient.response;

public class FieldError {
    public String field;
    public String message;

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
