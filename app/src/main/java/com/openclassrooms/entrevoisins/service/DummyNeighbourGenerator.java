package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyNeighbourGenerator {

    static List<Neighbour> generateNeighbours() {
        String avatarUrl = "https://i.pravatar.cc/350?u=";
        String address = "Saint-Pierre-du-Mont ; 5km";
        String phoneNumber = "+33 6 86 57 90 14";
        String aboutMe = "Bonjour !Je souhaiterais faire de la marche nordique. Pas initiée, " +
                "je recherche une ou plusieurs personnes susceptibles de m'accompagner !J'aime " +
                "les jeux de cartes tels la belote et le tarot..";

        return new ArrayList<>(Arrays.asList(
                new Neighbour(1, "Caroline", avatarUrl + "a042581f4e29026704d", address, phoneNumber, aboutMe),
                new Neighbour(2, "Jack", avatarUrl + "a042581f4e29026704e", address, phoneNumber, aboutMe),
                new Neighbour(3, "Chloé", avatarUrl + "a042581f4e29026704f", address, phoneNumber, aboutMe),
                new Neighbour(4, "Vincent", avatarUrl + "a042581f4e29026704a", address, phoneNumber, aboutMe),
                new Neighbour(5, "Elodie", avatarUrl + "a042581f4e29026704b", address, phoneNumber, aboutMe),
                new Neighbour(6, "Sylvain", avatarUrl + "a042581f4e29026704c", address, phoneNumber, aboutMe),
                new Neighbour(7, "Laetitia", avatarUrl + "a042581f4e29026703d", address, phoneNumber, aboutMe),
                new Neighbour(8, "Dan", avatarUrl + "a042581f4e29026703b", address, phoneNumber, aboutMe),
                new Neighbour(9, "Joseph", avatarUrl + "a042581f4e29026704d", address, phoneNumber, aboutMe),
                new Neighbour(10, "Emma", avatarUrl + "a042581f4e29026706d", address, phoneNumber, aboutMe),
                new Neighbour(11, "Patrick", avatarUrl + "a042581f4e29026702d", address, phoneNumber, aboutMe),
                new Neighbour(12, "Ludovic", avatarUrl + "a042581f3e39026702d", address, phoneNumber, aboutMe)
        ));
    }
}
