package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

/**
 * Event fired when a user wants to see the details of a Neighbour
 */
public class ShowNeighbourDetailEvent {

    /**
     * Neighbour to delete
     */
    public Neighbour neighbour;

    /**
     * Constructor.
     * @param neighbour
     */
    public ShowNeighbourDetailEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
