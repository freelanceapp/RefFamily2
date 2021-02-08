package com.apps.reffamily.activities_fragments.activity_add_sub_category;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.databinding.ActivityAddCategoryBinding;
import com.apps.reffamily.interfaces.Listeners;
import com.apps.reffamily.language.Language;
import com.apps.reffamily.models.AddSubCategoryModel;
import com.apps.reffamily.models.SingleSubCategoryModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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

public class AddSubCategoryActivity extends AppCompatActivity implements Listeners.AddSubCategoryListener {

    private ActivityAddCategoryBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    private String lang;
    private AddSubCategoryModel addSubCategoryModel;

    private Uri uri = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_category);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        addSubCategoryModel = new AddSubCategoryModel();
        binding.setModel(addSubCategoryModel);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        // getCategoryData();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataValid();
            }
        });
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });


    }


    @Override
    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    @Override
    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }


    @Override
    public void checkDataValid() {

        if (addSubCategoryModel.isDataValid(this)) {
            Common.CloseKeyBoard(this, binding.edtSubCategoryName);
            addSubCategory();
        }

    }

    @Override
    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    @Override
    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void addSubCategory() {
        if (uri == null) {
            addSubCategoryWithoutImage();
        } else {
            addSubCategoryModel.setImage(uri);
            addSubCategoryWithImage();
        }
    }

    private void addSubCategoryWithoutImage() {
//        String familyId = "";
//        String categoryId = "";

//        if (userModel != null) {
//            familyId = userModel.getData().getId() + "";
//            categoryId = userModel.getData().getCategory().getId() + "";
//
//        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .AddSubCategoryWithoutImage("Bearer " + userModel.getData().getToken(), addSubCategoryModel.getTitle(), addSubCategoryModel.getDesc())
                .enqueue(new Callback<SingleSubCategoryModel>() {
                    @Override
                    public void onResponse(Call<SingleSubCategoryModel> call, Response<SingleSubCategoryModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(AddSubCategoryActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                            navigateToHomeActivity();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(AddSubCategoryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("ERROR", response.message() + "");

                                Toast.makeText(AddSubCategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<SingleSubCategoryModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(AddSubCategoryActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddSubCategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void addSubCategoryWithImage() {
        try {


            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody title_part = Common.getRequestBodyText(addSubCategoryModel.getTitle());
            RequestBody desc_part = Common.getRequestBodyText(addSubCategoryModel.getDesc());
//            RequestBody category_id_part = Common.getRequestBodyText(addSubCategoryModel.getCategory_id() + "");
//            RequestBody family_id_part = Common.getRequestBodyText(userModel.getData().getId() + "");
            MultipartBody.Part image = Common.getMultiPart(this, uri, "image");

            Api.getService(Tags.base_url)
                    .AddSubCategoryWithImage("Bearer " + userModel.getData().getToken(), title_part, desc_part, image)
                    .enqueue(new Callback<SingleSubCategoryModel>() {
                        @Override
                        public void onResponse(Call<SingleSubCategoryModel> call, Response<SingleSubCategoryModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddSubCategoryActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                navigateToHomeActivity();
                            } else {
                                try {
                                    Log.e("dkdkkd", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(AddSubCategoryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddSubCategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleSubCategoryModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("msg_category_error", t.getMessage() + "__");

                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddSubCategoryActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddSubCategoryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage() + "__");
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void back() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);


            Picasso.get().load(uri).into(binding.image1);

        } else if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            Picasso.get().load(uri).into(binding.image1);

        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);
        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(
                    this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}