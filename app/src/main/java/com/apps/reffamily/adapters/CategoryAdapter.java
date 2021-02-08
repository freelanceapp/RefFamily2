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
import com.apps.reffamily.activities_fragments.activity_home.fragments.Fragment_Main;
import com.apps.reffamily.databinding.CategoryRowBinding;
import com.apps.reffamily.models.SingleCategoryModel;
import com.apps.reffamily.models.SingleSubCategoryModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterVH> {

    private List<SingleSubCategoryModel> dataList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    int i = 0;

    private Fragment fragment;



    public CategoryAdapter(List<SingleSubCategoryModel> dataList, Context context, Fragment fragment) {
        this.dataList = dataList;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public CategoryAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.category_row, parent, false);
        return new CategoryAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterVH holder, int position) {

        holder.binding.setData(dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = position;
                notifyDataSetChanged();

            }
        });

//        if (position == 0) {
//            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg_1));
//            holder.binding.name.setTextColor(context.getResources().getColor(R.color.white));
//
//        }
        if (i == position) {
            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg_1));
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.white));
            if (fragment instanceof Fragment_Main) {
                Fragment_Main fragment_main = (Fragment_Main) fragment;
                fragment_main.showproduct(position);
            }
        } else {
            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg));
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.gray12));
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CategoryAdapterVH extends RecyclerView.ViewHolder {
        public CategoryRowBinding binding;

        public CategoryAdapterVH(@NonNull CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
