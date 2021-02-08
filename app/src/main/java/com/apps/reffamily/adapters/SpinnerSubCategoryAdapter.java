
package com.apps.reffamily.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.apps.reffamily.R;
import com.apps.reffamily.databinding.SpinnerCategoryRowBinding;
import com.apps.reffamily.models.SingleCategoryModel;
import com.apps.reffamily.models.SingleSubCategoryModel;

import java.util.List;

import io.paperdb.Paper;

public class SpinnerSubCategoryAdapter extends BaseAdapter {
    private List<SingleSubCategoryModel> data;
    private Context context;
    private String lang;

    public SpinnerSubCategoryAdapter(List<SingleSubCategoryModel> data, Context context) {
        this.data = data;
        this.context = context;
        lang = Paper.book().read("lang","ar");

    }

    @Override
    public int getCount() {
        if (data.size() == 0)
            return 0;
        else
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_category_row,viewGroup,false);

       binding.setData(data.get(i).getTitle());

        return binding.getRoot();
    }
}
