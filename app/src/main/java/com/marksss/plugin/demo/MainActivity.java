package com.marksss.plugin.demo;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.marksss.plugin.R;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by shenxl on 2022/2/10.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_serial).setOnClickListener(view -> {
            clickSerial();
        });
        findViewById(R.id.btnDeviceId).setOnClickListener(view -> {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            Log.d("shenxl", "deviceId="+telephonyManager.getDeviceId());
        });
        findViewById(R.id.btnSubscriberId).setOnClickListener(view -> {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            Log.d("shenxl", "subscriberId="+telephonyManager.getSubscriberId());
        });
        findViewById(R.id.btnMeId).setOnClickListener(view -> {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            Log.d("shenxl", "meid="+telephonyManager.getMeid());
        });
        findViewById(R.id.btnHardwareAddress).setOnClickListener(view -> {
            Enumeration<NetworkInterface> networks = null;
            try {
                networks = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while(networks != null && networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = null;
                try {
                    mac = network.getHardwareAddress();
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                if(mac != null) {
                    System.out.print("Current MAC address : ");

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    Log.d("shenxl", "btnHardwareAddress="+sb.toString());
                }
            }
        });
    }

    private void clickSerial() {
        Log.d("shenxl", "serial="+Build.getSerial());
    }
}
