package com.apps.reffamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.reffamily.R;
import com.apps.reffamily.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.apps.reffamily.databinding.OffersRowBinding;
import com.apps.reffamily.models.AddProductModel;
import com.apps.reffamily.models.SingleProductModel;
import com.apps.reffamily.models.UserModel;
import com.apps.reffamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferAdapterVH> {

    private List<SingleProductModel> offerlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;
  // if 0 date from  ,,,, if 1 date to
    public AddProductModel.Data addProductModel;
    private Fragment fragment;

    public OfferAdapter(List<SingleProductModel> offerlist, Context context, Fragment fragment) {
        this.offerlist = offerlist;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OfferAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OffersRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.offers_row, parent, false);
        return new OfferAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapterVH holder, int position) {


        holder.binding.setModel(offerlist.get(position));

        holder.binding.deleteBtn.setOnClickListener(view -> {

            if(fragment instanceof Fragment_Offers){
                Fragment_Offers fragment_offers=(Fragment_Offers)fragment;
                fragment_offers.delete(offerlist.get(position),position);
            }
        });
        holder.binding.updateBtn.setOnClickListener(view -> {
if(fragment instanceof Fragment_Offers){
    Fragment_Offers fragment_offers=(Fragment_Offers)fragment;
    fragment_offers.offer(offerlist.get(position));
}

        });



}



    @Override
    public int getItemCount() {
        return offerlist.size();
    }

public class OfferAdapterVH extends RecyclerView.ViewHolder {
    public OffersRowBinding binding;

    public OfferAdapterVH(@NonNull OffersRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
}


}
