package com.tesis.ulima.mirutaconductor;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Christian on 5/29/2016.
 */
public class BusesPositionContract {
    public interface View{
        void showPositions(List<ParseObject> positions);
        void showRequestError();
    }

    public interface UserActionListener {
        void requestBusesPositon();
    }
}
