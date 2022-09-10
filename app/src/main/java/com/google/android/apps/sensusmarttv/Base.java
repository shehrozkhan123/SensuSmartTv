package com.google.android.apps.sensusmarttv;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.widget.Button;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.ExternalInputControl;
import com.connectsdk.service.capability.KeyControl;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MouseControl;
import com.connectsdk.service.capability.PowerControl;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.TextInputControl;
import com.connectsdk.service.capability.ToastControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.WebAppLauncher;

public class Base {
    private ConnectableDevice mTv;
    private Launcher launcher;
    private MediaPlayer mediaPlayer;
    private MediaControl mediaControl;
    private TVControl tvControl;
    private VolumeControl volumeControl;
    private ToastControl toastControl;
    private MouseControl mouseControl;
    private TextInputControl textInputControl;
    private PowerControl powerControl;
    private ExternalInputControl externalInputControl;
    private KeyControl keyControl;
    private WebAppLauncher webAppLauncher;
    public Button[] buttons;
    Context mContext;

    public Base() {
    }

    public Base(Context mContext) {
        this.mContext = mContext;
    }

    public void setTv(ConnectableDevice tv)  {
        mTv = tv;

        if (tv == null) {
            launcher = null;
            mediaPlayer = null;
            mediaControl = null;
            tvControl = null;
            volumeControl = null;
            toastControl = null;
            textInputControl = null;
            mouseControl = null;
            externalInputControl = null;
            powerControl = null;
            keyControl = null;
            webAppLauncher = null;

            disableButtons();
        }
        else {
            launcher = mTv.getCapability(Launcher.class);
//            mediaPlayer = (MediaPlayer) mTv.getCapability(MediaPlayer.class);
            mediaControl = mTv.getCapability(MediaControl.class);
            tvControl = mTv.getCapability(TVControl.class);
            volumeControl = mTv.getCapability(VolumeControl.class);
            toastControl = mTv.getCapability(ToastControl.class);
            textInputControl = mTv.getCapability(TextInputControl.class);
            mouseControl = mTv.getCapability(MouseControl.class);
            externalInputControl = mTv.getCapability(ExternalInputControl.class);
            powerControl = mTv.getCapability(PowerControl.class);
            keyControl = mTv.getCapability(KeyControl.class);
            webAppLauncher = mTv.getCapability(WebAppLauncher.class);

            enableButtons();
        }
    }

    public void disableButtons() {
        if (buttons != null)
        {
            for (Button button : buttons)
            {
                button.setOnClickListener(null);
                button.setEnabled(false);
            }
        }
    }

    public void enableButtons() {
        if (buttons != null)
        {
            for (Button button : buttons)
                button.setEnabled(true);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public ConnectableDevice getTv()
    {
        return mTv;
    }

    public Launcher getLauncher()
    {
        return launcher;
    }

    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public MediaControl getMediaControl()
    {
        return mediaControl;
    }

    public VolumeControl getVolumeControl()
    {
        return volumeControl;
    }

    public TVControl getTVControl()
    {
        return tvControl;
    }

    public ToastControl getToastControl()
    {
        return toastControl;
    }

    public TextInputControl getTextInputControl()
    {
        return textInputControl;
    }

    public MouseControl getMouseControl()
    {
        return mouseControl;
    }

    public ExternalInputControl getExternalInputControl()
    {
        return externalInputControl;
    }

    public PowerControl getPowerControl()
    {
        return powerControl;
    }

    public KeyControl getKeyControl()
    {
        return keyControl;
    }

    public WebAppLauncher getWebAppLauncher()
    {
        return webAppLauncher;
    }

    public Context getContext()
    {
        return mContext;
    }

    public void disableButton(final Button button) {
        button.setEnabled(false);
    }
}
