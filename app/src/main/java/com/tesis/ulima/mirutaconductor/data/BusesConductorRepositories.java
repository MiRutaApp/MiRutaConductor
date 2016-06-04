package com.tesis.ulima.mirutaconductor.data;

import android.support.annotation.NonNull;


/**
 * Created by Christian on 5/28/2016.
 */
public class BusesConductorRepositories {

    public static MiRutaConductorRepository sMiRutaConductorRepository = null;

    public static MiRutaConductorRepository getBusesPositionRepository(@NonNull MiRutaConductorApiService apiService){
        if (sMiRutaConductorRepository ==null){
            sMiRutaConductorRepository = new MiRutaConductorRepositoryImpl(apiService);
        }
        return sMiRutaConductorRepository;
    }

}
