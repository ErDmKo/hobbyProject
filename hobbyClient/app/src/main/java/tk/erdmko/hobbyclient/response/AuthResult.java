package tk.erdmko.hobbyclient.response;

public class AuthResult {
    public String access_token;
    public Integer expires_in;
    public String jti;
    public String refresh_token;
    public String token_type;
    public FieldError[] fieldErrors;
    public String error;
    public String error_description;
}
