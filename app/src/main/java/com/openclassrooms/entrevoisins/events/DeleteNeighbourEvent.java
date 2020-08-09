package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

/**
 * Event fired when a user deletes a Neighbour
 */
public class DeleteNeighbourEvent {

    /**
     * Neighbour to delete
     */
    public Neighbour mNeighbour;
    /**
     * Id of the fragment that displays the Neighbour
     */
    public int mWhichNeighbour;

    public DeleteNeighbourEvent(Neighbour neighbour, int whichNeighbour) {
        mNeighbour = neighbour;
        mWhichNeighbour = whichNeighbour;
    }
}
