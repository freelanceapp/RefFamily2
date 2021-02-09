package com.apps.reffamily.services;


import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.models.AllCatogryModel;
import com.apps.reffamily.models.AllProdutsModel;
import com.apps.reffamily.models.AllSubCategoryModel;
import com.apps.reffamily.models.BalanceModel;
import com.apps.reffamily.models.CountryDataModel;
import com.apps.reffamily.models.FeedbackDataModel;
import com.apps.reffamily.models.PlaceGeocodeData;
import com.apps.reffamily.models.PlaceMapDetailsData;
import com.apps.reffamily.models.ProductModel;
import com.apps.reffamily.models.SettingModel;
import com.apps.reffamily.models.SingleSubCategoryModel;
import com.apps.reffamily.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    @GET("apiFamily/allBasicCategories")
    Call<AllCatogryModel> getcategories();

    @GET("apiFamily/allFamilyCategories")
    Call<AllSubCategoryModel> getcategories(
            @Header("Authorization") String user_token

    );


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
                                       @Part("id_country") RequestBody id_country,
                                       @Part("software_type") RequestBody software_type,
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

    @FormUrlEncoded
    @POST("apiFamily/productsInHome")
    Call<AllProdutsModel> getProducts(
            @Header("Authorization") String user_token,
            @Field("category_id") int category_id

    );

    @FormUrlEncoded
    @POST("apiFamily/getFamilyByPhone")
    Call<UserModel> getProfile(
            @Field("phone") String phone

    );

    @Multipart
    @POST("apiFamily/createNewFamilyProduct")
    Call<ProductModel> addProduct(@Header("Authorization") String user_token,
                                  @Part("title") RequestBody title,
                                  @Part("category_id") RequestBody category_id,
                                  @Part("price") RequestBody price,
                                  @Part("old_price") RequestBody old_price,
                                  @Part("offer_value") RequestBody offer_value,
                                  @Part("desc") RequestBody desc,
                                  @Part("have_offer") RequestBody have_offer,
                                  @Part("offer_type") RequestBody offer_type,
                                  @Part("offer_started_at") RequestBody offer_started_at,
                                  @Part("offer_finished_at") RequestBody offer_finished_at,
                                  @Part MultipartBody.Part main_image,
                                  @Part List<MultipartBody.Part> images
    );

    @Multipart
    @POST("apiFamily/editFamilyProduct")
    Call<ProductModel> updateProduct(@Header("Authorization") String user_token,
                                     @Part("product_id") RequestBody product_id,
                                     @Part("title") RequestBody title,
                                     @Part("category_id") RequestBody sub_category_id,
                                     @Part("price") RequestBody price,
                                     @Part("old_price") RequestBody old_price,
                                     @Part("offer_value") RequestBody offer_value,
                                     @Part("desc") RequestBody desc,
                                     @Part("have_offer") RequestBody have_offer,
                                     @Part("offer_type") RequestBody offer_type,
                                     @Part("offer_started_at") RequestBody offer_started_at,
                                     @Part("offer_finished_at") RequestBody offer_finished_at,
                                     @Part MultipartBody.Part main_image
    );

    @FormUrlEncoded
    @POST("apiFamily/editFamilyProduct")
    Call<ProductModel> updateProduct(@Header("Authorization") String user_token,
                                     @Field("product_id") int product_id,
                                     @Field("title") String title,
                                     @Field("category_id") int sub_category_id,
                                     @Field("price") double price,
                                     @Field("old_price") double old_price,
                                     @Field("offer_value") String offer_value,
                                     @Field("desc") String desc,
                                     @Field("have_offer") String have_offer,
                                     @Field("offer_type") String offer_type,
                                     @Field("offer_started_at") String offer_started_at,
                                     @Field("offer_finished_at") String offer_finished_at
    );

    @FormUrlEncoded
    @POST("apiFamily/deleteSingleFamilyProduct")
    Call<ResponseBody> deleteProduct(@Header("Authorization") String user_token,
                                     @Field("product_id") String product_id

    );

    @GET("apiFamily/offersTab")
    Call<AllProdutsModel> getOffers(
            @Header("Authorization") String user_token
            );

    @FormUrlEncoded
    @POST("api/update-product")
    Call<AddProductModel> updateOffer(@Header("Authorization") String user_token,
                                      @Field("id") int id,
                                      @Field("family_id") int family_id,
                                      @Field("old_price") String old_price,
                                      @Field("offer_value") String offer_value,
                                      @Field("have_offer") String have_offer
    );


    @FormUrlEncoded
    @POST("api/delete-offer")
    Call<ResponseBody> deleteOffer(@Header("Authorization") String user_token,
                                   @Field("product_id") String product_id

    );
    @FormUrlEncoded
    @POST("apiFamily/createNewFamilyCategory")
    Call<SingleSubCategoryModel> AddSubCategoryWithoutImage(@Header("Authorization") String user_token,
                                                            @Field("title") String title,
                                                            @Field("desc") String desc



    );

    @Multipart
    @POST("apiFamily/createNewFamilyCategory")
    Call<SingleSubCategoryModel> AddSubCategoryWithImage(@Header("Authorization") String user_token,
                                                   @Part("title") RequestBody title,
                                                   @Part("desc") RequestBody desc,
                                                   @Part MultipartBody.Part image
    );
    @FormUrlEncoded
    @POST("apiFamily/updateReceiveNotification")
    Call<UserModel> updateStatus(@Header("Authorization") String user_token,
                                    @Field("receive_notifications") String receive_notifications

    );
    @GET("api/get-comments")
    Call<FeedbackDataModel> getFeedback(@Header("Authorization") String user_token,
                                        @Query(value = "user_type") String user_type,
                                        @Query(value = "user_id") int user_id,
                                        @Query(value = "page") int page,
                                        @Query(value = "pagination") String pagination,
                                        @Query(value = "limit_per_page") int limit_per_page);
    @GET("api/get-user-balance")
    Call<BalanceModel> getUserBalance(@Header("Authorization") String user_token,
                                      @Query("user_id") int user_id);
    @GET("api/sttings")
    Call<SettingModel> getSetting(@Query(value = "lang") String lang);



}