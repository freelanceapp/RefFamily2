package com.apps.reffamily.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.HomeActivity;
import com.apps.reffamily.activities_fragments.activity_sign_up.SignUpActivity;
import com.apps.reffamily.adapters.CategoryAdapter;
import com.apps.reffamily.adapters.ProductsAdapter;
import com.apps.reffamily.databinding.FragmentMainBinding;
import com.apps.reffamily.models.AllCatogryModel;
import com.apps.reffamily.models.AllProdutsModel;
import com.apps.reffamily.models.SingleCategoryModel;
import com.apps.reffamily.models.SingleProductModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;
import com.apps.reffamily.remote.Api;
import com.apps.reffamily.share.Common;
import com.apps.reffamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private String lang;
    private UserModel user;
    private CategoryAdapter categoryAdapter;
    private ProductsAdapter homeProductAdapter;
    private List<SingleCategoryModel> categoryList;
    private List<SingleProductModel> productList;
    private UserModel userModel;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();
        getProfile();
        return binding.getRoot();
    }


    private void initView() {

        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        user = preferences.getUserData(activity);
        categoryAdapter = new CategoryAdapter(categoryList, activity, this);
        homeProductAdapter = new ProductsAdapter(productList, activity, this);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        binding.recViewCategory.setAdapter(categoryAdapter);
        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewOffers.setAdapter(homeProductAdapter);

        binding.addBtn.setOnClickListener(View -> {

            NavigateToAddProductActivity();
        });

        binding.btnBuy.setOnClickListener(view -> {
//            Intent intent = new Intent(activity, SubscriptionActivity.class);
//            startActivityForResult(intent, 1);
        });

    }

    private void getProfile() {

        Api.getService(Tags.base_url).getProfile(userModel.getData().getPhone())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            ChangeStauts(response.body());
                            // binding.setModel(userModel);
                        } else {


                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                try {
                                    Log.e("errorCode:", response.code() + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.e("error Profile", t.getMessage());
                    }
                });
    }

    private void getCategoryData() {

        binding.progBarCategory.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getcategories("Bearer " + userModel.getData().getToken()).enqueue(new Callback<AllCatogryModel>() {
            @Override
            public void onResponse(Call<AllCatogryModel> call, Response<AllCatogryModel> response) {
                binding.progBarCategory.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        categoryList.clear();
                        categoryList.addAll(response.body().getData());
                        categoryAdapter.notifyDataSetChanged();
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllCatogryModel> call, Throwable t) {
                binding.progBarCategory.setVisibility(View.GONE);
                try {
                    binding.progBarCategory.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }


    private void NavigateToAddProductActivity() {

//        Intent intent = new Intent(this.getContext(), AddProductActivity.class);
//        startActivity(intent);

    }

    public void ChangeStauts(UserModel userModel) {
        if (userModel != null) {
            if (userModel.getData().getNumber_of_payment_days() == 0) {
                binding.llsubscribe.setVisibility(View.VISIBLE);
                binding.recViewCategory.setVisibility(View.GONE);
                binding.recViewOffers.setVisibility(View.GONE);
                binding.linearNoData.setVisibility(View.GONE);
            } else {
                binding.llsubscribe.setVisibility(View.GONE);
                binding.recViewCategory.setVisibility(View.VISIBLE);
                binding.recViewOffers.setVisibility(View.VISIBLE);
                getCategoryData();

            }
        }

    }


    public void showproduct(int position) {
        binding.progBarOffers.setVisibility(View.VISIBLE);
        binding.linearNoData.setVisibility(View.GONE);

        Api.getService(Tags.base_url).getProducts("Bearer " + userModel.getData().getToken(), categoryList.get(position).getId()).enqueue(new Callback<AllProdutsModel>() {
            @Override
            public void onResponse(Call<AllProdutsModel> call, Response<AllProdutsModel> response) {
                binding.progBarOffers.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        productList.clear();
                        productList.addAll(response.body().getData());
                        homeProductAdapter.notifyDataSetChanged();
                    } else {
                        binding.linearNoData.setVisibility(View.VISIBLE);
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllProdutsModel> call, Throwable t) {
                binding.progBarOffers.setVisibility(View.GONE);
                try {
                    binding.progBarOffers.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });

    }
}
