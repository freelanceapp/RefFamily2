package com.apps.reffamily.activities_fragments.activity_update_product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.adapters.ProductSliderAdapter;
import com.apps.reffamily.databinding.ActivityProductDetialsBinding;
import com.apps.reffamily.interfaces.Listeners;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.models.ProductModel;
import com.apps.reffamily.models.SingleProductModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetialsActivity extends AppCompatActivity implements Listeners.BackListener {

    private static final String TAG = "DATA";
    private ActivityProductDetialsBinding binding;
    private String lang;
    private ProductSliderAdapter sliderAdapter;
    private List<SingleProductModel.ImageModel> sliderModelList;
    private SingleProductModel productModel;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);

        getDataFromIntent();
        initView();
//        sliderInit();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            productModel = (SingleProductModel) getIntent().getSerializableExtra("DATA");
        }


    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        binding.setBackListener(this);
        userModel = preferences.getUserData(this);
        if (productModel != null) {
            if (productModel.getProduct_images() != null) {
                sliderModelList.addAll(productModel.getProduct_images());
                sliderInit();
            }

            if (!productModel.getMain_image().isEmpty() && !productModel.getMain_image().equals("0") && productModel.getMain_image() != null) {
                SingleProductModel.ImageModel imageModel = new SingleProductModel.ImageModel();
                imageModel.setImage(productModel.getMain_image());
                sliderModelList.add(imageModel);
                sliderInit();

            }
        }

        binding.setModel(productModel);

        binding.updateBtn.setOnClickListener(view -> {
            navigateToAddproductActivity();

        });
        binding.deleteBtn.setOnClickListener(view -> {
            deleteProduct();

        });


        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void deleteProduct() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url).deleteProduct("Bearer " + userModel.getData().getToken(), productModel.getId() + "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            navigateToHomeActivity();

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ProductDetialsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(ProductDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("error", t.getMessage());
                    }
                });

    }

    private void navigateToAddproductActivity() {
        List<Uri> uriList = new ArrayList<>();
        AddProductModel.Data addProductModel = new AddProductModel.Data();
        Intent intent = new Intent(this, AddProductActivity.class);
        addProductModel.setId(productModel.getId());
        addProductModel.setTitle(productModel.getTitle());
        addProductModel.setDesc(productModel.getDesc());
        addProductModel.setOffer_finished_at(productModel.getOffer_finished_at());
        addProductModel.setOffer_started_at(productModel.getOffer_started_at());

        if (productModel.getProduct_images().size() > 0) {
            for (int i = 0; i < productModel.getProduct_images().size(); i++) {
                uriList.add(Uri.parse(productModel.getProduct_images().get(i).getImage()));
            }
        }
        addProductModel.setImages(uriList);

        sliderModelList.addAll(productModel.getProduct_images());
        addProductModel.setFamily_id(productModel.getFamily_id());
        addProductModel.setSub_category_id(productModel.getCategory_id());
        addProductModel.setMain_image(productModel.getMain_image());
        if (productModel.getOffer_type() != null) {
            addProductModel.setOffer_type(productModel.getOffer_type());
        }
        addProductModel.setHave_offer(productModel.getHave_offer());
        addProductModel.setOffer_value(productModel.getOffer_value() + "");
        addProductModel.setRating_value(productModel.getRating_value());
        addProductModel.setPrice(productModel.getPrice());
        addProductModel.setOld_price(productModel.getOld_price() + "");

        Log.e("pr", productModel.getId() + "");
        intent.putExtra(TAG, addProductModel);

        startActivity(intent);

    }

    private void navigateToHomeActivity() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    //initiate slider ui
    private void sliderInit() {
        //top slider
        sliderAdapter = new ProductSliderAdapter(this, sliderModelList);
        binding.imageSliderTop.setSliderAdapter(sliderAdapter);
        binding.imageSliderTop.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSliderTop.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSliderTop.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSliderTop.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSliderTop.setIndicatorUnselectedColor(Color.GRAY);
//        binding.imageSliderTop.setScrollTimeInSec(3);
//        binding.imageSliderTop.setAutoCycle(true);
//        binding.imageSliderTop.startAutoCycle();

    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void back() {
        finish();
    }
}