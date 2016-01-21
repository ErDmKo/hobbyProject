package tk.erdmko.models;

/**
 * Created by erdmko on 18/01/16.
 */
public class FailResponse implements SimpleJsonResponse {
    public String text = "Fail";
    public String reason = null;

    public FailResponse(String s) {
        this.reason = s;
    }
    public String toString(){
        return this.text + " " + this.reason;
    }
}
