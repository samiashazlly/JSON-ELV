package com.samia.jsonexpandablelistview;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONExpandableListAdapter extends
        BaseExpandableListAdapter {
    LayoutInflater inflater = null;
    JSONObject jsonObject = null;

    JSONExpandableListAdapter(LayoutInflater inflater, JSONObject jsonObject) {
        this.inflater = inflater;
        this.jsonObject = jsonObject;
    }

    @Override
    public int getGroupCount() {
        return (jsonObject.length());
    }

    @Override
    public Object getGroup(int groupPosition) {
        @SuppressWarnings("rawtypes")
        Iterator i = jsonObject.keys();

        while (groupPosition > 0) {
            i.next();
            groupPosition--;
        }

        return (i.next());
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    inflater.inflate(android.R.layout.simple_expandable_list_item_1,
                            parent, false);
        }

        TextView tv =
                ((TextView) convertView.findViewById(android.R.id.text1));
        tv.setText(getGroup(groupPosition).toString());

        return (convertView);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            JSONArray children = getChildren(groupPosition);

            return (children.length());
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Exception getting children", e);
        }

        return (0);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        try {
            JSONArray children = getChildren(groupPosition);

            return (children.get(childPosition));
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(),
                    "Exception getting item from JSON array", e);
        }

        return (null);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition * 1024 + childPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    inflater.inflate(android.R.layout.simple_list_item_1, parent,
                            false);
        }

        TextView tv = (TextView) convertView;
        tv.setText(getChild(groupPosition, childPosition).toString());

        return (convertView);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return (true);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    private JSONArray getChildren(int groupPosition) throws JSONException {
        String key = getGroup(groupPosition).toString();

        return (jsonObject.getJSONArray(key));
    }
}