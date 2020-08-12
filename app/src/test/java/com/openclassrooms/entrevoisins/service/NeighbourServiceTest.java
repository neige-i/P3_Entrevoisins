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
        // Given: an expected list of neighbours
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.generateNeighbours();
        // When: we retrieve the list of neighbours
        List<Neighbour> neighbours = service.getNeighbours();
        // Then: the two lists contain the same neighbours
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        // Given: we want to delete the first neighbour
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        // When: we delete that neighbour
        service.deleteNeighbour(neighbourToDelete);
        // Then: the list does not contain that neighbour anymore
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void createNeighbourWithSuccess() {
        // Given: a new neighbour to add to the list
        Neighbour neighbourToCreate = new Neighbour(-1L, "", "", "", "", "");
        // When: the neighbour is added to the list
        service.createNeighbour(neighbourToCreate);
        // Then: the list contains that neighbour
        assertTrue(service.getNeighbours().contains(neighbourToCreate));
    }

    @Test
    public void toggleFavouriteWithSuccess() {
        // Given: we want to make the first neighbour a favourite one
        Neighbour neighbourToToggleFavourite = service.getNeighbours().get(0);
        // When: the favourite state of the neighbour is toggled
        service.toggleFavourite(neighbourToToggleFavourite);
        // Then: the neighbour is favourite
        assertTrue(neighbourToToggleFavourite.isFavourite());
    }

    @Test
    public void getFavouriteNeighboursWithSuccess() {
        // Given: the fourth and tenth neighbours of the list are expected to be favourite
        List<Neighbour> neighbours = service.getNeighbours();
        Neighbour neighbour3 = neighbours.get(3), neighbour9 = neighbours.get(9);
        List<Neighbour> expectedFavourite = Arrays.asList(neighbour3, neighbour9);
        // When: we change the favourite state
        service.toggleFavourite(neighbour3);
        service.toggleFavourite(neighbour9);
        // Then: the favourite list only contains the expected ones
        assertThat(service.getFavNeighbours(), IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFavourite.toArray()));
    }
}
