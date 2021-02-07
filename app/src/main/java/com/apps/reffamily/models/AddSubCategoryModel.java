package com.apps.reffamily.models;

import android.content.Context;
import android.net.Uri;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.apps.reffamily.BR;
import com.apps.reffamily.R;


public class AddSubCategoryModel extends BaseObservable {

    private int id;
    private String title;
    private String desc;
    private Uri image;
    private String category_id;

    public ObservableField<String> error_title = new ObservableField<>();
    public ObservableField<String> error_desc = new ObservableField<>();



    public boolean isDataValid(Context context)
    {
        if (!title.trim().isEmpty()&&
                !desc.trim().isEmpty()
        ){
            error_title.set(null);
            error_desc.set(null);

            return true;
        }else
            {
                if (title.trim().isEmpty())
                {
                    error_title.set(context.getString(R.string.field_required));

                }else
                    {
                        error_title.set(null);

                    }


                if (desc.trim().isEmpty())
                {
                    error_desc.set(context.getString(R.string.field_required));

                }else
                {
                    error_desc.set(null);

                }


                return false;
            }
    }
    public AddSubCategoryModel() {
        setTitle("");
        setDesc("");
        setImage(null);

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




    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}

