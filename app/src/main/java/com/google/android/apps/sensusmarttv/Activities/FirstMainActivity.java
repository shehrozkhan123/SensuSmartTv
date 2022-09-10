package com.google.android.apps.sensusmarttv.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.device.DevicePicker;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.command.ServiceCommandError;
import com.google.android.apps.sensusmarttv.R;

import java.util.ArrayList;
import java.util.List;

public class FirstMainActivity extends AppCompatActivity {
    AlertDialog deviceListDialog;
    AlertDialog pairingAlertDialog;
    AlertDialog pairingCodeDialog;
    DevicePicker dp;
    TextView btnSearch;
    //    public Button upButton;
//    public Button leftButton;
//    public Button clickButton;
//    public Button rightButton;
//    public Button backButton;
//    public Button downButton;
//    public Button homeButton;
//    public Button openKeyboardButton;
//    public Button[] buttons;
    DeviceService service;
//    Base base = new Base();

    private DiscoveryManager mDiscoveryManager;
    private ConnectableDevice mDevice;

    List<ConnectableDevice> imageDevices = new ArrayList<>();
    private ConnectableDeviceListener deviceListener = new ConnectableDeviceListener() {
        @Override
        public void onPairingRequired(ConnectableDevice device, DeviceService service, DeviceService.PairingType pairingType) {
            Log.d( "TAG", "Connected to " + mDevice.getFriendlyName() + service + pairingType );

            switch (pairingType) {
                case FIRST_SCREEN:
                    Log.d( "TAG", "First Screen" );
                    pairingAlertDialog.show();
                    break;

                case PIN_CODE:
                    if (pairingAlertDialog.isShowing()) {
                        pairingAlertDialog.show();
                    }
                    if (pairingCodeDialog.isShowing()) {
                        pairingCodeDialog.show();
                    }
                    break;
                case MIXED:
                    Log.d( "TAG", "Pin Code" );
                    pairingCodeDialog.show();
                    break;

                case NONE:
                default:
                    break;
            }
        }

        @Override
        public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
            Log.d( "TAG", "onConnectFailed" + device + error );
            connectFailed( mDevice );
        }

        @Override
        public void onDeviceReady(ConnectableDevice device) {
            Log.d( "TAG", "onDeviceReady" );


            //onPairingRequired( mDevice,service, DeviceService.PairingType.MIXED );

//            DeviceService.PairingType pinCode = DeviceService.PairingType.PIN_CODE;
//            Toast.makeText( FirstMainActivity.this, "" + pinCode.name(), Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onDeviceDisconnected(ConnectableDevice device) {
            Log.d( "TAG", "Device Disconnected" );
            connectEnded( mDevice );
            btnSearch.setText( "Connect" );

        }

        @Override
        public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {

        }
    };

    void connectEnded(ConnectableDevice device) {
        if (pairingAlertDialog.isShowing()) {
            pairingAlertDialog.dismiss();
        }
        if (pairingCodeDialog.isShowing()) {
            pairingCodeDialog.dismiss();
        }

        if (!mDevice.isConnected()) {
            mDevice.removeListener( deviceListener );
            mDevice = null;
        }
    }


    private void connectFailed(ConnectableDevice device) {
        if (device != null)
            Log.d( "2ndScreenAPP", "Failed to connect to " + device.getIpAddress() );

        if (mDevice != null) {
            mDevice.removeListener( deviceListener );
            mDevice.disconnect();
            mDevice = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_first_main );


        btnSearch = findViewById( R.id.btnSearch );
      /*  upButton = findViewById( R.id.upButton );
        leftButton = findViewById( R.id.leftButton );
        clickButton = findViewById( R.id.clickButton );
        rightButton = findViewById( R.id.rightButton );
        backButton = findViewById( R.id.backButton );
        downButton = findViewById( R.id.downButton );
        homeButton = findViewById( R.id.homeButton );
        openKeyboardButton = findViewById( R.id.openKeyboardButton );
        buttons = new Button[8];
        buttons[0] = upButton;
        buttons[1] = leftButton;
        buttons[2] = clickButton;
        buttons[3] = rightButton;
        buttons[4] = backButton;
        buttons[5] = downButton;
        buttons[6] = homeButton;
        buttons[7] = openKeyboardButton;*/


        mDiscoveryManager = DiscoveryManager.getInstance();
        mDiscoveryManager.registerDefaultDeviceTypes();
        mDiscoveryManager.setPairingLevel( DiscoveryManager.PairingLevel.ON );
//        DiscoveryManager.getInstance().registerDeviceService( AirPlayService.class, ZeroconfDiscoveryProvider.class);
//        DiscoveryManager.getInstance().registerDeviceService( CastService.class, CastDiscoveryProvider.class);
//        DiscoveryManager.getInstance().registerDeviceService( DIALService.class, SSDPDiscoveryProvider.class);
//        DiscoveryManager.getInstance().registerDeviceService( RokuService.class, SSDPDiscoveryProvider.class);
//        DiscoveryManager.getInstance().registerDeviceService( DLNAService.class, SSDPDiscoveryProvider.class); // LG TV devices only, includes NetcastTVService
//        DiscoveryManager.getInstance().registerDeviceService( WebOSTVService.class, SSDPDiscoveryProvider.class);

        DiscoveryManager.getInstance().start();

        getImageDevices();
        btnSearch.setOnClickListener( v -> {
            setupPicker();
            hConnectToggle();
            //pairingAlertDialog.show();
        } );

/*
        upButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (base.getKeyControl() != null) {
                    base.getKeyControl().up( null );
                    TestResponseObject testResponse = new TestResponseObject( true, TestResponseObject.SuccessCode, TestResponseObject.UpClicked );
                    Log.d( "TAG", "onClick: " + testResponse );
                }
            }
        } );
*/

    }


    private void getImageDevices() {


        for (ConnectableDevice device : DiscoveryManager.getInstance().getCompatibleDevices().values()) {
            if (device.hasCapability( MediaPlayer.Display_Image ))
                imageDevices.add( device );
        }

        Log.e( "SHAH", "getImageDevices: " + imageDevices );
    }

    public void hConnectToggle() {
        deviceListDialog.show();
        /*if (!this.isFinishing()) {
            if (mDevice != null) {
                if (mDevice.isConnected())
                    mDevice.disconnect();

//                connectItem.setTitle("Connect");
                mDevice.removeListener( deviceListener );
                mDevice = null;

            }
            deviceListDialog.show();

        }*/
    }

    private void setupPicker() {
        Log.d( "TAG", "setupPicker: " );
        dp = new DevicePicker( this );
        deviceListDialog = dp.getPickerDialog( "Device List", new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                mDevice = (ConnectableDevice) arg0.getItemAtPosition( arg2 );
                mDevice.addListener( deviceListener );
                mDevice.setPairingType( DeviceService.PairingType.MIXED );
                mDevice.connect();
                Log.d( "TAG", "onItemClick: " + mDevice.isConnectable() );
                btnSearch.setText( mDevice.getFriendlyName() );
//                connectItem.setTitle(mDevice.getFriendlyName());
                dp.pickDevice( mDevice );
            }
        } );

        pairingAlertDialog = new AlertDialog.Builder( this )
                .setTitle( "Pairing with TV" )
                .setMessage( "Please confirm the connection on your TV" )
                .setPositiveButton( "Okay", null )
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dp.cancelPicker();

                        hConnectToggle();
                    }
                } )
                .create();
        final EditText input = new EditText( this );
        input.setInputType( InputType.TYPE_CLASS_TEXT );

        final InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );

        pairingCodeDialog = new AlertDialog.Builder( this )
                .setTitle( "Enter Pairing Code on TV" )
                .setView( input )
                .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mDevice != null) {
                            String value = input.getText().toString().trim();
                            mDevice.sendPairingKey( value );
                            imm.hideSoftInputFromWindow( input.getWindowToken(), 0 );
                        }
                    }
                } )
                .setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dp.cancelPicker();

                        hConnectToggle();
                        imm.hideSoftInputFromWindow( input.getWindowToken(), 0 );
                    }
                } )
                .create();
    }
/*
    public void UpButton(View view) {
//        if (base.getKeyControl() != null) {
            base.getKeyControl().up( null );
            new TestResponseObject( true, TestResponseObject.SuccessCode, TestResponseObject.UpClicked );
            Log.d( "TAG", String.valueOf( TestResponseObject.SuccessCode ) );
            Toast.makeText( this, "UP", Toast.LENGTH_SHORT ).show();
//        }
    }*/
}