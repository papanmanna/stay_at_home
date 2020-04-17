package com.example.stayathome.ui.request;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.stayathome.databinding.ItemTimeBinding;
import com.example.stayathome.models.Station;

import java.util.List;

public class StationListAdapter extends BaseAdapter {

    private List<Station> stationList;
    private Context context;
    private int resource;
    private LayoutInflater inflater;

    public StationListAdapter(@NonNull Context context, int resource, List<Station> stations) {
        this.stationList = stations;
        this.context = context;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int position) {
        return stationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ItemTimeBinding timeBinding = DataBindingUtil.inflate(inflater, resource, parent, false);
        timeBinding.timeTv.setText(stationList.get(position).getStationName());

        return timeBinding.getRoot();
    }
}
