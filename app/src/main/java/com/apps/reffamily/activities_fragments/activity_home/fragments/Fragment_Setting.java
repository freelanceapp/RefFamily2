package com.apps.reffamily.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_category.CategoryActivity;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.activities_fragments.activity_language.LanguageActivity;
import com.apps.reffamily.activities_fragments.activity_login.LoginActivity;
import com.apps.reffamily.activities_fragments.activity_about_app.AboutAppActivity;
import com.apps.reffamily.activities_fragments.activity_user_feedback.UserFeedbackActivity;
import com.apps.reffamily.databinding.FragmentSettingBinding;
import com.apps.reffamily.interfaces.Listeners;
import com.apps.reffamily.models.BalanceModel;
import com.apps.reffamily.models.DefaultSettings;
import com.apps.reffamily.models.SettingModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment_Setting extends Fragment implements Listeners.SettingAction {

    private HomeActivity activity;
    private FragmentSettingBinding binding;
    private String lang;
    private Preferences preferences;
    private DefaultSettings defaultSettings;
    private UserModel userModel;
    private double balance=0.0;
    private SettingModel settingModel;

    public static Fragment_Setting newInstance() {
        return new Fragment_Setting();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        initView();


        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();

        userModel = preferences.getUserData(getActivity());
        defaultSettings = preferences.getAppSetting(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setActions(this);
        //binding.setBalance(balance);
        binding.setModel(userModel);

//        if (defaultSettings != null) {
//            if (defaultSettings.getRingToneName() != null && !defaultSettings.getRingToneName().isEmpty()) {
//                binding.tvRingtoneName.setText(defaultSettings.getRingToneName());
//            } else {
//                binding.tvRingtoneName.setText(getString(R.string.default1));
//            }
//        } else {
//            binding.tvRingtoneName.setText(getString(R.string.default1));
//
//        }

        binding.switchBtn.setOnClickListener(view -> {
            if (binding.switchBtn.isChecked()){
               // updatePhoneStatus("show");
            }else {
            //    updatePhoneStatus("hidden");

            }
        });
     //   getBalance();
        getBalance();
        getSetting();
    }

    public void getBalance(){
        Api.getService(Tags.base_url).getUserBalance(userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<BalanceModel>() {
                    @Override
                    public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                binding.setBalance(response.body());
//                                binding.tvBalance.setText(String.format(Locale.ENGLISH,"%s %s",response.body().getUser_balance(),currency));
//                                binding.tvTotalRevenue.setText(String.format(Locale.ENGLISH,"%s %s",response.body().getDelivery_fee(),currency));
//                                binding.tvOrderNum.setText(String.format(Locale.ENGLISH,"%s %s",response.body().getOrders(),getString(R.string.order2)));
//                                binding.setRate(response.body().getMy_rate());
//                                if (response.body().getUser_balance()>=0){
//                                    binding.tvBalance.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
//                                }else {
//                                    binding.tvBalance.setTextColor(ContextCompat.getColor(activity,R.color.color_red));
//
//                                }
                            }
                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<BalanceModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }
    private void getSetting(){
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url).getSetting(lang)
                .enqueue(new Callback<SettingModel>() {
                    @Override
                    public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                settingModel = response.body();


                            }
                        } else {

                            dialog.dismiss();

                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SettingModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

//
//    public void updatePhoneStatus(String status){
//        Log.e("status",status);
//        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//
//        Api.getService(Tags.base_url).updatePhoneStatus("Bearer " + userModel.getData().getToken(), userModel.getData().getId(),status)
//                .enqueue(new Callback<UserModel>() {
//                    @Override
//                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                        dialog.dismiss();
//                        if (response.isSuccessful() && response.body() != null) {
//                            userModel = response.body();
//                            preferences.create_update_userdata(activity, response.body());
//                            binding.setModel(response.body());
//                        } else {
//
//                            if (status.equals("show")){
//                                binding.switchBtn.setChecked(false);
//                            }else {
//                                binding.switchBtn.setChecked(true);
//                            }
//
//                            if (response.code() == 500) {
//                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else if (response.code() == 401) {
//                                try {
//                                    Log.e("errorCode:", response.code() + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserModel> call, Throwable t) {
//                        dialog.dismiss();
//                        if (status.equals("show")){
//                            binding.switchBtn.setChecked(false);
//                        }else {
//                            binding.switchBtn.setChecked(true);
//                        }
//                        Log.e("error Profile", t.getMessage());
//                    }
//                });
//    }
    @Override
    public void onSubscriptions() {
//        Intent intent = new Intent(activity, SubscriptionActivity.class);
//        intent.putExtra("data", preferences.getUserData(activity));
//        startActivity(intent);
//        getActivity().finish();
    }

    @Override
    public void onProfile() {
//        Intent intent = new Intent(activity, ProfileActivity.class);
//        intent.putExtra("data", preferences.getUserData(activity));
//        startActivity(intent);
    }

    @Override
    public void onEditProfile() {
//        Intent intent = new Intent(activity, UpdateProfileActivity.class);
//        intent.putExtra("data", preferences.getUserData(activity));
//        startActivityForResult(intent, 2);
    }

    @Override
    public void onFeedback() {
        Intent intent = new Intent(activity, UserFeedbackActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLanguageSetting() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void onTerms() {
        if (settingModel != null) {
            Intent intent = new Intent(activity, AboutAppActivity.class);
            String url = Tags.base_url + settingModel.getSettings().getTerms_and_conditions();
            intent.putExtra("url", url);
            startActivity(intent);
        } else {
            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrivacy() {
        if (settingModel != null) {
            Intent intent = new Intent(activity, AboutAppActivity.class);
            String url = Tags.base_url + settingModel.getSettings().getPrivacy_policy();
            intent.putExtra("url", url);
            startActivity(intent);
        } else {
            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRate() {
        String appId = activity.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = activity.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            startActivity(webIntent);
        }
    }

    @Override
    public void adjust() {
//        Intent intent = new Intent(activity, AdjustmnetActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onTone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 100);
    }

    @Override
    public void about() {
        if (settingModel != null) {
            Intent intent = new Intent(activity, AboutAppActivity.class);
            String url = Tags.base_url + settingModel.getSettings().getTerms_and_conditions();
            intent.putExtra("url", url);
            startActivity(intent);
        } else {
            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void logout() {
//        if (userModel != null) {
//            ProgressDialog dialog = Common.createProgressDialog(getActivity(), getString(R.string.wait));
//            dialog.show();
//            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    String phone_token = task.getResult().getToken();
//                    Api.getService(Tags.base_url).logout("Bearer " + userModel.getData().getToken(), phone_token).enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful()) {
//                                preferences.clear(getActivity());
//                                navigateToSignInActivity();
//                            } else {
//                                dialog.dismiss();
//                                try {
//                                    Log.e("error", response.code() + "__" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if (response.code() == 500) {
//                                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage() != null) {
//                                    Log.e("error", t.getMessage() + "__");
//
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        Toast.makeText(getActivity(), getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                Log.e("Error", e.getMessage() + "__");
//                            }
//
//                        }
//                    });
//                }
//            });
//        } else {
//            navigateToSignInActivity();
//        }
    }

    private void navigateToSignInActivity() {

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.finish();
        startActivity(intent);
    }

    @Override
    public void subCategories() {
        Intent intent = new Intent(activity, CategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+activity.getPackageName());
        startActivity(intent);
    }

    @Override
    public void contactUs() {
//        Intent intent = new Intent(activity, ContactUsActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

//
//            if (uri != null) {
//                Ringtone ringtone = RingtoneManager.getRingtone(activity, uri);
//                String name = ringtone.getTitle(activity);
//                binding.tvRingtoneName.setText(name);
//
//                if (defaultSettings == null) {
//                    defaultSettings = new DefaultSettings();
//                }
//
//                defaultSettings.setRingToneUri(uri.toString());
//                defaultSettings.setRingToneName(name);
//                preferences.createUpdateAppSetting(activity, defaultSettings);
//
//
//            }
//        } else if (requestCode == 200 && resultCode == RESULT_OK) {
//
//            activity.setResult(RESULT_OK);
//            activity.finish();
//        }
        }
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        if(preferences!=null){
//            userModel=preferences.getUserData(activity);
//            binding.setModel(userModel);
//        }
//    }
}
