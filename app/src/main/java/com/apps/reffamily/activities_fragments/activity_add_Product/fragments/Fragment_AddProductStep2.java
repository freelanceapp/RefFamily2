package com.apps.reffamily.activities_fragments.activity_add_Product.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.apps.reffamily.activities_fragments.activity_sign_up.SignUpActivity;
import com.apps.reffamily.adapters.SpinnerCategoryAdapter;
import com.apps.reffamily.adapters.SpinnerSubCategoryAdapter;
import com.apps.reffamily.databinding.FragmentAddProductStep2Binding;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.models.AllCatogryModel;
import com.apps.reffamily.models.AllSubCategoryModel;
import com.apps.reffamily.models.SingleCategoryModel;
import com.apps.reffamily.models.SingleSubCategoryModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_AddProductStep2 extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DATA";
    private FragmentAddProductStep2Binding binding;
    public AddProductModel.Data addProductModel = null;
    private SingleSubCategoryModel categoryModel;
    private List<SingleSubCategoryModel> categoryList = new ArrayList<>();
    private SpinnerSubCategoryAdapter spinnerCategoryAdapter;
    private Preferences preferences;
    private DatePickerDialog datePickerDialog;
    private String lang;
    private Calendar calendar1;
    private String date;
    private int date_type = -1; // if 0 date from  ,,,, if 1 date to
    private UserModel userModel;
    private AddProductActivity addProductActivity;

    public static Fragment_AddProductStep2 newInstance(AddProductModel.Data addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, addProductModel);
        Fragment_AddProductStep2 fragment_addProductStep2 = new Fragment_AddProductStep2();
        fragment_addProductStep2.setArguments(bundle);
        return fragment_addProductStep2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_product_step2, container, false);
        initView();
        getData();

        createDatePickerDialog();
        return binding.getRoot();
    }

    private void getData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            addProductModel = (AddProductModel.Data) bundle.getSerializable(TAG);

            binding.etOfferPrice.setText(addProductModel.getPrice() + "");
            binding.etDiscountPercentage.setText(addProductModel.getOffer_value() + "");
            binding.dateFromValue.setText(addProductModel.getOffer_started_at());
            binding.dateToValue.setText(addProductModel.getOffer_finished_at());

            if (addProductModel.getHave_offer().equals("with_offer")) {
                binding.foundNotfoundLayout.setVisibility(View.VISIBLE);
                binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
                binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
                binding.foundNotfoundLayout.setVisibility(View.VISIBLE);

                if (addProductModel.getOffer_type().equals("per")) {
                    binding.per.setChecked(true);
                    binding.value.setChecked(false);
                } else {
                    binding.per.setChecked(false);
                    binding.value.setChecked(true);
                }
            } else {
                binding.foundNotfoundLayout.setVisibility(View.GONE);
                binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
                binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
                binding.foundNotfoundLayout.setVisibility(View.GONE);
            }
        }
        binding.setModel(addProductModel);
    }


    private void initView() {
        addProductActivity = (AddProductActivity) getActivity();
        Paper.init(this.getContext());
        lang = Paper.book().read("lang", "ar");


        binding.foundNotfoundLayout.setVisibility(View.GONE);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(getActivity());
        binding.setModel(addProductModel);
//        addProductModel.setFamily_id(userModel.getData().getId());


        binding.per.setOnCheckedChangeListener((compoundButton, b) -> {
            addProductModel.setOffer_type("per");
        });


        binding.value.setOnCheckedChangeListener((compoundButton, b) -> {
            addProductModel.setOffer_type("value");
        });

        binding.foundBtn.setOnClickListener(view -> {
            addProductModel.setHave_offer("with_offer");
            binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
            binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
            binding.foundNotfoundLayout.setVisibility(View.VISIBLE);
            addProductModel.setOffer_type("per");
            binding.per.setChecked(true);
            binding.setModel(addProductModel);

        });

        binding.notFoundBtn.setOnClickListener(view -> {
            addProductModel.setHave_offer("without_offer");
            binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
            binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
            binding.foundNotfoundLayout.setVisibility(View.GONE);
            addProductModel.setOffer_type("");
            addProductModel.setOffer_started_at("");
            addProductModel.setOffer_finished_at("");
            binding.dateFromValue.setText(addProductModel.getOffer_started_at());
            binding.dateToValue.setText(addProductModel.getOffer_finished_at());
            binding.per.setChecked(false);
            binding.value.setChecked(false);
            binding.setModel(addProductModel);

        });

        binding.linearDateFrom.setOnClickListener(view -> {
            date_type = 0;
            datePickerDialog.show(getActivity().getFragmentManager(), "");
        });
        binding.linearDateTo.setOnClickListener(view -> {
            date_type = 1;
            datePickerDialog.show(getActivity().getFragmentManager(), "");
        });

        spinnerCategoryAdapter = new SpinnerSubCategoryAdapter(categoryList, addProductActivity);
        binding.spinnerCategory.setAdapter(spinnerCategoryAdapter);

        categoryModel = new SingleSubCategoryModel();
        categoryModel.setId(0);
        categoryModel.setTitle("Choose");
        categoryList.add(categoryModel);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    addProductModel.setSub_category_id(-1);
                } else {
                    addProductModel.setSub_category_id(categoryList.get(position).getId());

                }
                binding.setModel(addProductModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCategoryData();



    }


    private void getCategoryData() {

        ProgressDialog dialog = Common.createProgressDialog(addProductActivity, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url).getcategories("Bearer " + userModel.getData().getToken()).enqueue(new Callback<AllSubCategoryModel>() {
            @Override
            public void onResponse(Call<AllSubCategoryModel> call, Response<AllSubCategoryModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        categoryList.clear();
                        categoryList.add(categoryModel);
                        categoryList.addAll(response.body().getData());
                        Log.e("data", categoryList.size() + "__");
                        addProductActivity.runOnUiThread(() -> {
                            spinnerCategoryAdapter.notifyDataSetChanged();
                            getCategorypos();
                        });
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(addProductActivity, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(addProductActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllSubCategoryModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    dialog.dismiss();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(addProductActivity, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(addProductActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private void getCategorypos() {
        if (addProductActivity.type == 2) {
            int pos = -1;
            for (int i = 1; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == addProductModel.getSub_category_id()) {
                    pos = i;
                    break;
                }
            }
            binding.spinnerCategory.setSelection(pos);
        }
    }


    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(getContext(), R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(getContext(), R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(getContext(), R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(lang));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);
        Log.d(TAG, "onDateSet from:" + addProductModel.getOffer_started_at());


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar1 = calendar;

        // order_time_calender.set(Calendar.YEAR,year);
        //order_time_calender.set(Calendar.MONTH,monthOfYear);
        //order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        //Log.e("kkkk", calendar.getTime().getMonth() + "");

        date = year + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        if (date_type == 0) {
            binding.dateFromValue.setText(date);
            addProductModel.setOffer_started_at(date);
            Date currentTime = Calendar.getInstance().getTime();
            Log.d(TAG, "onDateSet from:" + addProductModel.getOffer_started_at());
            Log.d(TAG, "onDateSet from Time:" + currentTime);
        } else {
            binding.dateToValue.setText(date);
            addProductModel.setOffer_finished_at(date);
            Log.d(TAG, "onDateSet to:" + addProductModel.getOffer_finished_at());
        }
    }
}