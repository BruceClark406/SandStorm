package com.example.bogiebudddy;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // bluetooth variables
    final String DEVICE_ADDRESS = "00:14:03:05:F0:FA";
    final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothDevice device = null;
    BluetoothSocket socket;
    OutputStream outputStream;
    boolean bluetoothConnected;

    // initalize buttons
    Button right_btn;
    Button left_btn;
    Button forward_btn;
    Button back_btn;
    ImageButton bluetooth_btn;
    SeekBar speed_slider;

    // initialize text fields
    TextView mainMessage;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set all buttons
        right_btn = (Button) findViewById(R.id.right);
        left_btn = (Button) findViewById(R.id.left);
        forward_btn = (Button) findViewById(R.id.forward);
        back_btn = (Button) findViewById(R.id.back);
        bluetooth_btn = (ImageButton) findViewById(R.id.bluetooth);

        // set all text fields
        mainMessage = (TextView) findViewById(R.id.mainMessage);

        // set all seek bars
        SeekBar speed_bar = (SeekBar) findViewById(R.id.speedBar);


        // set bluetooth action listener
        bluetooth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize Bluetooth
                bluetoothInit();

                //connect to bluetooth
                bluetoothConnected = connectToBluetooth();


            }
        });

        right_btn.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bluetoothConnected){
                    //user holders button down
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        mainMessage.setText("Right\n2");
                        sendViaBluetooth(2);
                    }
                    // user lets go of button
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        mainMessage.setText("Halt\n5");
                        sendViaBluetooth(5);
                    }
                }
                else{
                    mainMessage.setText("Bluetooth not connected you dummy.");
                }
                return false;
            }
        });


        left_btn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bluetoothConnected){
                    //user holders button down
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        mainMessage.setText("Left\n4");
                        sendViaBluetooth(4);
                    }
                    // user lets go of button
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        mainMessage.setText("Halt\n5");
                        sendViaBluetooth(5);
                    }
                }
                else{
                    mainMessage.setText("Bluetooth not connected you dummy.");
                }
                return false;
            }
        });
        forward_btn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bluetoothConnected){
                    //user holders button down
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        mainMessage.setText("Forward\n1");
                        sendViaBluetooth(1);
                    }
                    // user lets go of button
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        mainMessage.setText("Halt\n5");
                        sendViaBluetooth(5);
                    }
                }
                else{
                    mainMessage.setText("Bluetooth not connected you dummy.");
                }
                return false;
            }

        });
        back_btn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bluetoothConnected){

                    //user holders button down
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        mainMessage.setText("Back\n3");
                        sendViaBluetooth(3);
                    }
                    // user lets go of button
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        mainMessage.setText("Halt\n5");
                        sendViaBluetooth(5);
                    }
                }
                else{
                    mainMessage.setText("Bluetooth not connected you dummy.");
                }
                return false;
            }
        });

        speed_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true){
                    String strProgress = Integer.toString(progress);
                    int code = Integer.parseInt("6"+strProgress);
                    sendViaBluetooth(code);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }



    public boolean bluetoothInit(){
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else // if not a bluetooth device, get next device
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }
        //output_view.setText("Found Bluetooth");
        return found;
    }

    @SuppressLint("SetTextI18n")
    public boolean connectToBluetooth(){
        boolean connected = true;

        try
        {
            UUID uuid = device.getUuids()[0].getUuid();
            mainMessage.setText("Connected to StandStorm");
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            try{
                socket.close();

            }
            catch(IOException e2){}
            e.printStackTrace();
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return connected;
    }

    public boolean sendViaBluetooth(int code){
        try {
            outputStream.write(code);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
