package tk.erdmko.hobbyclient;

import android.app.Activity;
import android.widget.TextView;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class ConnectedThread extends Thread {

    FileInputStream mInputStream;
    Activity activity;
    TextView mTextView;
    byte[] buffer = new byte[1024];
    boolean running;

    ConnectedThread(Activity activity) {
        this.activity = activity;
        mTextView = (TextView) activity.findViewById(R.id.textView);
        running = true;
    }

    public void run() {
        while (running) {
            try {
                int bytes = mInputStream.read(buffer);
                if (bytes > 3) { // The message is 4 bytes long
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long timer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getLong();
                            mTextView.setText(Long.toString(timer));
                        }
                    });
                }
            } catch (Exception ignore) {
            }
        }
    }

    public void cancel() {
        running = false;
    }
}