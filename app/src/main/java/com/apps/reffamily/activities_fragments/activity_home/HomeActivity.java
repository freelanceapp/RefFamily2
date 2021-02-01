package com.apps.reffamily.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.fragments.Fragment_Main;
import com.apps.reffamily.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.apps.reffamily.activities_fragments.activity_home.fragments.Fragment_Orders;
import com.apps.reffamily.activities_fragments.activity_home.fragments.Fragment_Setting;
import com.apps.reffamily.databinding.ActivityHomeBinding;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Orders fragment_orders;
    private Fragment_Offers fragment_offers;
    private Fragment_Setting fragment_setting;
    private UserModel userModel;
    private String lang;
    private String token;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();



    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        userModel = preferences.getUserData(this);
//        if (userModel.getData().getNotification_status().equals("on")) {
//            binding.switchBtn.setChecked(true);
//        } else {
//            binding.switchBtn.setChecked(false);
//        }

        updateHomeUi();
        displayFragmentMain();

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        updateHomeUi();
                        displayFragmentMain();
                        break;
                    case R.id.orders:
                        updateOrderUi();
                        displayFragmentOrder();
                        break;
                    case R.id.offers:
                        updateOfferUi();
                        displayFragmentOffers();
                        break;
                    case R.id.setting:
                        updateSettingsUi();
                        displayFragmentSettings();
                        break;

                }

                return true;
            }
        });

//        binding.ivPreviousOrder.setOnClickListener(view -> {
//            Intent intent = new Intent(HomeActivity.this, PreviousOrderActivity.class);
//            startActivity(intent);
//        });
//        binding.flNotification.setOnClickListener(view -> {
//            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
//            startActivity(intent);
//        });
//
//        if (userModel.getData().getNotification_status().equals("on")) {
//            binding.switchBtn.setChecked(true);
//
//        } else {
//            binding.switchBtn.setChecked(false);
//
//        }


//        binding.switchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                UserModel.User user = new UserModel.User();
//
//                if (isChecked) {
//                    updateStatus("on");
//                    user.setNotification_status("on");
//                } else {
//                    updateStatus("off");
//                    user.setNotification_status("off");
//
//                }
//            }
//        });

        if (userModel != null) {
            //updateToken();
        }
        //getProfile();

    }


//    private void updateToken() {
//        FirebaseInstanceId.getInstance()
//                .getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()) {
//                            String token = task.getResult().getToken();
//                            task.getResult().getId();
//                            Log.e("sssssss", token);
//                            Api.getService(Tags.base_url)
//                                    .updateToken("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), token, "android ")
//                                    .enqueue(new Callback<ResponseBody>() {
//                                        @Override
//                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                                            if (response.isSuccessful()) {
//                                                try {
//                                                    Log.e("Success", "token updated");
//                                                } catch (Exception e) {
//                                                    //  e.printStackTrace();
//                                                }
//                                            } else {
//                                                try {
//                                                    Log.e("error", response.code() + "_" + response.errorBody().string());
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                            try {
//                                                Log.e("Error", t.getMessage());
//                                            } catch (Exception e) {
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                });
//    }

    private void updateHomeUi() {
        binding.title.setText(getResources().getString(R.string.home));
        binding.logo.setVisibility(View.VISIBLE);
        binding.linearNotification.setVisibility(View.VISIBLE);
        binding.ivPreviousOrder.setVisibility(View.GONE);
    }

    private void updateOrderUi() {
        binding.title.setText(getResources().getString(R.string.orders));
        binding.linearNotification.setVisibility(View.GONE);
        binding.logo.setVisibility(View.GONE);
        binding.ivPreviousOrder.setVisibility(View.VISIBLE);
    }

    private void updateOfferUi() {
        binding.title.setText(getResources().getString(R.string.offers));
        binding.logo.setVisibility(View.VISIBLE);
        binding.linearNotification.setVisibility(View.VISIBLE);
        binding.ivPreviousOrder.setVisibility(View.GONE);
    }

    private void updateSettingsUi() {
        binding.title.setText(getResources().getString(R.string.settings));
        binding.logo.setVisibility(View.VISIBLE);
        binding.linearNotification.setVisibility(View.VISIBLE);
        binding.ivPreviousOrder.setVisibility(View.GONE);
    }



    public void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }



            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }

            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }

            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }

            //  binding.setTitle(getString(R.string.home));
        } catch (Exception e) {
        }

    }

    public void displayFragmentOrder() {
        try {
            if (fragment_orders == null) {
                fragment_orders = Fragment_Orders.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }

            if (fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_orders).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

            }


        } catch (Exception e) {
        }

    }

    public void displayFragmentSettings() {
        try {
            if (fragment_setting == null) {
                fragment_setting = Fragment_Setting.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }

            if (fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_setting).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_setting, "fragment_setting").addToBackStack("fragment_setting").commit();

            }


        } catch (Exception e) {
        }

    }

//    public void displayFragmentProfile() {
//        try {
//            if (fragment_profile == null) {
//                fragment_profile = Fragment_Profile.newInstance();
//            }
//
//
//            if (fragment_offers != null && fragment_offers.isAdded()) {
//                fragmentManager.beginTransaction().hide(fragment_offers).commit();
//            }
//            if (fragment_main != null && fragment_main.isAdded()) {
//                fragmentManager.beginTransaction().hide(fragment_main).commit();
//            }
//
//            if (fragment_orders != null && fragment_orders.isAdded()) {
//                fragmentManager.beginTransaction().hide(fragment_orders).commit();
//            }
//            if (fragment_setting != null && fragment_setting.isAdded()) {
//                fragmentManager.beginTransaction().hide(fragment_setting).commit();
//            }
//            if (fragment_profile.isAdded()) {
//                fragmentManager.beginTransaction().show(fragment_profile).commit();
//
//            } else {
//                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();
//
//            }
//
//        } catch (Exception e) {
//        }
//
//    }

    public void displayFragmentOffers() {
        try {
            if (fragment_offers == null) {
                fragment_offers = Fragment_Offers.newInstance();
            }



            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_offers).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_offers, "fragment_offers").addToBackStack("fragment_offers").commit();

            }



        } catch (Exception e) {
        }

    }

    @Override
    public void onBackPressed() {

        back();
    }


//    private void updateStatus(String off) {
//        Api.getService(Tags.base_url)
//                .updateStatus("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), off)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            if (off.equals("off")) {
//                                Toast.makeText(HomeActivity.this, getResources().getString(R.string.notifications) + "   " + getResources().getString(R.string.off), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(HomeActivity.this, getResources().getString(R.string.notifications) + "  " + getResources().getString(R.string.on), Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        } else {
//                            try {
//                                Log.e("mmmmmmmmmm", response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            if (response.code() == 500) {
//                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                Log.e("faild", response.message());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        try {
//                            if (t.getMessage() != null) {
//                                Log.e("msg_category_error", t.toString() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Log.e("faild", t.getMessage());
//                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });
//    }


    private void back() {
        if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
            finish();
        } else {
            updateHomeUi();
            binding.bottomNav.setSelectedItemId(R.id.home);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*getProfile();
        if (fragment_main != null) {
            fragment_main.getProductData();
        }*/
    }

//    private void getProfile() {
//
//
//        Api.getService(Tags.base_url).getProfile("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
//                .enqueue(new Callback<UserModel>() {
//                    @Override
//                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            userModel = response.body();
//                            preferences.create_update_userdata(HomeActivity.this, response.body());
//
//                            // binding.setModel(userModel);
//                        } else {
//
//
//                            if (response.code() == 500) {
//                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else if (response.code() == 401) {
//                                try {
//                                    Log.e("errorCode:", response.code() + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserModel> call, Throwable t) {
//                        Log.e("error Profile", t.getMessage());
//                    }
//                });
//    }

}
