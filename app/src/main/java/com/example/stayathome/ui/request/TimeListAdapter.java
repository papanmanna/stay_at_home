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
import com.example.stayathome.models.TimeModel;

import java.util.List;

public class TimeListAdapter extends BaseAdapter {


    private List<TimeModel> timeModels;
    private Context context;
    private int resource;
    private LayoutInflater inflater;

    public TimeListAdapter(@NonNull Context context, int resource, List<TimeModel> timeModels) {
        this.timeModels = timeModels;
        this.context = context;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return timeModels.size();
    }

    @Override
    public Object getItem(int position) {
        return timeModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ItemTimeBinding timeBinding = DataBindingUtil.inflate(inflater, resource, parent, false);
        timeBinding.timeTv.setText(timeModels.get(position).value);

        return timeBinding.getRoot();
    }
}
