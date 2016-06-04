package com.tesis.ulima.mirutaconductor.data;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Christian on 5/28/2016.
 */
public interface MiRutaConductorRepository {

    interface FetchBusesPositionsCallback {
        void onSuccess(List<ParseObject> geoPointsList);

        void onError(ParseException e);
    }

    void fetchBusesPosition(FetchBusesPositionsCallback callback);

    interface FetchRutaCallback {
        void onSuccess(List<ParseObject> geoPointsList);

        void onError(ParseException e);
    }

    void fetchRutaCallback(FetchRutaCallback callback);
}
