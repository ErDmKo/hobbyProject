package tk.erdmko.models;

public class MessageModel {

    private String text;

    public String getText() {
        return new StringBuilder(text).reverse().toString();
    }

}
