package tk.erdmko.models;

/**
 * Created by erdmko on 22/10/15.
 */
public class SocketResponseModel {

    public SocketResponseModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    private String text;
}
