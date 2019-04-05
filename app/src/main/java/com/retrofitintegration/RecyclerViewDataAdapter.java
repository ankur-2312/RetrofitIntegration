package com.retrofitintegration;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.MyViewHolder> {

    private ArrayList<PoJo> poJoArrayList;

    RecyclerViewDataAdapter(ArrayList<PoJo> poJoArrayList) {
        this.poJoArrayList = poJoArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflateView = layoutInflater.inflate(R.layout.inflate, parent, false);
        return new MyViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvId.setText(String.valueOf(poJoArrayList.get(position).getId()));
        holder.tvUserID.setText(String.valueOf(poJoArrayList.get(position).getUserId()));
        holder.tvTitle.setText(poJoArrayList.get(position).getTitle());
        holder.tvBody.setText(poJoArrayList.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return poJoArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvId, tvUserID, tvTitle, tvBody;

        MyViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvUserID = itemView.findViewById(R.id.tvUserId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);

        }
    }


}