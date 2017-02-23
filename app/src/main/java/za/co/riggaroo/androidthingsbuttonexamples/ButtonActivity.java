package za.co.riggaroo.androidthingsbuttonexamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.userdriver.InputDriver;
import com.google.android.things.userdriver.UserDriverManager;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class ButtonActivity extends Activity {

    private InputDriver inputDriver;
    private static final String DRIVER_NAME = "Button";
    private static final int DRIVER_VERSION = 1;
    private Gpio buttonGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        PeripheralManagerService service = new PeripheralManagerService();

        try {
            buttonGpio = service.openGpio(BoardDefaults.getGPIOForButton());
            // Configure the peripheral
            buttonGpio.setDirection(Gpio.DIRECTION_IN);
            buttonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
            // Attach callback for input events
            buttonGpio.registerGpioCallback(callback);
            inputDriver = InputDriver.builder(InputDevice.SOURCE_CLASS_BUTTON).setName(DRIVER_NAME)
                    .setVersion(DRIVER_VERSION).setKeys(new int[]{KeyEvent.KEYCODE_SPACE}).build();
            UserDriverManager.getManager().registerInputDriver(inputDriver);
        } catch (IOException ioException) {
            Log.d(TAG, "Failed to register GPIO callback", ioException);
        }


    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            Log.d(TAG, "onKeyDown:" + keyCode + ". event:" + event.toString());
            Toast.makeText(getApplicationContext(), "Button pressed, yay!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private GpioCallback callback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            int keyAction = 0;
            try {
                keyAction = gpio.getValue() ? KeyEvent.ACTION_DOWN : KeyEvent.ACTION_UP;
                inputDriver.emit(new KeyEvent[]{new KeyEvent(keyAction, KeyEvent.KEYCODE_SPACE)});
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Exception in callback:", e);
                return false;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inputDriver != null) {
            // Close the Gpio pin
            Log.i(TAG, "Closing Button GPIO pin");

            UserDriverManager.getManager().unregisterInputDriver(inputDriver);
            inputDriver = null;

        }
        if (buttonGpio != null) {
            try {
                buttonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "button close");
            }
        }
    }
}
