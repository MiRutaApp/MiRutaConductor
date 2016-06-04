package com.tesis.ulima.mirutaconductor.prod;

import com.tesis.ulima.mirutaconductor.data.BusesConductorRepositories;
import com.tesis.ulima.mirutaconductor.data.MiRutaConductorRepository;

/**
 * Created by Christian on 5/29/2016.
 */
public class Injection {

    public static MiRutaConductorRepository provideBusesPositionRepository(){
        return BusesConductorRepositories.getBusesPositionRepository(BusesConductorApiClient.getInstance());
    }
}
