package com.openclassrooms.entrevoisins.di;

import com.openclassrooms.entrevoisins.service.DummyNeighbourApiService;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static NeighbourApiService service = new DummyNeighbourApiService();

    /**
     * @return an instance on @{@link NeighbourApiService}
     */
    public static NeighbourApiService getNeighbourApiService() {
        return service;
    }

    /**
     * @return always a new instance on @{@link NeighbourApiService}.
     * Useful for tests, so we ensure the context is clean.
     */
    public static NeighbourApiService getNewInstanceApiService() {
        return new DummyNeighbourApiService();
    }
}
