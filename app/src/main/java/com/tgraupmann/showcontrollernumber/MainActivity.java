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

    // assumes descriptor is not null
    private ControllerData getController(String descriptor) {
        for (ControllerData controller : mControllers) {
            if (controller.mDescriptor.equals(descriptor)) {
                return controller;
            }
        }
        return null;
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

    private int getPlayerNum(String descriptor) {
        int[] ids = InputDevice.getDeviceIds();
        ControllerData controller = getController(descriptor);
        if (null == controller) {
            return 0;
        }
        return controller.mPlayerNum;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        final ListView listControllers = (ListView) findViewById(R.id.listControllers);
        InputDevice inputDevice = event.getDevice();
        if (null != inputDevice) {
            String descriptor = inputDevice.getDescriptor();
            if (null != descriptor) {
                ControllerData controller = getController(descriptor);
                if (controller == null) {
                    controller = new ControllerData();
                    controller.mDescriptor = descriptor;
                    controller.mDeviceId = event.getDeviceId();
                    controller.mName = event.getDevice().getName();
                    controller.mPlayerNum = mControllers.size();
                    mControllers.add(controller);
                }
                controller.mKeyCode = event.getKeyCode();
                if (mControllers.size() == 0) {
                    listControllers.setAdapter(null);
                    listControllers.invalidate();
                } else {
                    ControllerAdapter adapter = new ControllerAdapter(MainActivity.this, mControllers);
                    listControllers.setAdapter(adapter);
                }
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
