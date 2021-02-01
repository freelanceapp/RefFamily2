package com.apps.reffamily.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.databinding.FragmentOffersBinding;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment implements DatePickerDialog.OnDateSetListener {

    private HomeActivity activity;
    private FragmentOffersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
//    private OfferAdapter offerAdapter;
//    private List<OfferModel.Data> offerlist;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar1;
    private String date;
    private int date_type = -1;

    public static Fragment_Offers newInstance() {
        return new Fragment_Offers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
      //  initView();
        return binding.getRoot();
    }


//    private void initView() {
//        activity = (HomeActivity) getActivity();
//        preferences = Preferences.newInstance();
//        userModel = preferences.getUserData(getActivity());
//        Paper.init(activity);
//        offerlist = new ArrayList<>();
//        offerAdapter = new OfferAdapter(offerlist, activity,this);
//        lang = Language_Helper.getLanguage(this.getContext());
//        binding.setLang(lang);
//        binding.recViewOffers.setAdapter(offerAdapter);
//        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this.getContext()));
//
//        getOffers();
//
//    }
//
//    private void getOffers() {
//        offerlist.clear();
//        offerAdapter.notifyDataSetChanged();
//        binding.progBarOffer.setVisibility(View.VISIBLE);
//        Api.getService(Tags.base_url).getOffers(userModel.getData().getId()).enqueue(new Callback<OfferModel>() {
//            @Override
//            public void onResponse(Call<OfferModel> call, Response<OfferModel> response) {
//                binding.progBarOffer.setVisibility(View.GONE);
//                Log.e("Offer Size:", "  " + response.body().getData().size());
//                if (response.isSuccessful() && response.body() != null) {
//                    offerlist.addAll(response.body().getData());
//                    offerAdapter.notifyDataSetChanged();
//                    if (offerlist.size() == 0) {
//                        binding.linearNoData.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.linearNoData.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OfferModel> call, Throwable t) {
//                Log.e("Offer Fragment Error:", t.getMessage());
//                binding.progBarOffer.setVisibility(View.GONE);
//            }
//        });
//
//
//    }
//    public void offer(OfferModel.Data data) {
//
//        AlertDialog dialog =  new AlertDialog.Builder(activity)
//                .create();
//        DialogUpdatePriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_update_price, null, false);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_bg_white_1);
//        dialog.setView(binding.getRoot());
//        dialog.show();
//        dialog.setCancelable(true);
//        createDatePickerDialog();
//        OfferModel.Data productModel = data;
//        addProductModel = new AddProductModel.Data();
//        addProductModel.setId(productModel.getId());
//        addProductModel.setTitle(productModel.getTitle());
//        addProductModel.setDesc(productModel.getDesc());
//        addProductModel.setOffer_finished_at(productModel.getOffer_finished_at().substring(0,productModel.getOffer_finished_at().indexOf(" ")));
//        addProductModel.setOffer_started_at(productModel.getOffer_started_at().substring(0,productModel.getOffer_started_at().indexOf(" ")));
//        addProductModel.setImages(productModel.getImages());
//        Log.e("images: ", "" + productModel.getImages().size());
//        addProductModel.setFamily_id(productModel.getFamily_id());
//        addProductModel.setSub_category_id(productModel.getSub_category_id());
//        addProductModel.setMain_image(productModel.getMain_image());
//        addProductModel.setHave_offer(productModel.getHave_offer());
//        addProductModel.setOffer_type(productModel.getOffer_type());
//        addProductModel.setOffer_value(productModel.getOffer_value()+"");
//        addProductModel.setRating_value(productModel.getRating_value());
//        addProductModel.setPrice(productModel.getPrice());
//        addProductModel.setOld_price(productModel.getOld_price()+"");
//        binding.setModel(addProductModel);
//
//        Log.e("with_offer: ", "with_offer");
//        binding.confirmBtn.setOnClickListener(view1 -> {
//            Log.e("click: ", "update");
//                Api.getService(Tags.base_url).updateOffer("Bearer "+userModel.getData().getToken(),addProductModel.getId(),addProductModel.getFamily_id(),
//                        addProductModel.getOld_price(),addProductModel.getOffer_value(),addProductModel.getHave_offer()).enqueue(new Callback<AddProductModel>() {
//                    @Override
//                    public void onResponse(Call<AddProductModel> call, Response<AddProductModel> response) {
//
//if(response.isSuccessful()){
//    getOffers();
//    dialog.dismiss();
//}
//                    }
//
//                    @Override
//                    public void onFailure(Call<AddProductModel> call, Throwable t) {
//
//                    }
//                });});
//            binding.linearDateFrom.setOnClickListener(view -> {
//                date_type = 0;
//                datePickerDialog.show(activity.getFragmentManager(), "");
//            });
//            binding.linearDateTo.setOnClickListener(view -> {
//                date_type = 1;
//                datePickerDialog.show(activity.getFragmentManager(), "");
//            });
//
//}
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