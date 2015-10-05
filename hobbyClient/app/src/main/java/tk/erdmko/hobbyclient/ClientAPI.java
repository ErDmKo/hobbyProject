package tk.erdmko.hobbyclient;

import retrofit.http.GET;

/**
 * Created by erdmko on 05/10/15.
 */
public interface ClientAPI {
    @GET("/client.json")
    public ServerData getServerData();
}
