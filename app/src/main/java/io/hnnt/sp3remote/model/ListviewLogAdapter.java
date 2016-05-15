package io.hnnt.sp3remote.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.hnnt.sp3remote.R;

/**
 * Created by nume on 2016-05-15.
 */

public class ListviewLogAdapter extends BaseAdapter{

        private static ArrayList<LogItem> listLog;

        private LayoutInflater mInflater;

        public ListviewLogAdapter(Context logFragment, ArrayList<LogItem> results){
            listLog = results;
            mInflater = LayoutInflater.from(logFragment);
        }

        @Override
        public int getCount() {
            return listLog.size();
        }

        @Override
        public Object getItem(int arg0) {
            return listLog.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }


        public View getView(int pos, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.log_row, null);
                holder = new ViewHolder();
                holder.firstLine= (TextView) convertView.findViewById(R.id.firstLine);
                holder.secondLine = (TextView) convertView.findViewById(R.id.secondLine);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.firstLine.setText(listLog.get(pos).label);
            holder.secondLine.setText(listLog.get(pos).value);
            return convertView;
        }

        static class ViewHolder{
            TextView firstLine, secondLine;
        }

        public void update(ArrayList<LogItem> newlist){
            listLog = newlist;
        }
    }