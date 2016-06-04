package com.tesis.ulima.mirutaconductor;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.tesis.ulima.mirutaconductor.data.MiRutaConductorRepository;

import java.util.List;

/**
 * Created by Christian on 5/29/2016.
 */
public class BusesPositionPresenter implements BusesPositionContract.UserActionListener{
    private BusesPositionContract.View mView;
    private MiRutaConductorRepository mMiRutaConductorRepository;

    public BusesPositionPresenter(BusesPositionContract.View view, MiRutaConductorRepository miRutaConductorRepository){
        mView=view;
        mMiRutaConductorRepository = miRutaConductorRepository;
    }
    @Override
    public void requestBusesPositon() {
        mMiRutaConductorRepository.fetchBusesPosition(new MiRutaConductorRepository.FetchBusesPositionsCallback() {
            @Override
            public void onSuccess(List<ParseObject> geoPointsList) {
                mView.showPositions(geoPointsList);
            }

            @Override
            public void onError(ParseException e) {
                mView.showRequestError();
            }
        });

    }

}
