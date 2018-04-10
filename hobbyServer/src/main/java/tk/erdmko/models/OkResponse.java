package tk.erdmko.models;


public class OkResponse implements SimpleJsonResponse {
    public String text = "Ok";

    public void setText(String text) {
        this.text = text;
    }

    public void setCsrf(String csrf) {
        this.csrf = csrf;
    }

    public String csrf = "";

    public OkResponse(String text) {
        this.text = text;
    }

    public OkResponse(String text, String csrf) {
        this.text = text;
        this.csrf = csrf;
    }
}
