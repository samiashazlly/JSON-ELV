package com.samia.jsonexpandablelistview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;


public class MainActivity extends Activity implements
        OnChildClickListener, OnGroupExpandListener,
        OnGroupCollapseListener {
    private ExpandableListAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream inputStream = getResources().openRawResource(R.raw.data);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        StringBuffer buf = new StringBuffer();

        try {
            while ((str = in.readLine()) != null) {
                buf.append(str);
                buf.append('\n');
            }

            in.close();

            JSONObject jsonObject = new JSONObject(buf.toString());

            ExpandableListView elv = (ExpandableListView) findViewById(R.id.elv);

            adapter = new JSONExpandableListAdapter(getLayoutInflater(), jsonObject);
            elv.setAdapter(adapter);

            elv.setOnChildClickListener(this);
            elv.setOnGroupExpandListener(this);
            elv.setOnGroupCollapseListener(this);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Exception reading JSON", e);
        }
    }

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