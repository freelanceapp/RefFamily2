package com.apps.reffamily.activities_fragments.activity_add_Product.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.apps.reffamily.adapters.ImagesAdapter;
import com.apps.reffamily.databinding.DialogSelectImageBinding;
import com.apps.reffamily.databinding.FragmentAddProductStep1Binding;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;


public class Fragment_AddProductStep1 extends Fragment {
    private static final String TAG = "DATA";
    private AddProductActivity activity;
    private FragmentAddProductStep1Binding binding;
    public AddProductModel.Data addProductModel;
    private Preferences preferences;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url, uri = null;
    private List<Uri> urlList;

    private int type;

    private ImagesAdapter imagesAdapter;


    public static Fragment_AddProductStep1 newInstance(AddProductModel.Data addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, addProductModel);
        Fragment_AddProductStep1 fragment_addProductStep1 = new Fragment_AddProductStep1();
        fragment_addProductStep1.setArguments(bundle);
        return fragment_addProductStep1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_product_step1, container, false);

        initView();
        getData();
        return binding.getRoot();
    }

    private void getData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            addProductModel = (AddProductModel.Data) bundle.getSerializable(TAG);
            if (!addProductModel.getTitle().equals("")) {
                binding.linearImages.setVisibility(View.GONE);

            } else {
                binding.linearImages.setVisibility(View.VISIBLE);
            }
            binding.etOfferName.setText(addProductModel.getTitle());
            binding.etOfferDesc.setText(addProductModel.getDesc());
            if (addProductModel.getMain_image() != null) {
                if (!addProductModel.getMain_image().equals(""))
                    Picasso.get().load(Tags.IMAGE_URL + Uri.parse(addProductModel.getMain_image())).into(binding.image1);
            }
            if (urlList.size() == 0)
                urlList.addAll(addProductModel.getImages());
            imagesAdapter.notifyDataSetChanged();
            binding.setModel(addProductModel);
        }
        else {
            binding.setModel(addProductModel);
        }

    }

    private void initView() {
        preferences = Preferences.getInstance();
        urlList = new ArrayList<>();
        imagesAdapter = new ImagesAdapter(urlList, getContext(), this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(imagesAdapter);


        binding.image1.setOnClickListener(view -> {
            type = 1;
            CreateImageAlertDialog();
        });
        binding.image2.setOnClickListener(view -> {
            type = 2;
            CreateImageAlertDialog();
        });

    }


    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(getContext(), uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(getContext(), R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            url = getUriFromBitmap(bitmap);
            if (type == 2) {
               // imageModel.setImage(url.toString());

                urlList.add(url);
                imagesAdapter.notifyDataSetChanged();
                addProductModel.setImages(urlList);
            } else {
                uri = url;
                addProductModel.setMain_image(uri.toString());
                Picasso.get().load(url).into(binding.image1);
            }
        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            url = data.getData();
            if (type == 2) {

                urlList.add(url);
                imagesAdapter.notifyDataSetChanged();
                addProductModel.setImages(urlList);
            } else {
                uri = url;
                addProductModel.setMain_image(uri.toString());
                Picasso.get().load(url).into(binding.image1);
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContext().getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);
        } catch (SecurityException e) {
            Toast.makeText(getContext(), getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(
                    getContext(), getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void deleteImage(int adapterPosition) {
        urlList.remove(adapterPosition);
        imagesAdapter.notifyItemRemoved(adapterPosition);
//        addProductModel.setImages(urlList);
    }

}