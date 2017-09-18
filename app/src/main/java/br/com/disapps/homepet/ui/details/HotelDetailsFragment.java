package br.com.disapps.homepet.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.disapps.homepet.R;
import br.com.disapps.homepet.app.HomePet;
import br.com.disapps.homepet.data.model.Hotel;
import br.com.disapps.homepet.ui.common.AppFragment;
import br.com.disapps.homepet.ui.common.AppView;
import br.com.disapps.homepet.ui.details.adapter.ServiceAdapter;
import butterknife.BindView;

/**
 * Created by diefferson.santos on 31/08/17.
 */

public class HotelDetailsFragment extends AppFragment<IHotelDetailsView, HotelDetailsPresenter> implements IHotelDetailsView {

    @BindView(R.id.loading_view)
    ConstraintLayout loadView;

    @BindView(R.id.content)
    ConstraintLayout content;

    @BindView(R.id.hotel_description)
    TextView hotelDescription;

    @BindView(R.id.hotel_address)
    TextView hotelAddress;

    @BindView(R.id.ratings)
    TextView ratings;

    @BindView(R.id.comments)
    TextView comments;

    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.service_recycler)
    RecyclerView serviceRecycler;

    @BindView(R.id.rating_bt)
    Button rattingButton;

    @BindView(R.id.comment_bt)
    Button commentButton;

    private ServiceAdapter serviceAdapter;

    public static HotelDetailsFragment newInstance(int codeHotel){

        HotelDetailsFragment hotelDetailsFragment = new HotelDetailsFragment();

        Bundle args = new Bundle();
        args.putInt("codeHotel", codeHotel);
        hotelDetailsFragment.setArguments(args);

        return hotelDetailsFragment;

    }

    @Override
    public String getFragmentTag() {
        return HotelDetailsFragment.class.getSimpleName();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_hotel_details;
    }

    @Override
    public HotelDetailsPresenter createPresenter() {
        return new HotelDetailsPresenter(HomePet.Companion.getInstance().getHotelRepository());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLoadingFragment(loadView);

        int codeHotel = getArguments().getInt("codeHotel");

        getPresenter().loadHotel(codeHotel);

        validateLogin();

    }

    private void validateLogin(){
        if(!HomePet.Companion.getInstance().getPreferences().isLogged()){
            rattingButton.setVisibility(View.GONE);
            commentButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void fillHotelDetails(Hotel hotel) {
        hotelDescription.setText(hotel.getDescription());
        hotelAddress.setText(hotel.getCompleteAddress());
        ratings.setText(String.valueOf(hotel.getRating()));
        comments.setText(String.valueOf(hotel.getCommentsNumber()));
        rating.setRating(hotel.getRating());
        content.setVisibility(View.VISIBLE);
        serviceAdapter = new ServiceAdapter(hotel.getServices());
        serviceRecycler.setAdapter(serviceAdapter);
    }
}
