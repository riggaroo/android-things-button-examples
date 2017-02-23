package za.co.riggaroo.androidthingsbuttonexamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.things.contrib.driver.button.Button;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class ButtonActivity extends Activity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        try {
            button = new Button(BoardDefaults.getGPIOForButton(), Button.LogicState.PRESSED_WHEN_LOW);
            button.setOnButtonEventListener(new Button.OnButtonEventListener() {
                @Override
                public void onButtonEvent(final Button button, final boolean pressed) {
                    Log.i(TAG, "Button value changed:" + pressed);
                    if (pressed) {
                        Toast.makeText(getApplicationContext(), "Button pressed, yay!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (IOException ioException) {
            Log.d(TAG, "Failed to register button callback", ioException);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (button != null) {
            try {
                button.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                button = null;
            }
        }
    }
}
