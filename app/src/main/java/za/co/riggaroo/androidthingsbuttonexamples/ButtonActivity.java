package za.co.riggaroo.androidthingsbuttonexamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class ButtonActivity extends Activity {

    private ButtonInputDriver mInputDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        try {
            mInputDriver = new ButtonInputDriver(BoardDefaults.getGPIOForButton(), Button.LogicState.PRESSED_WHEN_HIGH,
                    KeyEvent.KEYCODE_A // the keycode to send
            );
            mInputDriver.register();
        } catch (IOException e) {
            Log.e(TAG, "couldn't configure the button...", e);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_A) {
            // do something awesome
            Log.d(TAG, "onKeyEvent triggered:" + keyCode);
            return true; // indicate we handled the event
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInputDriver != null) {
            // Close the Gpio pin
            Log.i(TAG, "Closing Button GPIO pin");
            try {
                mInputDriver.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                mInputDriver = null;
            }
        }
    }
}
