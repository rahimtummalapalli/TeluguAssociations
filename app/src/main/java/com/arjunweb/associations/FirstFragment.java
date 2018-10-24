package com.arjunweb.associations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FirstFragment extends Fragment
{
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        expListView = view. findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                 Toast.makeText(getActivity(),
                 "Group Clicked " + listDataHeader.get(groupPosition),
                 Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        prepareListData();
        return view;
    }
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void prepareListData()
    {
        try
        {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getActivity()));
            JSONArray m_jArry = obj.getJSONArray("heading");
            for(int i=0;i<m_jArry.length();i++)
            {
                String headername=m_jArry.getJSONObject(i).getString("name");
                listDataHeader.add(headername);
                JSONArray jsonArraychild=obj.getJSONArray("childData");
                List<String> childList=new ArrayList<>(m_jArry.length());
                for(int j=0;j<jsonArraychild.length();j++)
                {
                    String headername1=jsonArraychild.getJSONObject(j).getString("headingName");
                    if(headername.equalsIgnoreCase(headername1))
                    {
                        String childname=jsonArraychild.getJSONObject(j).getString("childname");
                        childList.add(childname);
                    }
                    listDataChild.put(listDataHeader.get(i), childList);
                }
            }
            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setGroupIndicator(null);
            listAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
