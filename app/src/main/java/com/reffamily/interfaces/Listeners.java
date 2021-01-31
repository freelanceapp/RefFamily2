package com.reffamily.interfaces;


public interface Listeners {


    interface SignUpListener{

        void openSheet(int type);
        void closeSheet();
        void checkDataValid();
        void checkReadPermission();
        void checkCameraPermission();

    }
    interface AddSubCategoryListener{

        void openSheet();
        void closeSheet();
        void checkDataValid();
        void checkReadPermission();
        void checkCameraPermission();
    }

    interface AddProductStep1Listener{

        void openSheet();
        void closeSheet();
        void checkReadPermission();
        void checkCameraPermission();

    }



    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }
    interface BackListener
    {
        void back();
    }
    interface LoginListener{
        void validate();
        void showCountryDialog();
        void costumerApp();

    }


    interface SettingAction{
        void onSubscriptions();
        void onProfile();
        void onEditProfile();
        void onFeedback();
        void onLanguageSetting();
        void onTerms();
        void onPrivacy();
        void onRate();
        void adjust();

        void onTone();
        void about();
        void logout();
        void subCategories();
        void share();
        void contactUs();

    }
}
