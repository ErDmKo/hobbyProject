package tk.erdmko.hobbyclient;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.mime.TypedFile;

public class MainActivity extends AppCompatActivity {
    private EditText mEditText;
    private TextView mText;
    private ClientAPI clientAPI;
    static final int REQUEST_TAKE_PHOTO = 1;
    private final String DEFAULT_URL = "http://10.0.2.2:8080/";
    private File storageDir;
    protected final String TAG = getClass().getSimpleName();
    private File photoFile;


    ConnectionFactory factory = new ConnectionFactory();
    private void setupConnectionFactory(String host) {
        factory.setHost(host);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setText(DEFAULT_URL);
        mText = (TextView) findViewById(R.id.textView);
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public void getImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "file haven't");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "tk.erdmko.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String resultInfo = "New Image Added";
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // data is empty and this ok because photoFile is not
            Log.i(TAG, String.valueOf(photoFile.length()));
            Toast.makeText(getApplicationContext(), resultInfo, Toast.LENGTH_SHORT).show();
            String output = mEditText.getText().toString();
            // if (Patterns.WEB_URL.matcher(output).matches()) {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(150, TimeUnit.SECONDS); // connect timeout
            client.setReadTimeout(150, TimeUnit.SECONDS);    // socket timeout
            clientAPI = new RestAdapter.Builder()
                    .setEndpoint(output)
                    .setClient(new OkClient(client))
                    .build()
                    .create(ClientAPI.class);
            Thread serverCallThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final ServerData serverData = clientAPI.uploadFile(
                                new TypedFile("image/jpeg", photoFile),
                                "testFile");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mText.setText(String.format("%s exeptions %s", serverData.text, serverData.reason));
                            }
                        });
                    } catch (RetrofitError err) {
                        final String errorTest = err.toString();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), errorTest, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }});
            serverCallThread.start();
        }
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile(
                "testImage",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }
    public void getQueue(View view) throws URISyntaxException {
        URL url = null;
        try {
            url = new URL(mEditText.getText().toString());
            setupConnectionFactory(url.getHost());
            AsyncTask<String, Void, String> serverCallTask = new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Connection connection = null;
                    try {
                        connection = factory.newConnection();
                        String QUEUE_NAME = "hello";
                        Channel channel = connection.createChannel();
                        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Connected",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });

                        String message = "Hello World!";
                        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));

                        Consumer consumer = new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                                    throws IOException {
                                final String message = new String(body, "UTF-8");
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mText.setText(String.format("Message %s", message));
                                    }
                                });
                            }
                        };
                        channel.basicConsume(QUEUE_NAME, true, consumer);
                    } catch (IOException e) {
                        final String message = e.toString();
                        e.printStackTrace();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        getApplicationContext(),
                                        message,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                        final String message = e.toString();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        getApplicationContext(),
                                        message,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                    }
                    return null;
                }
            };
            serverCallTask.execute("1");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



    }
    public void sendRequest(View view) {
        String output = mEditText.getText().toString();
        // if (Patterns.WEB_URL.matcher(output).matches()) {
            clientAPI = new RestAdapter.Builder()
                    .setEndpoint(output)
                    .build()
                    .create(ClientAPI.class);
            Thread serverCallThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerData serverData = clientAPI.getServerData();
                        final String text = serverData.text;
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mText.setText(text);
                            }
                        });
                    } catch (RetrofitError err) {
                        final String errorTest = err.toString();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), errorTest, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
            serverCallThread.start();
        //}

        Log.i(TAG, "Button clicked" + output);

    }
}
