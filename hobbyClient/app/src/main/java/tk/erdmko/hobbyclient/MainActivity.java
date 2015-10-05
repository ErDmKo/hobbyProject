package tk.erdmko.hobbyclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import retrofit.RestAdapter;

public class MainActivity extends AppCompatActivity {
    private EditText mEditText;
    private TextView mText;
    private ClientAPI clientAPI;
    private final String DEFAULT_URL = "http://10.37.0.160:8080/";
    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setText(DEFAULT_URL);
        mText = (TextView) findViewById(R.id.textView);
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
