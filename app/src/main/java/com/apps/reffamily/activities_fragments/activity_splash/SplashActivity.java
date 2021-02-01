package com.apps.reffamily.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.activities_fragments.activity_login.LoginActivity;
import com.apps.reffamily.databinding.ActivitySplashBinding;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.DefaultSettings;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.tags.Tags;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private ConstraintSet constraintSetOld = new ConstraintSet();
    private ConstraintSet constraintSetNew = new ConstraintSet();
    private ConstraintLayout constraintLayout;
    private LinearLayout llLang;
    private boolean status;
    private CardView cardAr,cardEn;
    private TextView tvAr,tvEn;
    private Button btnNext;
    private String lang = "ar";
    private Preferences preferences;





    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase,Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        constraintLayout = findViewById(R.id.layout);
        btnNext = findViewById(R.id.btnNext);
        llLang = findViewById(R.id.llLang);
        cardAr = findViewById(R.id.cardAr);
        cardEn = findViewById(R.id.cardEn);
        tvAr = findViewById(R.id.tvAr);
        tvEn = findViewById(R.id.tvEn);


        if (preferences.getAppSetting(this)!=null&&preferences.getAppSetting(this).isLanguageSelected()){



                if (preferences.getSession(this).equals(Tags.session_login)){
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    },1500);

                }else {

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    },1500);

                }





        }else {
            constraintSetOld.clone(constraintLayout);
            constraintSetNew.clone(this,R.layout.language_layout);
            new Handler().postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Transition transition = new ChangeBounds();
                    transition.setDuration(400);
                    transition.setInterpolator(new AccelerateDecelerateInterpolator());
                    TransitionManager.beginDelayedTransition(constraintLayout,transition);

                }

                if (!status){
                    constraintSetNew.applyTo(constraintLayout);
                }else {
                    constraintSetOld.applyTo(constraintLayout);

                }
                status =!status;


                Animation animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.lanuch);
                llLang.startAnimation(animation);

            },1000);
        }


        cardAr.setOnClickListener(v -> {
            cardAr.setCardElevation(5f);
            cardEn.setCardElevation(0f);
            tvAr.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            tvEn.setTextColor(ContextCompat.getColor(this,R.color.gray6));
            lang = "ar";

        });
        cardEn.setOnClickListener(v -> {
            cardAr.setCardElevation(0f);
            cardEn.setCardElevation(5f);
            tvAr.setTextColor(ContextCompat.getColor(this,R.color.gray6));
            tvEn.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            lang = "en";
        });
        btnNext.setOnClickListener(v -> {
            refreshActivity();
        });

    }

    private void refreshActivity() {
        Paper.init(this);
        Paper.book().write("lang",lang);
        DefaultSettings defaultSettings = new DefaultSettings();
        defaultSettings.setLanguageSelected(true);
        preferences.createUpdateAppSetting(this,defaultSettings);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }









}
