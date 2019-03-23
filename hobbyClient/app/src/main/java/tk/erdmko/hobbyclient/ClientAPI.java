package tk.erdmko.hobbyclient;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import tk.erdmko.hobbyclient.request.AuthRequest;
import tk.erdmko.hobbyclient.response.HealthCheck;
import tk.erdmko.hobbyclient.response.AuthResult;

/**
 * Created by erdmko on 05/10/15.
 */
public interface ClientAPI {
    @GET("/client.json")
    public HealthCheck getServerData(@Header("Authorization") String auth);

    @Multipart
    @POST("/upload")
    public HealthCheck uploadFile(@Header("Authorization") String auth, @Part("file") TypedFile file, @Part("name") String name);

    @POST("/users/authenticate")
    public AuthResult auth(@Body AuthRequest request);
}
