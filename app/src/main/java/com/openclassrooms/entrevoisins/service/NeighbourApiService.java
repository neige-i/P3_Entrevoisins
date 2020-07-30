package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Deletes a neighbour
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);

    /**
     * Create a neighbour
     * @param neighbour
     */
    void createNeighbour(Neighbour neighbour);

    /**
     * Get the favourite Neighbours only
     * @return {@link List}
     */
    List<Neighbour> getFavNeighbours();

    /**
     * Toggles the favourite state of the given neighbour
     * @param neighbour
     */
    void toggleFavourite(Neighbour neighbour);
//
//    /**
//     * Removes a neighbour from the favourite list
//     * @param neighbour
//     */
//    void removeFavouriteNeighbour(Neighbour neighbour);
//
//    /**
//     * Adds a neighbour to the favourite list
//     * @param neighbour
//     */
//    void addFavouriteNeighbour(Neighbour neighbour);
}
