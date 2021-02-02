package com.apps.reffamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;


import com.apps.reffamily.R;
import com.apps.reffamily.databinding.SliderRowBinding;
import com.apps.reffamily.models.SingleProductModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ProductSliderAdapter extends SliderViewAdapter<ProductSliderAdapter.SliderAdapterVH1> {

    Context context;
  private List<SingleProductModel.ImageModel> sliderModelList ;



    public ProductSliderAdapter(Context context, List<SingleProductModel.ImageModel> sliderModelList) {
        this.context = context;
        this.sliderModelList=sliderModelList;
    }

    @Override
    public SliderAdapterVH1 onCreateViewHolder(ViewGroup parent) {
        SliderRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider_row, parent, false);
        return new SliderAdapterVH1(binding);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH1 holder, int position) {
        holder.binding.setSliderData(sliderModelList.get(position).getImage());
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }




    class SliderAdapterVH1 extends SliderViewAdapter.ViewHolder {

        SliderRowBinding binding;

        public SliderAdapterVH1(SliderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
