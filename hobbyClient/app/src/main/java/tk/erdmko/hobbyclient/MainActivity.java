package tk.erdmko.hobbyclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
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
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String resultInfo = "New Image Added";
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.i(TAG, String.valueOf(data));
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

                    final ServerData serverData = clientAPI.uploadFile(
                            new TypedFile("image/jpeg", photoFile),
                            "testFile");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mText.setText(serverData.text+serverData.reason);
                        }
                    });
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
                    final ServerData serverData = clientAPI.getServerData();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mText.setText(serverData.text);
                        }
                    });
                }
            });
            serverCallThread.start();
        //}

        Log.i(TAG, "Button clicked" + output);

    }
}
