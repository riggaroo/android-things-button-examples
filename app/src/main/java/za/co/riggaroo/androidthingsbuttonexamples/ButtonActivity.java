package za.co.riggaroo.androidthingsbuttonexamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class ButtonActivity extends Activity {

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
            buttonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    try {
                        Log.i(TAG, "GPIO value changed:" + gpio.getValue());
                        Toast.makeText(getApplicationContext(), "Button changed, yay!", Toast.LENGTH_SHORT).show();
                        // Return true to continue listening to events
                        return true;
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception reading value out");
                        return false;
                    }
                }
            });
        } catch (IOException ioException) {
            Log.d(TAG, "Failed to register GPIO callback", ioException);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (buttonGpio != null) {
            // Close the Gpio pin
            Log.i(TAG, "Closing Button GPIO pin");
            try {
                buttonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                buttonGpio = null;
            }
        }
    }
}
