package com.apps.reffamily.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;


import com.apps.reffamily.BR;
import com.apps.reffamily.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddProductModel extends BaseObservable implements Serializable {

    public static class Data extends BaseObservable implements Serializable {

        private List<Uri> images;
        private int id;
        private int family_id;
        private int sub_category_id;
        private double price;
        private String old_price;
        private String offer_value;
        private double rating_value;
        private String title;
        private String desc;
        private String main_image;
        private String have_offer;
        private String offer_type;
        private String offer_started_at;
        private String offer_finished_at;
        public ObservableField<String> error_title = new ObservableField<>();
        public ObservableField<String> error_desc = new ObservableField<>();
        public ObservableField<String> error_price = new ObservableField<>();
        public ObservableField<String> error_old_price = new ObservableField<>();
        public ObservableField<String> error_offer_value = new ObservableField<>();
        public ObservableField<String> error_offer_started_at = new ObservableField<>();
        public ObservableField<String> error_offer_finished_at = new ObservableField<>();

        public Data() {
            title = "";
            desc = "";
            have_offer = "without_offer";
            offer_type = "";
            main_image = "";
            offer_finished_at = "";
            offer_started_at = "";
            this.old_price = "";
            offer_value = "";
            images = new ArrayList<>();
        }

        public boolean step1(Context context) {


            if (!title.trim().isEmpty() && !desc.trim().isEmpty() && !main_image.isEmpty()) {
                error_title.set(null);
                error_desc.set(null);
                return true;
            } else {
                if (title.trim().isEmpty()) {
                    error_title.set(context.getString(R.string.field_required));
                }

                if (desc.trim().isEmpty()) {
                    error_desc.set(context.getString(R.string.field_required));
                }
                if (main_image.isEmpty()) {
                    Toast.makeText(context, context.getResources().getString(R.string.main_image_required), Toast.LENGTH_SHORT).show();
                }
                return false;
            }


        }


        public boolean step2(Context context) {


            if (!old_price.trim().isEmpty() && sub_category_id != -1 && ((have_offer.equals("with_offer") && !offer_started_at.equals("") && !offer_finished_at.equals("")) || have_offer.equals("without_offer"))) {
                error_old_price.set(null);
                return true;
            } else {
                if (old_price.trim().isEmpty()) {
                    error_old_price.set(context.getString(R.string.field_required));
                }

                if (have_offer.equals("with_offer") && offer_value.equals("")) {
                    error_offer_value.set(context.getString(R.string.field_required));
                }
                if (have_offer.equals("with_offer") && offer_started_at.equals("")) {
                    error_offer_started_at.set(context.getString(R.string.field_required));
                }
                if (have_offer.equals("with_offer") && offer_finished_at.equals("")) {
                    error_offer_finished_at.set(context.getString(R.string.field_required));
                }
                if (sub_category_id == -1) {
                    Toast.makeText(context, "Category Required", Toast.LENGTH_SHORT).show();
                }
                return false;
            }


        }


        public List<Uri> getImages() {
            return images;
        }

        public void setImages(List<Uri> images) {
            this.images = images;
        }

        @Bindable
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
            notifyPropertyChanged(BR.title);
        }

        @Bindable
        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
            notifyPropertyChanged(BR.desc);
        }

        @Bindable
        public int getSub_category_id() {
            return sub_category_id;
        }

        public void setSub_category_id(int sub_category_id) {
            this.sub_category_id = sub_category_id;
            notifyPropertyChanged(BR.sub_category_id);
        }

        @Bindable
        public int getFamily_id() {
            return family_id;
        }

        public void setFamily_id(int family_id) {
            this.family_id = family_id;
            notifyPropertyChanged(BR.family_id);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Bindable
        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
            notifyPropertyChanged(BR.price);


        }

        @Bindable
        public String getOld_price() {
            return old_price;
        }

        public void setOld_price(String old_price) {
            this.old_price = old_price;
            notifyPropertyChanged(BR.old_price);
        }

        @Bindable
        public String getOffer_value() {
            return offer_value;
        }

        public void setOffer_value(String offer_value) {
            this.offer_value = offer_value;
            notifyPropertyChanged(BR.offer_value);

        }

        @Bindable
        public String getOffer_type() {
            return offer_type;
        }

        public void setOffer_type(String offer_type) {
            this.offer_type = offer_type;
            notifyPropertyChanged(BR.offer_type);
        }

        @Bindable
        public double getRating_value() {
            return rating_value;
        }

        public void setRating_value(double rating_value) {
            this.rating_value = rating_value;
            notifyPropertyChanged(BR.rating_value);

        }

        public String getMain_image() {
            return main_image;
        }

        public void setMain_image(String main_image) {
            this.main_image = main_image;
        }

        public String getHave_offer() {
            return have_offer;
        }

        public void setHave_offer(String have_offer) {
            this.have_offer = have_offer;
        }

        public void setOffer_started_at(String offer_started_at) {
            this.offer_started_at = offer_started_at;
        }

        public void setOffer_finished_at(String offer_finished_at) {
            this.offer_finished_at = offer_finished_at;
        }

        public String getOffer_started_at() {
            return offer_started_at;
        }

        public String getOffer_finished_at() {
            return offer_finished_at;
        }
    }

}