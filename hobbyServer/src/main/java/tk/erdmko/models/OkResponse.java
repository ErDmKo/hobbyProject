package tk.erdmko.models;

/**
 * Created by erdmko on 05/10/15.
 */
public class OkResponse implements SimpleJsonResponse {
    public String text = "Ok";

    public OkResponse(String text) {
        this.text = text;
    }
}
