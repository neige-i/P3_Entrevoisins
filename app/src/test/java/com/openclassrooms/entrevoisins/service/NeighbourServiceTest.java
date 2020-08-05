package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void createNeighbourWithSuccess() {
        Neighbour neighbourToCreate = new Neighbour(System.currentTimeMillis(), "", "", "", "", "");
        service.createNeighbour(neighbourToCreate);
        assertTrue(service.getNeighbours().contains(neighbourToCreate));
    }

    @Test
    public void toggleFavouriteWithSuccess() {
        Neighbour neighbourToToggleFavourite = service.getNeighbours().get(0);
        boolean initialFavouriteState = neighbourToToggleFavourite.isFavourite();
        service.toggleFavourite(neighbourToToggleFavourite);
        assertEquals(!initialFavouriteState, service.getNeighbours().get(0).isFavourite());

        service.toggleFavourite(neighbourToToggleFavourite);
        assertEquals(initialFavouriteState, service.getNeighbours().get(0).isFavourite());
    }

    @Test
    public void getFavouriteNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> favNeighbours = Arrays.asList(neighbours.get(3), neighbours.get(9));

        service.toggleFavourite(favNeighbours.get(0));
        service.toggleFavourite(favNeighbours.get(1));
        assertThat(service.getFavNeighbours(), IsIterableContainingInAnyOrder.containsInAnyOrder(favNeighbours.toArray()));

        service.toggleFavourite(favNeighbours.get(0));
        service.toggleFavourite(favNeighbours.get(1));
        assertTrue(service.getFavNeighbours().isEmpty());
    }
}
