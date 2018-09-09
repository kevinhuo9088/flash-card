package com.flashcard.newport.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flashcard.newport.project.R;

import java.util.ArrayList;

public class OptionAdapter extends BaseAdapter {

    private ArrayList<String> mText;
    private ArrayList<Integer> mImages;
    private static LayoutInflater mInflate;

    public OptionAdapter(Activity optionActivity, ArrayList<String> text, ArrayList<Integer> images ){
        mText = new ArrayList<>(text);
        mImages = new ArrayList<>(images);
        mInflate = (LayoutInflater)optionActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mText.size();
    }

    @Override
    public Object getItem(int position){
        return mText.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rw;

        if(convertView == null) rw = mInflate.inflate(R.layout.option_listview_layout, null);
        else rw = convertView;

        TextView ot = (TextView)rw.findViewById(R.id.option_text);
        ImageView om = (ImageView)rw.findViewById(R.id.option_image);
        ot.setText(mText.get(position));
        om.setImageResource(mImages.get(position));
        return rw;
    }

}