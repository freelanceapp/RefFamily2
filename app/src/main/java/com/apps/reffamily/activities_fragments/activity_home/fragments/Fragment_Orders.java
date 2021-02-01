package com.apps.reffamily.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.databinding.FragmentOrdersBinding;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Orders extends Fragment {

    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
//    private OrderAdapter orderAdapter;
//    private List<OrderModel.Data> orderList;

    public static Fragment_Orders newInstance() {
        return new Fragment_Orders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
       // initView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

//    private void initView() {
//        activity = (HomeActivity) getActivity();
//        preferences = Preferences.newInstance();
//        userModel = preferences.getUserData(getActivity());
//        orderList = new ArrayList<>();
//        orderAdapter = new OrderAdapter(orderList, getActivity());
//        Paper.init(activity);
//        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
//        binding.setLang(lang);
//        binding.recViewOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));
//
//        binding.recViewOrders.setAdapter(orderAdapter);
//
//
//        getOrders();
//    }
//
//    private void getOrders() {
//        orderList.clear();
//        orderAdapter.notifyDataSetChanged();
//        binding.progBarOrders.setVisibility(View.VISIBLE);
//        Log.e("klfkfkfk", "Bearer " + userModel.getData().getToken() +
//                userModel.getData().getId() + "family" + "family" + "current");
//        Api.getService(Tags.base_url).getOrderByStatus("Bearer " + userModel.getData().getToken(),
//                userModel.getData().getId(), "family", "family", "current").enqueue(new Callback<OrderModel>() {
//            @Override
//            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
//                Log.e("kdkdkk", response.code() + "  " + response.body().getData().size() + "");
//                binding.progBarOrders.setVisibility(View.GONE);
//                if (response.isSuccessful() && response.body() != null) {
//                    orderList.addAll(response.body().getData());
//                    orderAdapter.notifyDataSetChanged();
//                }
//                if (orderList.size() == 0) {
//                    binding.linearNoData.setVisibility(View.VISIBLE);
//                } else {
//                    binding.linearNoData.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OrderModel> call, Throwable t) {
//                binding.progBarOrders.setVisibility(View.GONE);
//                Log.e("Fragment_Orders: ", t.getMessage());
//            }
//        });
//
//
//    }


}
