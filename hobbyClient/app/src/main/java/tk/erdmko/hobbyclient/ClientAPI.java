package tk.erdmko.hobbyclient;

import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by erdmko on 05/10/15.
 */
public interface ClientAPI {
    @GET("/client.json")
    public ServerData getServerData();

    @Multipart
    @POST("/upload")
    public ServerData uploadFile(@Part("file") TypedFile file, @Part("name") String name);
}
