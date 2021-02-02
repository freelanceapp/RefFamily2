package com.apps.reffamily.activities_fragments.activity_add_Product.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.reffamily.R;
import com.apps.reffamily.adapters.ImagesAdapter;
import com.apps.reffamily.databinding.FragmentAddProductStep3Binding;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_AddProductStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AddProductStep3 extends Fragment {
    private static final String TAG = "DATA";

    private FragmentAddProductStep3Binding binding;
    public AddProductModel.Data addProductModel;
    private List<Uri> urlList;
    private ImagesAdapter imagesAdapter;
    private Preferences preferences;


    public static Fragment_AddProductStep3 newInstance(AddProductModel.Data addProductModel) {
        Bundle bundle = new Bundle();
        Fragment_AddProductStep3 fragment_addProductStep3 = new Fragment_AddProductStep3();
        bundle.putSerializable(TAG, addProductModel);
        fragment_addProductStep3.setArguments(bundle);
        return fragment_addProductStep3;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_product_step3, container, false);

        initView();
        getData();
        return binding.getRoot();
    }

    private void getData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            addProductModel = (AddProductModel.Data) bundle.getSerializable(TAG);
            // Log.e("imageUrl", addProductModel.getMain_image() + "");
            binding.setModel(addProductModel);

            urlList.addAll(addProductModel.getImages());
            imagesAdapter.notifyDataSetChanged();

            if (addProductModel.getHave_offer().equals("with_offer")) {
                binding.tvDiscount.setVisibility(View.VISIBLE);
                binding.tvDiscountPercentage.setVisibility(View.VISIBLE);
                binding.constraintDiscount.setVisibility(View.VISIBLE);
                double price_after_offer = Double.parseDouble(addProductModel.getOld_price()) - (Double.parseDouble(addProductModel.getOld_price()) * (Integer.parseInt(addProductModel.getOffer_value()) / 100.0));
                if (addProductModel.getOffer_type().equals("per")) {
                    addProductModel.setPrice(price_after_offer);
                } else {
                    addProductModel.setPrice(Double.parseDouble(addProductModel.getOld_price()) - Integer.parseInt(addProductModel.getOffer_value()));
                }

                binding.setModel(addProductModel);

            } else {

                addProductModel.setPrice(Double.parseDouble(addProductModel.getOld_price()));
                binding.setModel(addProductModel);
                binding.tvDiscount.setVisibility(View.GONE);
                binding.tvDiscountPercentage.setVisibility(View.GONE);
                binding.constraintDiscount.setVisibility(View.GONE);
            }
            if (addProductModel.getMain_image().contains("content")) {
                Picasso.get().load(Uri.parse(addProductModel.getMain_image())).into(binding.image1);
            } else {
                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + addProductModel.getMain_image())).into(binding.image1);

            }

            binding.setModel(addProductModel);
        }
    }

    private void initView() {

        preferences = Preferences.getInstance();
        urlList = new ArrayList<>();
        imagesAdapter = new ImagesAdapter(urlList, getContext(), this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(imagesAdapter);


    }


}