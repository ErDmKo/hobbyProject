package tk.erdmko.models;


public class OkResponse implements SimpleJsonResponse {
    public String text = "Ok";

    public OkResponse(String text) {
        this.text = text;
    }
}
