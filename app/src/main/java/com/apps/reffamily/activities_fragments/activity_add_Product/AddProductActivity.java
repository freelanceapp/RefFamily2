package com.apps.reffamily.activities_fragments.activity_add_Product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep1;
import com.apps.reffamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep2;
import com.apps.reffamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep3;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.databinding.ActivityAddProductBinding;
import com.apps.reffamily.interfaces.Listeners;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.models.ProductModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity implements Listeners.BackListener {

    private static final String TAG = "DATA";
    private ActivityAddProductBinding binding;
    private FragmentManager fragmentManager;
    private String lang;
    private int step = 1;
    public AddProductModel.Data addProductModel = null;
    Preferences preferences;
    UserModel userModel;
    private Fragment_AddProductStep1 fragment_addProductStep1;
    private Fragment_AddProductStep2 fragment_addProductStep2;
    private Fragment_AddProductStep3 fragment_addProductStep3;
    public int type = 1; // if = 1 --> add ,,,, if = 2 --> update

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
        if (savedInstanceState == null) {
            displayFragmentStep1(addProductModel);
        }

    }

    private void getDataFromIntent() {
        Intent intent1 = getIntent();
        if (intent1 != null) {
            addProductModel = (AddProductModel.Data) intent1.getSerializableExtra(TAG);
            if (addProductModel != null) {
                type = 2;
                Log.e("update: ", type + "");


            }
        }
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);


        if (addProductModel == null) {
            addProductModel = new AddProductModel.Data();

        }


        binding.nextBtn.setOnClickListener(view -> {
            if (step == 1) {
                if (fragment_addProductStep1 != null && fragment_addProductStep1.isAdded()) {
                    addProductModel = fragment_addProductStep1.addProductModel;

                }
                if (addProductModel.step1(this)) {
                    Log.e("step1", "11");

                    displayFragmentStep2(addProductModel);

                }
            } else if (step == 2) {
                if (fragment_addProductStep2 != null && fragment_addProductStep2.isAdded()) {
                    addProductModel = fragment_addProductStep2.addProductModel;
                }
                Log.e("fff", addProductModel.getOld_price() + "");
                if (addProductModel.step2(this)) {
                    if (type == 2) {
                        binding.nextBtn.setText(getResources().getString(R.string.update));

                    }
                    displayFragmentStep3(addProductModel);
                    Log.e("step2", "22");


                }
            } else if (step == 3) {
                Log.e("fff", addProductModel.getPrice() + "");

                if (fragment_addProductStep3 != null && fragment_addProductStep3.isAdded()) {

                    addProductModel = fragment_addProductStep3.addProductModel;
                }

                    if (type == 2) {

                        updateProduct();
                    } else {
                        addProduct();
                    }

            }


        });


        binding.prevBtn.setOnClickListener(view -> {
            if (step == 2) {
                displayFragmentStep1(addProductModel);
            } else if (step == 3) {
                displayFragmentStep2(addProductModel);

            }

        });


    }


    @Override
    public void onBackPressed() {
        if (step == 1) {
            finish();
        } else if (step == 2) {
            displayFragmentStep1(addProductModel);
        } else if (step == 3) {
            displayFragmentStep2(addProductModel);

        }

    }

    private void displayFragmentStep1(AddProductModel.Data addProductModel) {
        updateStep1UI();
        step = 1;
        if (fragment_addProductStep1 == null) {
            fragment_addProductStep1 = Fragment_AddProductStep1.newInstance(addProductModel);
        }
        if (fragment_addProductStep2 != null && fragment_addProductStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep2).commit();
        }

        if (fragment_addProductStep3 != null && fragment_addProductStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep3).commit();
        }
        if (fragment_addProductStep1.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addProductStep1).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addProductStep1, "fragment_addProductStep1").addToBackStack("fragment_addProductStep1").commit();
        }

    }


    private void displayFragmentStep2(AddProductModel.Data addProductModel) {
        updateStep2UI();
        step = 2;
        if (fragment_addProductStep2 == null) {
            fragment_addProductStep2 = Fragment_AddProductStep2.newInstance(addProductModel);
        }
        if (fragment_addProductStep1 != null && fragment_addProductStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep1).commit();
        }

        if (fragment_addProductStep3 != null && fragment_addProductStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep3).commit();
        }
        if (fragment_addProductStep2.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addProductStep2).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addProductStep2, "fragment_addProductStep2").addToBackStack("fragment_addProductStep2").commit();
        }

    }

    private void displayFragmentStep3(AddProductModel.Data addProductModel) {
        updateStep3UI();
        step = 3;
        if (fragment_addProductStep3 == null) {
            fragment_addProductStep3 = Fragment_AddProductStep3.newInstance(addProductModel);
        }
        if (fragment_addProductStep1 != null && fragment_addProductStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep1).commit();
        }

        if (fragment_addProductStep2 != null && fragment_addProductStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep2).commit();
        }
        if (fragment_addProductStep3.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addProductStep3).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addProductStep3, "fragment_addProductStep3").addToBackStack("fragment_addProductStep3").commit();
        }

    }


    private void updateStep1UI() {
        binding.nextBtn.setText(getResources().getString(R.string.next));
        binding.prevBtn.setVisibility(View.GONE);
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
    }

    private void updateStep2UI() {
        binding.prevBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setText(getResources().getString(R.string.next));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
    }

    private void updateStep3UI() {
        binding.prevBtn.setVisibility(View.VISIBLE);
        if (type == 1) {
            binding.nextBtn.setText(getResources().getString(R.string.addproduct));
        } else {
            binding.nextBtn.setText(getResources().getString(R.string.update));
        }
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
    }


    private void addProduct() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();

        Log.e("category", addProductModel.getSub_category_id() + "");
        Log.e("start", addProductModel.getOffer_started_at() + "");
        Log.e("finish", addProductModel.getOffer_finished_at() + "");
        Log.e("price", addProductModel.getPrice() + "");
        Log.e("have_offer", addProductModel.getHave_offer() + "");

        if (addProductModel.getOffer_value().trim().isEmpty()) {
            addProductModel.setOffer_value("0");
        }
        RequestBody title = Common.getRequestBodyText(addProductModel.getTitle());
//        RequestBody family_id = Common.getRequestBodyText("" + addProductModel.getFamily_id());
        RequestBody sub_category_id = Common.getRequestBodyText("" + addProductModel.getSub_category_id());
        RequestBody price = Common.getRequestBodyText("" + addProductModel.getPrice());
        RequestBody old_price = Common.getRequestBodyText("" + addProductModel.getOld_price());
        RequestBody offer_value = Common.getRequestBodyText(addProductModel.getOffer_value());
        RequestBody offer_type = Common.getRequestBodyText(addProductModel.getOffer_type());
        RequestBody desc = Common.getRequestBodyText(addProductModel.getDesc());
        RequestBody have_offer = Common.getRequestBodyText(addProductModel.getHave_offer());
        RequestBody offer_started_at = Common.getRequestBodyText(addProductModel.getOffer_started_at());
        RequestBody offer_finish_at = Common.getRequestBodyText(addProductModel.getOffer_finished_at());
        MultipartBody.Part main_image = Common.getMultiPart(this, Uri.parse(addProductModel.getMain_image()), "main_image");

        List<MultipartBody.Part> images = getMultipartBodyList(addProductModel.getImages(), "images[]");
//        Log.e("ssssub: ", addProductModel.getSub_category_id() + "  " + family_id + "   " + addProductModel.getOffer_value() + "  0   " + addProductModel.getOffer_type());

        Api.getService(Tags.base_url).addProduct("Bearer " + userModel.getData().getToken(), title, sub_category_id, price, old_price, offer_value, desc, have_offer, offer_type, offer_started_at, offer_finish_at, main_image, images)
                .enqueue(new Callback<ProductModel>() {
                    @Override
                    public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if(response.body().getStatus()==200){
                            Intent intent = new Intent(AddProductActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();}
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (Exception e) {
                                Log.e("aaaaaqqqq", e.toString() + "__");
                            }
                            if (response.code() == 500) {
                                Toast.makeText(AddProductActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(AddProductActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {

                                    Log.e("errorfaild", response.code() + "_" + response.errorBody().string());
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqqfaild", e.toString() + "__");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductModel> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("error", t.getMessage());
                    }
                });

    }

    private void updateProduct() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();


        Log.e("main_image", addProductModel.getMain_image() + "");
        Log.e("category", addProductModel.getSub_category_id() + "");
        Log.e("start", addProductModel.getOffer_started_at() + "");
        Log.e("finish", addProductModel.getOffer_finished_at() + "");
        Log.e("price", addProductModel.getPrice() + "");
        Log.e("have_offer", addProductModel.getHave_offer() + "");
        Log.e("offer_type", addProductModel.getOffer_type() + "");


        RequestBody id = Common.getRequestBodyText("" + addProductModel.getId());
        RequestBody title = Common.getRequestBodyText(addProductModel.getTitle());
        RequestBody family_id = Common.getRequestBodyText("" + addProductModel.getFamily_id());
        RequestBody sub_category_id = Common.getRequestBodyText("" + addProductModel.getSub_category_id());
        RequestBody price = Common.getRequestBodyText("" + addProductModel.getPrice());
        RequestBody old_price = Common.getRequestBodyText("" + addProductModel.getOld_price());
        RequestBody offer_type = Common.getRequestBodyText(addProductModel.getOffer_type());
        if (addProductModel.getOffer_started_at() == null) {
            addProductModel.setOffer_started_at("");
            addProductModel.setOffer_finished_at("");
        }
        RequestBody offer_started_at = Common.getRequestBodyText(addProductModel.getOffer_started_at());
        RequestBody offer_finish_at = Common.getRequestBodyText(addProductModel.getOffer_finished_at());
        RequestBody offer_value = Common.getRequestBodyText("" + addProductModel.getOffer_value());
        RequestBody desc = Common.getRequestBodyText(addProductModel.getDesc());
        RequestBody have_offer = Common.getRequestBodyText(addProductModel.getHave_offer());
        if (addProductModel.getMain_image() != null && addProductModel.getMain_image().contains("products/")) {


            Log.e("imaageText", "imaageText" + addProductModel.getOffer_finished_at());
            RequestBody main_image2 = Common.getRequestBodyText(addProductModel.getMain_image());
            Api.getService(Tags.base_url).updateProduct("Bearer " + userModel.getData().getToken(), addProductModel.getId(), addProductModel.getTitle(), addProductModel.getSub_category_id(), addProductModel.getPrice(), Double.parseDouble(addProductModel.getOld_price()), addProductModel.getOffer_value(), addProductModel.getDesc(), addProductModel.getHave_offer(), addProductModel.getOffer_type(), addProductModel.getOffer_started_at(), addProductModel.getOffer_finished_at())
                    .enqueue(new Callback<ProductModel>() {
                        @Override
                        public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if(response.body().getStatus()==200){
                                    Intent intent = new Intent(AddProductActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();}

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddProductActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddProductActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductModel> call, Throwable t) {
                            dialog.dismiss();
                            Log.e("error", t.getMessage());
                        }
                    });

        } else {
            Log.e("imaageText", "imaageText" + addProductModel.getOffer_finished_at());

            Log.e("MultipartBody", "MultipartBody");
            MultipartBody.Part main_image1 = Common.getMultiPart(this, Uri.parse(addProductModel.getMain_image()), "main_image");
            Api.getService(Tags.base_url).updateProduct("Bearer " + userModel.getData().getToken(), id, title, sub_category_id, price, old_price, offer_value, desc, have_offer, offer_type, offer_started_at, offer_finish_at, main_image1)
                    .enqueue(new Callback<ProductModel>() {
                        @Override
                        public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {

                                if(response.body().getStatus()==200){
                                    Intent intent = new Intent(AddProductActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();}

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddProductActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddProductActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductModel> call, Throwable t) {
                            dialog.dismiss();
                            Log.e("error", t.getMessage());
                        }
                    });
        }
    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri model : uriList) {
            MultipartBody.Part part = Common.getMultiPart(this, Uri.parse(model.toString()), image_cv);
            partList.add(part);
        }
        return partList;
    }


    @Override
    public void back() {
        finish();
    }
}