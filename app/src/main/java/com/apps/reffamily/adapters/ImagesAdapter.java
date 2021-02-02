package com.apps.reffamily.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep1;
import com.apps.reffamily.databinding.ImageRowBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {

    private List<Uri> urlList;
    private Context context;
    private Fragment fragment;



    public ImagesAdapter(List<Uri> urlList, Context context, Fragment fragment) {
        this.urlList = urlList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        holder.imageRowBinding.setUrl(urlList.get(position).toString());

        holder.imageRowBinding.imageDelete.setOnClickListener(view -> {
            if(fragment instanceof Fragment_AddProductStep1){
                Fragment_AddProductStep1 fragment_addProductStep1 = (Fragment_AddProductStep1) fragment;
                fragment_addProductStep1.deleteImage(holder.getAdapterPosition());}


                }
        );


    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageRowBinding imageRowBinding;

        public MyHolder(ImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;



        }


    }
}
