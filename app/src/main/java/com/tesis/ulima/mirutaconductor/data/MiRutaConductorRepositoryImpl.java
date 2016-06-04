package com.tesis.ulima.mirutaconductor.data;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Christian on 5/28/2016.
 */
public class MiRutaConductorRepositoryImpl implements MiRutaConductorRepository {
    MiRutaConductorApiService mApiService;

    public MiRutaConductorRepositoryImpl(MiRutaConductorApiService apiService){
        mApiService=apiService;
    }

    @Override
    public void fetchBusesPosition(final FetchBusesPositionsCallback callback) {
        mApiService.fetchBusesPosition(new MiRutaConductorApiService.FetchBusesPositionApiCallback<List<ParseObject>>() {
            @Override
            public void onLoaded(List<ParseObject> busesGeoPoints) {
                callback.onSuccess(busesGeoPoints);
            }

            @Override
            public void onError(ParseException e) {
                callback.onError(e);
            }
        });

    }

    @Override
    public void fetchRutaCallback(final FetchRutaCallback callback) {
        mApiService.fetchRuta(new MiRutaConductorApiService.FetchRutaApiCallBack<List<ParseObject>>() {
            @Override
            public void onLoaded(List<ParseObject> rutaGeoPoints) {
                callback.onSuccess(rutaGeoPoints);
            }

            @Override
            public void onError(ParseException e) {
                callback.onError(e);
            }
        });

    }
}
