package com.apps.reffamily.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.apps.reffamily.BR;
import com.apps.reffamily.R;


public class SignUpModel extends BaseObservable {
    private String logo;
    private String banner;

    private String name;
    private String email;
    private String address;
    private String phone_code;
    private String phone;
//    private String account_bank_number;
    private String ipad_number;
    private String software_type;
    private String user_type;
    private int category_id;
    private String latitude;
    private String longitude;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
//    public ObservableField<String> error_account_bank_number = new ObservableField<>();
    public ObservableField<String> error_ipad_number = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() &&
                !email.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                !ipad_number.trim().isEmpty() &&
                ipad_number.length() == 22 &&
//                !account_bank_number.trim().isEmpty() &&
                !address.isEmpty() &&
                category_id != 0
                && !banner.trim().isEmpty()

        ) {
            error_name.set(null);
            error_email.set(null);
            error_ipad_number.set(null);
//            error_account_bank_number.set(null);
            error_address.set(null);

            return true;
        } else {
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_email.set(null);

            }
            if (address.trim().isEmpty()) {
                error_address.set(context.getString(R.string.field_required));

            } else {
                error_address.set(null);

            }
            if (banner.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.choose_banner), Toast.LENGTH_LONG).show();
            }
//            if (account_bank_number.trim().isEmpty()) {
//                error_account_bank_number.set(context.getString(R.string.field_required));
//
//            } else {
//                error_account_bank_number.set(null);
//
//            }

            if (ipad_number.trim().isEmpty()) {
                error_ipad_number.set(context.getString(R.string.field_required));

            } else if (ipad_number.length() != 22) {
                error_ipad_number.set(context.getString(R.string.ipan_number_length_error));
            } else {
                error_ipad_number.set(null);
            }
            return false;
        }
    }

    public SignUpModel() {
        setName("");
        setEmail("");
        //setAccount_bank_number("");
        setIpad_number("");
        setLatitude("");
        setLongitude("");
        setAddress("");
        setLogo(null);
        setCategory_id(0);
        setBanner("");

    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }


    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

//    @Bindable
//    public String getAccount_bank_number() {
//        return account_bank_number;
//    }
//
//    public void setAccount_bank_number(String account_bank_number) {
//        this.account_bank_number = account_bank_number;
//        notifyPropertyChanged(BR.account_bank_number);
//    }

    @Bindable
    public String getIpad_number() {
        return ipad_number;
    }

    public void setIpad_number(String ipad_number) {
        this.ipad_number = ipad_number;
        notifyPropertyChanged(BR.ipad_number);
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}

