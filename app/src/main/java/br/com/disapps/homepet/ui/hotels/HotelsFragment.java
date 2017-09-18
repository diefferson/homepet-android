package br.com.disapps.homepet.ui.hotels;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import br.com.disapps.homepet.R;
import br.com.disapps.homepet.app.HomePet;
import br.com.disapps.homepet.data.model.Hotel;
import br.com.disapps.homepet.data.ws.request.HotelsRequest;
import br.com.disapps.homepet.ui.common.AppFragment;

import br.com.disapps.homepet.ui.hotel.HotelActivity;
import br.com.disapps.homepet.ui.hotels.adapter.HotelAdapter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by diefferson.santos on 23/08/17.
 */

public class HotelsFragment extends AppFragment<IHotelsView , HotelsPresenter> implements IHotelsView   {

    @BindView(R.id.hotel_recycler)
    RecyclerView hotelRecycler;

    @BindView(R.id.loading_view)
    ConstraintLayout loadingView;

    @BindView(R.id.bottom_sheet)
    View bottomSheet;

    @BindView(R.id.apply_sort)
    Button applySort;

    @BindView(R.id.sort)
    RadioGroup sort;

    @BindView(R.id.sense)
    RadioGroup sense;

    private HotelAdapter hotelAdapter;

    private BottomSheetBehavior mBottomSheetBehavior;

    public static HotelsFragment newInstance(){
        return  new HotelsFragment();
    }

    @Override
    public String getFragmentTag() {
        return HotelsFragment.class.getSimpleName();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_hotels;
    }

    @Override
    public HotelsPresenter createPresenter() {
        return new HotelsPresenter(HomePet.Companion.getInstance().getHotelRepository());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        getAppActivityListener().setTitle("Pesquisar");

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);

        setupLoadingFragment(loadingView);

        hotelRecycler.setHasFixedSize(true);
        hotelRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getPresenter().loadHoteis(new HotelsRequest());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hotels_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter_hotel){

            if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }else{
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fillHotelAdapter(List<Hotel> hoteis) {

        hotelAdapter = new HotelAdapter(hoteis);

        hotelAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position)-> {
            Intent intent = new Intent(getActivity(), HotelActivity.class);
            Bundle args = new Bundle();
            args.putInt("hotelCode", hotelAdapter.getItem(position).getCode());
            intent.putExtras(args);
            startActivity(intent);

        });

        hotelRecycler.setAdapter(hotelAdapter);
    }

    @OnClick(R.id.apply_sort)
    public void applySort(){
        HotelsRequest request = new HotelsRequest();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        switch (sort.getCheckedRadioButtonId()){
            case R.id.price:
                request.setSort("price");
                break;
            case R.id.comments:
                request.setSort("comments");
                break;
            case R.id.rating:
                request.setSort("rating");
                break;
        }

        switch (sense.getCheckedRadioButtonId()){
            case R.id.asc:
                request.setSense("asc");
                break;
            case R.id.desc:
                request.setSense("desc");
                break;
        }

        getPresenter().loadHoteis(request);
    }
}
