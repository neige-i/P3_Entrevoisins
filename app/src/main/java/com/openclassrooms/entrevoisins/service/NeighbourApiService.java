package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;

/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     *
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Deletes a neighbour
     *
     * @param neighbour the {@link Neighbour} to delete
     */
    void deleteNeighbour(Neighbour neighbour);

    /**
     * Create a neighbour
     *
     * @param neighbour the {@link Neighbour} to create
     */
    void createNeighbour(Neighbour neighbour);

    /**
     * Toggles the favourite state of the given neighbour
     *
     * @param neighbour the {@link Neighbour} to change its favourite state
     */
    void toggleFavourite(Neighbour neighbour);

    /**
     * Get the favourite Neighbours only
     *
     * @return {@link List}
     */
    List<Neighbour> getFavNeighbours();
}
