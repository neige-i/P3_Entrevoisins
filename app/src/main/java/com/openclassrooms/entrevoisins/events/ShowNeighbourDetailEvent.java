package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

/**
 * Event fired when a user wants to see the details of a Neighbour
 */
public class ShowNeighbourDetailEvent {

    /**
     * Neighbour to show its information
     */
    public Neighbour mNeighbour;
    /**
     * Id of the fragment that displays the Neighbour
     */
    public int mWhichNeighbour;

    public ShowNeighbourDetailEvent(Neighbour neighbour, int whichNeighbour) {
        mNeighbour = neighbour;
        mWhichNeighbour = whichNeighbour;
    }
}
