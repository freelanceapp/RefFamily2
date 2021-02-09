package com.apps.reffamily.activities_fragments.activity_language;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.apps.reffamily.R;
import com.apps.reffamily.databinding.ActivityLanguageBinding;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageActivity extends AppCompatActivity {

    private ActivityLanguageBinding binding;
    private String lang;
    private boolean canSelect = false;
    private String selectedLang="";
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        initView();
    }

    private void initView() {
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        selectedLang = lang;

        binding.setLang(lang);
        binding.close.setOnClickListener(v -> finish());
        if (lang.equals("ar"))
        {
            binding.tvAr.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.imageAr.setVisibility(View.VISIBLE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this,R.color.color4));
            binding.imageEn.setVisibility(View.GONE);

        }else {
            binding.tvAr.setTextColor(ContextCompat.getColor(this,R.color.color4));
            binding.imageAr.setVisibility(View.GONE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.imageEn.setVisibility(View.VISIBLE);
        }

        binding.consAr.setOnClickListener(v -> {

            binding.tvAr.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.imageAr.setVisibility(View.VISIBLE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this,R.color.color4));
            binding.imageEn.setVisibility(View.GONE);

            if (lang.equals("ar")){
                selectedLang = lang;
                canSelect = false;

            }else {

                canSelect = true;
                selectedLang = "ar";


            }

            updateBtnUi();
        });


        binding.consEn.setOnClickListener(v -> {
            binding.tvAr.setTextColor(ContextCompat.getColor(this,R.color.color4));
            binding.imageAr.setVisibility(View.GONE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.imageEn.setVisibility(View.VISIBLE);

            if (lang.equals("ar")){

                canSelect = true;
                selectedLang = "en";

            }else {
                selectedLang = lang;
                canSelect = false;
            }

            updateBtnUi();

        });


        binding.btnConfirm.setOnClickListener(v -> {
            if (canSelect){

//                if (userModel==null){
                    Paper.book().write("lang",selectedLang);
                    Language.setLocale(this,selectedLang);
                    setResult(RESULT_OK);
                    finish();
//                }else {
//                    getUserData();
//                }


            }
        });
    }

    private void updateBtnUi() {
        if (canSelect){
            binding.btnConfirm.setTextColor(ContextCompat.getColor(this,R.color.white));
            binding.btnConfirm.setBackgroundResource(R.drawable.small_rounded_primary);
        }else {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(this,R.color.gray9));
            binding.btnConfirm.setBackgroundResource(R.drawable.small_rounded_gray);
        }
    }


//    private void getUserData() {
//        Api.getService(Tags.base_url)
//                .getUserById(userModel.getData().getToken(),lang,userModel.getData().getId())
//                .enqueue(new Callback<UserModel>() {
//                    @Override
//                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                        if (response.isSuccessful()) {
//
//                            userModel = response.body();
//                            preferences.create_update_userdata(com.apps.ref.activities_fragments.activity_language.LanguageActivity.this,userModel);
//
//                            Paper.book().write("lang",selectedLang);
//                            Language.setLocale(com.apps.ref.activities_fragments.activity_language.LanguageActivity.this,selectedLang);
//                            setResult(RESULT_OK);
//                            finish();
//
//                        } else {
//                            try {
//                                Log.e("error", response.code() + "__" + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (response.code() == 500) {
//                                Toast.makeText(com.apps.ref.activities_fragments.activity_language.LanguageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(com.apps.ref.activities_fragments.activity_language.LanguageActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserModel> call, Throwable t) {
//                        try {
//                            if (t.getMessage() != null) {
//                                Log.e("error", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(com.apps.ref.activities_fragments.activity_language.LanguageActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(com.apps.ref.activities_fragments.activity_language.LanguageActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });
//    }

}