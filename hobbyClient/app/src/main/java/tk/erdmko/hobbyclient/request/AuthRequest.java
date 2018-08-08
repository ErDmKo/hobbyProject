package tk.erdmko.hobbyclient.request;

public class AuthRequest {
    final String username;
    final String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}