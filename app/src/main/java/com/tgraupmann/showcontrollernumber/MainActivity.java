package com.tgraupmann.showcontrollernumber;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timot on 2/6/2017.
 */

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final boolean sEnableLogging = false;

    private List<ControllerData> mControllers = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private ControllerData getController(int deviceId) {
        for (ControllerData controller : mControllers) {
            if (controller.mDeviceId == deviceId) {
                return controller;
            }
        }

        ControllerData controller = new ControllerData();
        controller.mDeviceId = deviceId;
        mControllers.add(controller);
        return controller;
    }

    private static boolean isGamepad(InputDevice inputDevice) {
        int hasFlags = InputDevice.SOURCE_GAMEPAD | InputDevice.SOURCE_JOYSTICK;
        return (inputDevice.getSources() & hasFlags) == hasFlags;
    }

    private static boolean isRemote(InputDevice inputDevice) {
        int hasFlags = InputDevice.SOURCE_DPAD;
        return ((inputDevice.getSources() & hasFlags) == hasFlags) &&
                inputDevice.getKeyboardType() == InputDevice.KEYBOARD_TYPE_NON_ALPHABETIC;
    }

    private static int getPlayerNum(int deviceId) {
        int[] ids = InputDevice.getDeviceIds();

        int playerNum=0;
        for (int i = 0; i < ids.length; ++i) {
            InputDevice inputDevice = InputDevice.getDevice(ids[i]);
            if (!isGamepad(inputDevice) &&
                    !isRemote(inputDevice)) {
                continue;
            }
            if (playerNum >= 4) {
                if (sEnableLogging) {
                    Log.d(TAG, "getPlayerNum: >=4 playerNum="+playerNum);
                }
                return 0;
            }
            if (deviceId == ids[i]) {
                if (sEnableLogging) {
                    Log.d(TAG, "getPlayerNum playerNum="+playerNum);
                }
                return playerNum;
            }
            ++playerNum;
        }

        if (sEnableLogging) {
            Log.d(TAG, "getPlayerNum: deviceId not found playerNum=0");
        }
        return 0;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        final ListView listControllers = (ListView) findViewById(R.id.listControllers);
        ControllerData controller = getController(event.getDeviceId());
        controller.mPlayerNum = getPlayerNum(event.getDeviceId());
        controller.mKeyCode = event.getKeyCode();
        if (mControllers.size() == 0) {
            listControllers.setAdapter(null);
            listControllers.invalidate();
        } else {
            ControllerAdapter adapter = new ControllerAdapter(MainActivity.this, mControllers);
            listControllers.setAdapter(adapter);
        }

        return super.dispatchKeyEvent(event);
    }
}
