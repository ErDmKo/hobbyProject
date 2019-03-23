package tk.erdmko.hobbyclient;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;
import tk.erdmko.hobbyclient.response.HealthCheck;

public class MainActivity extends AppCompatActivity {
    private TextView mText;
    private ClientAPI clientAPI;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String serverURL;
    private String token;
    private File storageDir;
    protected final String TAG = getClass().getSimpleName();
    private File photoFile;


    private static final String ACTION_USB_PERMISSION = "tk.erdmko.hobbyclient.USB_PERMISSION";
    UsbAccessory mAccessory;
    ParcelFileDescriptor mFileDescriptor;
    FileInputStream mInputStream;
    FileOutputStream mOutputStream;
    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;
    private boolean mPermissionRequestPending;
    TextView connectionStatus;
    ConnectedThread mConnectedThread;

    private void setConnectionStatus(boolean connected) {
        Toast.makeText(
                this.getApplicationContext(),
                connected ? "USB Connected" : "USB Disconected",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void openAccessory(UsbAccessory accessory) {
        mFileDescriptor = mUsbManager.openAccessory(accessory);
        if (mFileDescriptor != null) {
            mAccessory = accessory;
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);

            mConnectedThread = new ConnectedThread(this);
            mConnectedThread.start();

            setConnectionStatus(true);

            Log.d(TAG, "Accessory opened");
        } else {
            setConnectionStatus(false);
            Log.d(TAG, "Accessory open failed");
        }
    }

    private void closeAccessory() {
        setConnectionStatus(false);

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Close all streams
        try {
            if (mInputStream != null)
                mInputStream.close();
        } catch (Exception ignored) {
        } finally {
            mInputStream = null;
        }
        try {
            if (mOutputStream != null)
                mOutputStream.close();
        } catch (Exception ignored) {
        } finally {
            mOutputStream = null;
        }
        try {
            if (mFileDescriptor != null)
                mFileDescriptor.close();
        } catch (IOException ignored) {
        } finally {
            mFileDescriptor = null;
            mAccessory = null;
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        openAccessory(accessory);
                    else {
                        Log.d(TAG, "Permission denied for accessory " + accessory);
                    }
                    mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null && accessory.equals(mAccessory))
                    closeAccessory();
            }
        }
    };

    private  String genAuthHeader() {
        return  "Bearer " + this.token;
    }

    ConnectionFactory factory = new ConnectionFactory();
    private void setupConnectionFactory(String host) {
        factory.setHost(host);
    }
    private static class QuequeTask extends AsyncTask<String, Void, String> {
        private WeakReference<MainActivity> activityReference;
        private WeakReference<ConnectionFactory> factoryRef;
        private WeakReference<TextView> mTextRef;

        QuequeTask(MainActivity activityReference, ConnectionFactory factoryRef, TextView mTextRef) {
            this.activityReference = new WeakReference<>(activityReference);
            this.factoryRef = new WeakReference<>(factoryRef);
            this.mTextRef = new WeakReference<>(mTextRef);
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectionFactory factory = factoryRef.get();
            final MainActivity mainActivity = activityReference.get();
            final TextView mText = mTextRef.get();

            try {
                Connection connection = factory.newConnection();
                String QUEUE_NAME = "hello";
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                mainActivity.getApplicationContext(),
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
                        mainActivity.runOnUiThread(new Runnable() {
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
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                mainActivity.getApplicationContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            } catch (TimeoutException e) {
                e.printStackTrace();
                final String message = e.toString();
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                mainActivity.getApplicationContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.textView);
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Intent intent = getIntent();
        token = intent.getStringExtra(LoginActivity.TOKEN_MESSAGE);
        serverURL = intent.getStringExtra(LoginActivity.SERVER_MESSAGE);
        if (token == null) {
            mText.setError(getText(R.string.error_empty_token));
        }
        if (serverURL == null) {
            mText.setError(getText(R.string.error_empty_server));
        }

        // USB connect
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        registerReceiver(mUsbReceiver, filter);
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
            // if (Patterns.WEB_URL.matcher(output).matches()) {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(150, TimeUnit.SECONDS); // connect timeout
            client.setReadTimeout(150, TimeUnit.SECONDS);    // socket timeout
            clientAPI = new RestAdapter.Builder()
                    .setEndpoint(serverURL)
                    .setClient(new OkClient(client))
                    .build()
                    .create(ClientAPI.class);

            Thread serverCallThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final HealthCheck healthCheck = clientAPI.uploadFile(genAuthHeader(),
                                new TypedFile("image/jpeg", photoFile),
                                "testFile");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mText.setText(String.format("%s exeptions %s", healthCheck.text, healthCheck.reason));
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

        return File.createTempFile(
                "testImage",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
    public void getQueue(View view) {
        try {
            URL url = new URL(serverURL);
            setupConnectionFactory(url.getHost());
            QuequeTask serverCallTask = new QuequeTask(this, factory, mText);
            serverCallTask.execute("1");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



    }
    public void sendRequest(View view) {
        clientAPI = new RestAdapter.Builder()
                .setEndpoint(serverURL)
                .build()
                .create(ClientAPI.class);
        Thread serverCallThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HealthCheck healthCheck = clientAPI.getServerData(genAuthHeader());
                    final String text = healthCheck.text;
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

        Log.i(TAG, "Button clicked " + serverURL);

    }
    @Override
    public void onResume() {
        super.onResume();

        if (mAccessory != null) {
            setConnectionStatus(true);
            return;
        }

        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            if (mUsbManager.hasPermission(accessory))
                openAccessory(accessory);
            else {
                setConnectionStatus(false);
                synchronized (mUsbReceiver) {
                    if (!mPermissionRequestPending) {
                        mUsbManager.requestPermission(accessory, mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        } else {
            setConnectionStatus(false);
            Log.d(TAG, "mAccessory is null");
        }
    }
}
