package com.tesis.ulima.mirutaconductor.data;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Christian on 5/28/2016.
 */
public interface MiRutaConductorApiService {

    interface FetchBusesPositionApiCallback<T> {
        void onLoaded(T busesGeoPoints);

        void onError(ParseException e);
    }

    void fetchBusesPosition(FetchBusesPositionApiCallback<List<ParseObject>> callback);

    interface FetchRutaApiCallBack<T> {
        void onLoaded(T rutaGeoPoints);

        void onError(ParseException e);
    }

    void fetchRuta(FetchRutaApiCallBack<List<ParseObject>> callBack);


}
