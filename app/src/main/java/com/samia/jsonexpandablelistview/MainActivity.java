package com.samia.jsonexpandablelistview;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


public class MainActivity extends Activity implements
        OnChildClickListener, OnGroupExpandListener,
        OnGroupCollapseListener {

    private String urlString = "https://api.myjson.com/bins/3car6";
    private ExpandableListAdapter adapter = null;
    String response = null ;
    private JSONObject jsonObject;
    private ExpandableListView elv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "Network Connected", Toast.LENGTH_LONG).show();
        }

        elv = (ExpandableListView) findViewById(R.id.elv);
        fetchJSON();

    }

    public boolean isNetworkAvailable() {
        boolean connected ;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

    public void fetchJSON(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    String crappyPrefix = "null";

                    if(response.startsWith(crappyPrefix)){
                        response = response.substring(crappyPrefix.length(), response.length());
                    }
                    jsonObject = new JSONObject(response);
                    handler.sendEmptyMessage(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        );
        thread.start();
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter = new JSONExpandableListAdapter(getLayoutInflater(), jsonObject);
            elv.setAdapter(adapter);
            elv.setOnChildClickListener(MainActivity.this);
            elv.setOnGroupExpandListener(MainActivity.this);
            elv.setOnGroupCollapseListener(MainActivity.this);
        }
    };

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition,
                                long id) {
        Toast.makeText(this,
                adapter.getChild(groupPosition, childPosition)
                        .toString(), Toast.LENGTH_SHORT).show();

        return (false);
    }


    @Override
    public void onGroupExpand(int groupPosition) {
        Toast.makeText(this,
                "Expanding: "
                        + adapter.getGroup(groupPosition).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        Toast.makeText(this,
                "Collapsing: "
                        + adapter.getGroup(groupPosition).toString(),
                Toast.LENGTH_SHORT).show();
    }
}