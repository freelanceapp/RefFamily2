package com.apps.reffamily.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.adapters.OfferAdapter;
import com.apps.reffamily.databinding.DialogUpdatePriceBinding;
import com.apps.reffamily.databinding.FragmentOffersBinding;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.models.AllProdutsModel;
import com.apps.reffamily.models.SingleProductModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment implements DatePickerDialog.OnDateSetListener {

    private HomeActivity activity;
    private FragmentOffersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private OfferAdapter offerAdapter;
    private List<SingleProductModel> offerlist;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar1;
    private String date;
    private int date_type = -1;
    private AddProductModel.Data addProductModel;

    public static Fragment_Offers newInstance() {
        return new Fragment_Offers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(getActivity());
        Paper.init(activity);
        offerlist = new ArrayList<>();
        offerAdapter = new OfferAdapter(offerlist, activity, this);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.recViewOffers.setAdapter(offerAdapter);
        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getOffers();

    }

    private void getOffers() {
        offerlist.clear();
        offerAdapter.notifyDataSetChanged();
        binding.progBarOffer.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).getOffers("Bearer " + userModel.getData().getToken()).enqueue(new Callback<AllProdutsModel>() {
            @Override
            public void onResponse(Call<AllProdutsModel> call, Response<AllProdutsModel> response) {
                binding.progBarOffer.setVisibility(View.GONE);
//                Log.e("Offer Size:", "  " + response.body().getData().size());
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getStatus()==200){
                    offerlist.addAll(response.body().getData());
                    offerAdapter.notifyDataSetChanged();
                    if (offerlist.size() == 0) {
                        binding.linearNoData.setVisibility(View.VISIBLE);
                    } else {
                        binding.linearNoData.setVisibility(View.GONE);
                    }}
                    else {
                      if (offerlist.size() == 0) {
                        binding.linearNoData.setVisibility(View.VISIBLE);
                    } else {
                        binding.linearNoData.setVisibility(View.GONE);
                    }
                    }
                }
            }

            @Override
            public void onFailure(Call<AllProdutsModel> call, Throwable t) {
                Log.e("Offer Fragment Error:", t.getMessage());
                binding.progBarOffer.setVisibility(View.GONE);
            }
        });


    }

    public void delete(SingleProductModel data, int position) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getResources().getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url).deleteOffer("Bearer " + userModel.getData().getToken(), data.getId() + "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            offerlist.remove(position);
                            offerAdapter.notifyItemRemoved(position);
                            offerAdapter.notifyDataSetChanged();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("error", t.getMessage());
                    }
                });

    }

    public void offer(SingleProductModel data) {

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();
        DialogUpdatePriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_update_price, null, false);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_bg_white_1);
        dialog.setView(binding.getRoot());
        dialog.show();
        dialog.setCancelable(true);
        createDatePickerDialog();
        SingleProductModel productModel = data;
        addProductModel = new AddProductModel.Data();
        addProductModel.setId(productModel.getId());
        addProductModel.setTitle(productModel.getTitle());
        addProductModel.setDesc(productModel.getDesc());
        addProductModel.setOffer_finished_at(productModel.getOffer_finished_at().substring(0, productModel.getOffer_finished_at().indexOf(" ")));
        addProductModel.setOffer_started_at(productModel.getOffer_started_at().substring(0, productModel.getOffer_started_at().indexOf(" ")));
        List<Uri> uriList = new ArrayList<>();
        if (productModel.getProduct_images().size() > 0) {
            for (int i = 0; i < productModel.getProduct_images().size(); i++) {
                uriList.add(Uri.parse(productModel.getProduct_images().get(i).getImage()));
            }
        }
        addProductModel.setImages(uriList);
        Log.e("images: ", "" + productModel.getProduct_images().size());
        addProductModel.setFamily_id(productModel.getFamily_id());
        addProductModel.setSub_category_id(productModel.getCategory_id());
        addProductModel.setMain_image(productModel.getMain_image());
        addProductModel.setHave_offer(productModel.getHave_offer());
        addProductModel.setOffer_type(productModel.getOffer_type());
        addProductModel.setOffer_value(productModel.getOffer_value() + "");
        addProductModel.setRating_value(productModel.getRating_value());
        addProductModel.setPrice(productModel.getPrice());
        addProductModel.setOld_price(productModel.getOld_price() + "");
        binding.setModel(addProductModel);

        Log.e("with_offer: ", "with_offer");
        binding.confirmBtn.setOnClickListener(view1 -> {
            Log.e("click: ", "update");
            Api.getService(Tags.base_url).updateOffer("Bearer " + userModel.getData().getToken(), addProductModel.getId(), addProductModel.getFamily_id(),
                    addProductModel.getOld_price(), addProductModel.getOffer_value(), addProductModel.getHave_offer()).enqueue(new Callback<AddProductModel>() {
                @Override
                public void onResponse(Call<AddProductModel> call, Response<AddProductModel> response) {

                    if (response.isSuccessful()) {
                        getOffers();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<AddProductModel> call, Throwable t) {

                }
            });
        });
        binding.linearDateFrom.setOnClickListener(view -> {
            date_type = 0;
            datePickerDialog.show(activity.getFragmentManager(), "");
        });
        binding.linearDateTo.setOnClickListener(view -> {
            date_type = 1;
            datePickerDialog.show(activity.getFragmentManager(), "");
        });

    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(lang));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);
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
//            if (date_type == 0) {
//                binding.dateFromValue.setText(date);
//                addProductModel.setOffer_started_at(date);
//                Log.d("OfferAdapter", "onDateSet from:" + addProductModel.getOffer_started_at());
//            } else {
//                binding.dateToValue.setText(date);
//                addProductModel.setOffer_finished_at(date);
//                Log.d("OfferAdapter", "onDateSet to:" + addProductModel.getOffer_finished_at());
//            }
    }
}