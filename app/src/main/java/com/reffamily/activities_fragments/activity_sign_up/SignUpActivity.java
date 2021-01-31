package com.reffamily.activities_fragments.activity_sign_up;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.reffamily.R;
import com.reffamily.adapters.CateegoryAdapter;
import com.reffamily.databinding.ActivitySignUpBinding;
import com.reffamily.interfaces.Listeners;
import com.reffamily.language.Language;
import com.reffamily.models.AllCatogryModel;
import com.reffamily.models.PlaceGeocodeData;
import com.reffamily.models.PlaceMapDetailsData;
import com.reffamily.models.SignUpModel;
import com.reffamily.models.SingleCategoryModel;
import com.reffamily.models.UserModel;
import com.reffamily.preferences.Preferences;
import com.reffamily.remote.Api;
import com.reffamily.share.Common;
import com.reffamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class SignUpActivity extends AppCompatActivity implements Listeners.SignUpListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivitySignUpBinding binding;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private int typeimage = -1;
    private Uri uri = null;
    private Uri uribanner = null;
    private SignUpModel signUpModel;
    private Preferences preferences;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private String lang;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private GoogleMap mMap;
    private double lat, lng;
    private Marker marker;
    private final float zoom = 15.6f;
    private SingleCategoryModel categoryModel;
    private List<SingleCategoryModel> categoryList = new ArrayList<>();
    private CateegoryAdapter cateegoryAdapter;
    private String country_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.onAttach(newBase,Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
        getDataFromIntent();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        signUpModel = new SignUpModel();
        binding.setModel(signUpModel);
        binding.setListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        cateegoryAdapter = new CateegoryAdapter(categoryList, this);
        binding.spinnerCategory.setAdapter(cateegoryAdapter);

        categoryModel = new SingleCategoryModel();
        categoryModel.setId(0);
        categoryModel.setTitle("choose");
        categoryList.add(categoryModel);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    signUpModel.setCategory_id(-1);
                } else {
                    signUpModel.setCategory_id(categoryList.get(position).getId());

                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCategoryData();


        binding.imageSearch.setOnClickListener(view ->
        {
            String address = binding.edtAddress.getText().toString().trim();
            if (!address.isEmpty()) {
                Common.CloseKeyBoard(this, binding.edtAddress);
                binding.edtAddress.setError(null);
                Search(address);
            } else {
                binding.edtAddress.setError(getString(R.string.field_required));

            }
        });
        updateUI();
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    private void getCategoryData() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url).getcategories().enqueue(new Callback<AllCatogryModel>() {
            @Override
            public void onResponse(Call<AllCatogryModel> call, Response<AllCatogryModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        categoryList.clear();
                        categoryList.add(categoryModel);
                        categoryList.addAll(response.body().getData());
                        Log.e("data", categoryList.size() + "__");
                        SignUpActivity.this.runOnUiThread(() -> {
                            cateegoryAdapter.notifyDataSetChanged();
                        });
                    } else {
                        try {

                            Log.e("error", response.code() + "_" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (response.code() == 500) {
                            Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                        }
                    }


                }


            }

            @Override
            public void onFailure(Call<AllCatogryModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    dialog.dismiss();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(SignUpActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("phone_code") != null) {
                String phone_code = intent.getStringExtra("phone_code");
                String phone = intent.getStringExtra("phone");

                signUpModel.setPhone_code(phone_code);
                signUpModel.setPhone(phone);
                country_id = intent.getStringExtra("country_id");

            } else if (intent.getSerializableExtra("data") != null) {
                binding.btsignup.setText(getResources().getString(R.string.edit_profile));
            }
        }
    }

    @Override
    public void openSheet(int type) {
        this.typeimage = type;
        binding.expandLayout.setExpanded(true, true);
    }

    @Override
    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }


    @Override
    public void checkDataValid() {

        if (signUpModel.isDataValid(this)) {
            Common.CloseKeyBoard(this, binding.edtName);
            signUp();
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


    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    private void signUp() {
        if (uri == null) {
            signUpWithoutImage();
        } else {
            signUpWithImage();
        }
    }

    private void signUpWithoutImage() {
        Log.e("address sigUpWthoutIma:", signUpModel.getAddress());
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("category", signUpModel.getCategory_id() + "");
        List<String> list = new ArrayList<>();
        list.add(signUpModel.getCategory_id() + "");
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());
//        RequestBody account_number_part = Common.getRequestBodyText(signUpModel.getAccount_bank_number());
        RequestBody ipad_part = Common.getRequestBodyText(signUpModel.getIpad_number());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLatitude());
        RequestBody lon_part = Common.getRequestBodyText(signUpModel.getLongitude());
        RequestBody user_type_part = Common.getRequestBodyText("family");
        RequestBody software_type_part = Common.getRequestBodyText("android");
        RequestBody category_id_part = Common.getRequestBodyText(signUpModel.getCategory_id() + "");
        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        MultipartBody.Part bannner = Common.getMultiPart(this, uribanner, "banner");

        Api.getService(Tags.base_url)
                .signUpWithoutImage(name_part, email_part, phone_code_part, phone_part, address_part,bannner, user_type_part, software_type_part, ipad_part, lat_part, lon_part, category_id_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            preferences.create_update_session(SignUpActivity.this, Tags.session_login);
                            navigateToHomeActivity();
                        } else {
                            Log.e("nnnnnnnnnnnn", response.code() + "");
                            Log.e("555555", response.message());
                            try {
                                Log.e("2222222", response.errorBody().string() + "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }  else if (response.code() == 422) {
                                try {
                                    Log.e("msg_category_error", response.errorBody().string() + "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString() + "__");

                                }
                            }  else {
                                Log.e("ERROR", response.message() + "");

                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void signUpWithImage() {
        Log.e("address signUpWithImag:", signUpModel.getAddress());

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());
        RequestBody country_part = Common.getRequestBodyText(country_id);
        RequestBody ipad_part = Common.getRequestBodyText(signUpModel.getIpad_number());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLatitude());
        RequestBody lon_part = Common.getRequestBodyText(signUpModel.getLongitude());
        RequestBody user_type_part = Common.getRequestBodyText("family");
        RequestBody software_type_part = Common.getRequestBodyText("android");
        RequestBody category_id_part = Common.getRequestBodyText(signUpModel.getCategory_id() + "");
        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        MultipartBody.Part logo = Common.getMultiPart(this, uri, "logo");
        MultipartBody.Part bannner = Common.getMultiPart(this, uribanner, "banner");


        Api.getService(Tags.base_url)
                .signUpWithImage(name_part, email_part, phone_code_part, phone_part, address_part, logo,bannner,country_part, software_type_part, ipad_part, lat_part, lon_part, category_id_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            preferences.create_update_session(SignUpActivity.this, Tags.session_login);
                            navigateToHomeActivity();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }  else if (response.code() == 422) {
                                try {
                                    Log.e("msg_category_error422", response.errorBody().string() + "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString() + "__");

                                }
                            }  else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


    private void navigateToHomeActivity() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }


    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            CheckPermission();

            mMap.setOnMapClickListener(latLng -> {
                marker.setPosition(latLng);
                lat = latLng.latitude;
                lng = latLng.longitude;
                getGeoData(lat, lng);
            });


        }
    }

    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;
        signUpModel.setLatitude("" + lat);
        signUpModel.setLongitude("" + lng);
        binding.setModel(signUpModel);

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        } else {
            marker.setPosition(new LatLng(lat, lng));


        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


    }

    private void getGeoData(double lat, double lng) {


        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, "ar", getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                String address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                binding.edtAddress.setText(signUpModel.getAddress());
                                signUpModel.setLatitude("" + lat);
                                signUpModel.setLongitude("" + lng);
                                signUpModel.setAddress(address);
                                binding.setModel(signUpModel);
                                Log.e("addressaa", signUpModel.getAddress());


                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void Search(String query) {

        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, "ar", getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {
                                String address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                lat = response.body().getCandidates().get(0).getGeometry().getLocation().getLat();
                                lng = response.body().getCandidates().get(0).getGeometry().getLocation().getLng();
                                binding.edtAddress.setText(address);
                                signUpModel.setAddress(address);
                                signUpModel.setLatitude("" + lat);
                                signUpModel.setLongitude("" + lng);
                                binding.setModel(signUpModel);
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
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
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {


                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(SignUpActivity.this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        AddMarker(lat, lng);
        getGeoData(location.getLatitude(), location.getLongitude());

        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {
            if (typeimage == 0) {
                uri = data.getData();
                signUpModel.setLogo(uri.toString());
                File file = new File(Common.getImagePath(this, uri));
                Picasso.get().load(file).fit().into(binding.imgLogo);
            } else {
                uribanner = data.getData();
                signUpModel.setBanner(uribanner.toString());
                File file = new File(Common.getImagePath(this, uribanner));
                Picasso.get().load(file).fit().into(binding.imgBanner);
            }
        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (typeimage == 0) {
                uri = getUriFromBitmap(bitmap);
                if (uri != null) {
                    signUpModel.setLogo(uri.toString());
                    String path = Common.getImagePath(this, uri);
                    if (path != null) {
                        Picasso.get().load(new File(path)).fit().into(binding.imgLogo);
                    } else {
                        Picasso.get().load(uri).fit().into(binding.imgLogo);
                    }
                }
            } else {
                uribanner = getUriFromBitmap(bitmap);
                if (uribanner != null) {
                    signUpModel.setBanner(uribanner.toString());
                    String path = Common.getImagePath(this, uribanner);
                    if (path != null) {
                        Picasso.get().load(new File(path)).fit().into(binding.imgBanner);
                    } else {
                        Picasso.get().load(uri).fit().into(binding.imgBanner);
                    }
                }
            }
        } else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            startLocationUpdate();
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                navigateToHomeActivity();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

        if (locationRequest != null && locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);

        }

    }
}
