package com.mobile.phj.smartguard.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mobile.phj.smartguard.parser.StreetLightJsonParser;
import com.mobile.phj.smartguard.vo.StreetLight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-12-08.
 */

public class GetDataThread extends Thread {
    Handler myhandler;
    String urlString ="http://192.168.1.10:8080/StreetLightProject?action=showlist";
    URL url = null;
    InputStream is = null;
    HttpURLConnection connection = null;

    public GetDataThread(Handler handler){
        this.myhandler = handler;
    }
    @Override
    public void run() {
        Log.v("phj..............", "start");
        try {
            url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            Log.v("phj..............", "connection OK " + connection.getResponseMessage());
            try {
                is = connection.getInputStream();
            } catch (Exception e) {
                Log.v("phj......", e.toString());
            }
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder responseData = new StringBuilder();
            String data = null;
            while ((data = reader.readLine()) != null) {
                Log.v("phj..............", "test : " + data);
                responseData.append(data);
            }
            doParse(responseData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void doParse(String json) {
        StreetLightJsonParser parser = new StreetLightJsonParser();
        ArrayList<StreetLight> list = parser.parse(json);
        sendMessage(list);
    }
    void sendMessage(ArrayList<StreetLight> list) {
        //sendHandler
        Message msg = myhandler.obtainMessage();
        msg.what = 200;
        msg.obj = list;
        myhandler.sendMessage(msg);

    }

}
