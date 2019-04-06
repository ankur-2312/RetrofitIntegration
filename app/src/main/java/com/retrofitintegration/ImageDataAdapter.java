package com.retrofitintegration;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.ArrayList;


public class ImageDataAdapter extends RecyclerView.Adapter<ImageDataAdapter.MyViewHolder> {
    private ArrayList<Bitmap> imageBitmap;
    Uri uri;

    ImageDataAdapter(ArrayList<Bitmap> imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflateView = layoutInflater.inflate(R.layout.inflateimage, parent, false);
        return new MyViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Glide.with(MyApplication.getContext()).load(imageBitmap.get(position)).circleCrop().into(holder.ivImage);
        }
        holder.ivImage.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return imageBitmap.size();
    }

    //View holder to hold the views of inflated layout
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        MyViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}