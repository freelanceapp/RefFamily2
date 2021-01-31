package com.reffamily.services;


import com.reffamily.models.AllCatogryModel;
import com.reffamily.models.CountryDataModel;
import com.reffamily.models.PlaceGeocodeData;
import com.reffamily.models.PlaceMapDetailsData;
import com.reffamily.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("api/countries")
    Call<CountryDataModel> getCountries(@Query(value = "lang") String lang);
    @FormUrlEncoded
    @POST("apiFamily/familyLogin")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );

    @GET("apiFamily/allCategories")
    Call<AllCatogryModel> getcategories();
    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );
    @Multipart
    @POST("apiFamily/familyRegister")
    Call<UserModel> signUpWithoutImage(@Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("phone_code") RequestBody phone_code,
                                       @Part("phone") RequestBody phone,
                                       @Part("address") RequestBody address,
                                       @Part MultipartBody.Part banner,
                                       @Part("software_type") RequestBody software_type,
                                       @Part("id_country") RequestBody id_country,
                                       @Part("iban_number") RequestBody ipad_number,
                                       @Part("latitude") RequestBody iban_number,
                                       @Part("longitude") RequestBody longitude,
                                       @Part("basic_category_id") RequestBody basic_category_id


    );

    @Multipart
    @POST("apiFamily/familyRegister")
    Call<UserModel> signUpWithImage(@Part("name") RequestBody name,
                                    @Part("email") RequestBody email,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
                                    @Part("address") RequestBody address,
                                    @Part MultipartBody.Part logo,
                                    @Part MultipartBody.Part banner,
                                    @Part("id_country") RequestBody id_country,
                                    @Part("software_type") RequestBody software_type,
                                    @Part("iban_number") RequestBody ipad_number,
                                    @Part("latitude") RequestBody iban_number,
                                    @Part("longitude") RequestBody longitude,
                                    @Part("basic_category_id") RequestBody basic_category_id
    );



    @FormUrlEncoded
    @POST("api/update-profile")
    Call<UserModel> updateProfileWithoutImage(@Header("Authorization") String user_token,
                                              @Field("id") int id,
                                              @Field("name") String name,
                                              @Field("email") String email,
                                              @Field("phone_code") String phone_code,
                                              @Field("phone") String phone,
                                              @Field("address") String address,
                                              @Field("user_type") String user_type,
                                              @Field("software_type") String software_type,
                                              @Field("account_bank_number") String account_bank_number,
                                              @Field("ipad_number") String ipad_number,
                                              @Field("latitude") String latitude,
                                              @Field("longitude") String longitude,
                                              @Field("category_id[]") List<String> category_id


    );

    @Multipart
    @POST("api/update-profile")
    Call<UserModel> updateProfileWithImage(@Header("Authorization") String user_token,
                                           @Part("id") RequestBody id,
                                           @Part("name") RequestBody name,
                                           @Part("email") RequestBody email,
                                           @Part("phone_code") RequestBody phone_code,
                                           @Part("phone") RequestBody phone,
                                           @Part("address") RequestBody address,
                                           @Part MultipartBody.Part logo,
                                           @Part("user_type") RequestBody user_type,
                                           @Part("software_type") RequestBody software_type,
                                           @Part("account_bank_number") RequestBody account_bank_number,
                                           @Part("ipad_number") RequestBody ipad_number,
                                           @Part("latitude") RequestBody latitude,
                                           @Part("longitude") RequestBody longitude,
                                           @Part("category_id[]") List<RequestBody> category_id
    );

    @Multipart
    @POST("api/update-profile")
    Call<UserModel> updateProfileWithImage(@Header("Authorization") String user_token,
                                           @Part("id") RequestBody id,
                                           @Part("name") RequestBody name,
                                           @Part("email") RequestBody email,
                                           @Part("phone_code") RequestBody phone_code,
                                           @Part("phone") RequestBody phone,
                                           @Part("address") RequestBody address,
                                           @Part MultipartBody.Part logo,
                                           @Part MultipartBody.Part bannner,

                                           @Part("user_type") RequestBody user_type,
                                           @Part("software_type") RequestBody software_type,
                                           @Part("account_bank_number") RequestBody account_bank_number,
                                           @Part("ipad_number") RequestBody ipad_number,
                                           @Part("latitude") RequestBody latitude,
                                           @Part("longitude") RequestBody longitude,
                                           @Part("category_id[]") List<RequestBody> category_id
    );
    @Multipart
    @POST("api/update-profile")
    Call<UserModel> updateProfileWithImagewithoutlogo(@Header("Authorization") String user_token,
                                                      @Part("id") RequestBody id,
                                                      @Part("name") RequestBody name,
                                                      @Part("email") RequestBody email,
                                                      @Part("phone_code") RequestBody phone_code,
                                                      @Part("phone") RequestBody phone,
                                                      @Part("address") RequestBody address,
                                                      @Part MultipartBody.Part bannner,

                                                      @Part("user_type") RequestBody user_type,
                                                      @Part("software_type") RequestBody software_type,
                                                      @Part("account_bank_number") RequestBody account_bank_number,
                                                      @Part("ipad_number") RequestBody ipad_number,
                                                      @Part("latitude") RequestBody latitude,
                                                      @Part("longitude") RequestBody longitude,
                                                      @Part("category_id[]") List<RequestBody> category_id
    );
}