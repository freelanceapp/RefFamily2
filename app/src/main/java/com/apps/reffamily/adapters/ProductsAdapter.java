package com.apps.reffamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.reffamily.R;
import com.apps.reffamily.databinding.ProductRowBinding;
import com.apps.reffamily.models.SingleProductModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterVH> {

    private List<SingleProductModel> dataList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    int i = 0;

    private Fragment fragment;


    public ProductsAdapter(Context context) {
        this.context = context;
    }

    public ProductsAdapter(List<SingleProductModel> dataList, Context context, Fragment fragment) {
        this.dataList = dataList;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public ProductsAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_row, parent, false);
        return new ProductsAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapterVH holder, int position) {

        holder.binding.setData(dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        if (position == 0) {
//            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg_1));
//            holder.binding.name.setTextColor(context.getResources().getColor(R.color.white));
//
//        }



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ProductsAdapterVH extends RecyclerView.ViewHolder {
        public ProductRowBinding binding;

        public ProductsAdapterVH(@NonNull ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
