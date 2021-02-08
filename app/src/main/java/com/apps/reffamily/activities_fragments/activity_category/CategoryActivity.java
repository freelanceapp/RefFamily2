package com.apps.reffamily.activities_fragments.activity_category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_add_sub_category.AddSubCategoryActivity;
import com.apps.reffamily.adapters.SubCategoryAdapter;
import com.apps.reffamily.databinding.ActivityCategoryBinding;
import com.apps.reffamily.interfaces.Listeners;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.AllSubCategoryModel;
import com.apps.reffamily.models.SingleSubCategoryModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityCategoryBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;

    private List<SingleSubCategoryModel> subCategoriesList;
    private SubCategoryAdapter subCategoryAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        subCategoriesList = new ArrayList<>();
        binding.setBackListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        binding.recViewSubCategories.setLayoutManager(new LinearLayoutManager(this));

        subCategoryAdapter = new SubCategoryAdapter(subCategoriesList, this);
        binding.recViewSubCategories.setAdapter(subCategoryAdapter);


        binding.addBtn.setOnClickListener(View -> {

            NavigateToAddSubCategoryActivity();
        });

        getCategoryData();
    }

    private void NavigateToAddSubCategoryActivity() {
        Intent intent = new Intent(this, AddSubCategoryActivity.class);
        startActivity(intent);
    }

    private void getCategoryData() {

        binding.progBarOffer.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getcategories("Bearer " + userModel.getData().getToken()).enqueue(new Callback<AllSubCategoryModel>() {
            @Override
            public void onResponse(Call<AllSubCategoryModel> call, Response<AllSubCategoryModel> response) {
                binding.progBarOffer.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getStatus() == 200) {
                        Log.e("dlldldlld", response.code() + "dldlldldld");

                        if (response.body().getData().size() > 0) {
                            subCategoriesList.clear();
                            subCategoriesList.addAll(response.body().getData());
                            subCategoryAdapter.notifyDataSetChanged();
                        } else {
                            binding.linearNoData.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Log.e("dlldldlld", "dldlldldld");
                        binding.linearNoData.setVisibility(View.VISIBLE);
                    }


                } else {
                    try {

                        Log.e("errorssssss", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(CategoryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(CategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllSubCategoryModel> call, Throwable t) {
                binding.progBarOffer.setVisibility(View.GONE);
                try {
                    binding.progBarOffer.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(CategoryActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }
}