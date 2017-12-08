package com.mobile.phj.smartguard;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobile.phj.smartguard.thread.GetDataThread;
import com.mobile.phj.smartguard.vo.StreetLight;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<StreetLight> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doAction();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sookmyung = new LatLng(37.5463899, 126.9647134);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sookmyung,17));
        doAction();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    list = (ArrayList<StreetLight>) msg.obj;
                    Log.v("MainActivity","list : "+ list.size());
                    addMarker(list);
                    break;
            }
        }
    };
    void addMarker(ArrayList<StreetLight> list){
        TextView tx = (TextView)findViewById(R.id.textView1);
        LatLng position;
        StreetLight streetLight ;
        String info;
        for(int i = 0 ;i<list.size() ; i++){
            streetLight = list.get(i);
            position= new LatLng(streetLight.lat,streetLight.lon);
            info = streetLight.info;
            if(streetLight.alram==1){
                mMap.addMarker(new MarkerOptions().position(position).title(info).icon(BitmapDescriptorFactory.fromResource(R.drawable.alarm_on)));
                tx.setText(streetLight.id+"로 출동");
            }else{
                mMap.addMarker(new MarkerOptions().position(position).title(info).icon(BitmapDescriptorFactory.fromResource(R.drawable.alarm_off)));
            }
        }
    }

    void doAction() {
        new GetDataThread(handler).start();
    }
}
