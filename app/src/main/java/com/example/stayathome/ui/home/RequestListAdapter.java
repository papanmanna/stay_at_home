package com.example.stayathome.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stayathome.R;
import com.example.stayathome.databinding.ItemRequestBinding;
import com.example.stayathome.models.GetRequestResponse;
import com.example.stayathome.models.TimeModel;

import java.util.ArrayList;
import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.MyViewHolder> {

    private List<GetRequestResponse.RequestData> requestDataList;
    private Context context;
    private List<TimeModel> timeModelList = new ArrayList<>();

    RequestListAdapter(Context context, List<GetRequestResponse.RequestData> requestDataList, List<TimeModel> timeModelList) {
        this.context = context;
        this.requestDataList = requestDataList;
        this.timeModelList = timeModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemRequestBinding binding = ItemRequestBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.binding.setModel(requestDataList.get(i));
        switch (requestDataList.get(i).getStatus()) {
            case "ACTIVE":
                myViewHolder.binding.parent.setStrokeColor(context.getResources().getColor(R.color.colorPrimary));
                myViewHolder.binding.statusTv.setBackground(context.getResources().getDrawable(R.drawable.pending_background));
                break;
            case "APPROVED":
                myViewHolder.binding.parent.setStrokeColor(context.getResources().getColor(R.color.green));
                myViewHolder.binding.statusTv.setBackground(context.getResources().getDrawable(R.drawable.accepted_background));
                break;
            case "REJECTED":
                myViewHolder.binding.parent.setStrokeColor(context.getResources().getColor(R.color.red));
                myViewHolder.binding.statusTv.setBackground(context.getResources().getDrawable(R.drawable.rejected_background));
                break;
        }
        myViewHolder.binding.timeTv.setText(timeModelList.get(requestDataList.get(i).getStartHour()).value
                + context.getResources().getString(R.string.to) + timeModelList.get(requestDataList.get(i).getEndHour()).value);


    }

    @Override
    public int getItemCount() {
        return requestDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRequestBinding binding;

        MyViewHolder(@NonNull ItemRequestBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
