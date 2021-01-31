package com.reffamily.activities_fragments.activity_login;



import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.reffamily.R;
import com.reffamily.activities_fragments.activity_verification_code.VerificationCodeActivity;
import com.reffamily.adapters.CountriesAdapter;
import com.reffamily.databinding.ActivityLoginBinding;
import com.reffamily.databinding.DialogCountriesBinding;
import com.reffamily.interfaces.Listeners;
import com.reffamily.language.Language;
import com.reffamily.models.CountryDataModel;
import com.reffamily.models.CountryModel;
import com.reffamily.models.LoginModel;
import com.reffamily.remote.Api;
import com.reffamily.share.Common;
import com.reffamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Listeners.LoginListener {
    private ActivityLoginBinding binding;
    private LoginModel loginModel;
    private List<CountryModel> countryModelList;
    private CountriesAdapter countriesAdapter;
    private AlertDialog dialog;
    private String lang;
    private String phone_code = "+966";
    private String country_id="142";
    private boolean fromSplash = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new TransitionSet());
            getWindow().setExitTransition(new TransitionSet());


        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        fromSplash = intent.getBooleanExtra("from",true);

    }

    private void initView() {
        countryModelList = new ArrayList<>();
        loginModel = new LoginModel();
        binding.setLoginModel(loginModel);
        binding.setListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        Log.e("lang",lang);
        binding.setLang(lang);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.lanuch);
        binding.cons.startAnimation(animation);
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0"))
                {
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.tvSkip.setOnClickListener(v -> {
//            Intent intent = new Intent(this, SplashLoadingActivity.class);
//            startActivity(intent);
//            finish();
            //navigateToHomeActivity();
        });
        createCountriesDialog();
        getPhoneCodes();
        if (!fromSplash){
            binding.tvSkip.setVisibility(View.GONE);
        }
    }



    private void getPhoneCodes() {
        Api.getService(Tags.base_url)
                .getCountries(lang)
                .enqueue(new Callback<CountryDataModel>() {
                    @Override
                    public void onResponse(Call<CountryDataModel> call, Response<CountryDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            countryModelList.clear();
                            countryModelList.addAll(response.body().getData());
                            countriesAdapter.notifyDataSetChanged();

                            if (countryModelList.size()>0){
                                binding.arrow.setVisibility(View.VISIBLE);
                                if (countryModelList.size()>=1){
                                    country_id = countryModelList.get(1).getId_country();
                                    phone_code = "+"+countryModelList.get(1).getPhone_code();

                                }else {
                                    country_id = countryModelList.get(0).getId_country();
                                    phone_code = "+"+countryModelList.get(0).getPhone_code();
                                }
                                binding.tvCode.setText(phone_code);
                            }else {
                                binding.arrow.setVisibility(View.GONE);

                            }

                        } else {

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CountryDataModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                                }
                                else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                                else {
                                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }


    private void createCountriesDialog() {

        dialog = new AlertDialog.Builder(this)
                .create();
        countriesAdapter = new CountriesAdapter(countryModelList,this);

        DialogCountriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_countries, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(countriesAdapter);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());

    }



    @Override
    public void validate() {
        if (loginModel.isDataValid(this))
        {
            Common.CloseKeyBoard(this,binding.edtPhone);
            navigateToVerificationCodeActivity();
        }
    }

    @Override
    public void showCountryDialog() {
        if (countryModelList.size()>0){
            dialog.show();

        }
    }

    @Override
    public void costumerApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.refCustomerFamily")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.refCustomerFamily")));
        }
    }

    private void navigateToVerificationCodeActivity() {

        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("phone_code",phone_code);
        intent.putExtra("phone",loginModel.getPhone());
        intent.putExtra("country_id",country_id);
        intent.putExtra("from",fromSplash);

        startActivity(intent);
        finish();

    }

    private void navigateToHomeActivity() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }

    public void setItemData(CountryModel countryModel) {
        dialog.dismiss();
        phone_code = "+"+countryModel.getPhone_code();
        binding.tvCode.setText(phone_code);
        country_id = countryModel.getId_country();
    }
}
