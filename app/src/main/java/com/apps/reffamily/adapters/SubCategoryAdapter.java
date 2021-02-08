package com.apps.reffamily.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_category.CategoryActivity;
import com.apps.reffamily.databinding.SubcategoryRowBinding;
import com.apps.reffamily.models.SingleSubCategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleSubCategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private CategoryActivity activity;

    public SubCategoryAdapter(List<SingleSubCategoryModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (CategoryActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SubcategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.subcategory_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        myHolder.itemView.setOnClickListener(v -> {

        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public SubcategoryRowBinding binding;

        public MyHolder(@NonNull SubcategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
