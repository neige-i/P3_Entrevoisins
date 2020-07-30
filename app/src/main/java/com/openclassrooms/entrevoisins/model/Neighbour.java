package com.openclassrooms.entrevoisins.model;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Objects;

/**
 * Model object representing a Neighbour
 */
public class Neighbour implements Serializable {

    /** Identifier */
    private long id;

    /** Full name */
    private String name;

    /** Avatar */
    private String avatarUrl;

    /** Adress */
    private String address;

    /** Phone number */
    private String phoneNumber;

    /** Website */
    private String websiteUrl;

    /** About me */
    private String aboutMe;

    /** Favourite or not */
    private boolean isFavourite;

    /**
     * Constructor
     * @param id
     * @param name
     * @param avatarUrl
     */
    public Neighbour(long id, String name, String avatarUrl, String address,
                     String phoneNumber, String aboutMe) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUrl = generateWebsiteFromName(name);
        this.aboutMe = aboutMe;
        this.isFavourite = false;
    }

    public Neighbour(Neighbour neighbour) {
        this.id = neighbour.id;
        this.name = neighbour.name;
        this.avatarUrl = neighbour.avatarUrl;
        this.address = neighbour.address;
        this.phoneNumber = neighbour.phoneNumber;
        this.websiteUrl = neighbour.websiteUrl;
        this.aboutMe = neighbour.aboutMe;
        this.isFavourite = neighbour.isFavourite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    /**
     * Generates the facebook website address according to the neighbour's {@link #name}.
     * <br />It removes all the accents from the name and makes it all in lowercase before adding it
     * to the beginning of the facebook website URL.
     *
     * @param name Name that is used to determine the end of the facebook website URL
     * @return  The complete facebook website address
     */
    private String generateWebsiteFromName(String name) {
        String nameUrl = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
        return "www.facebook.com/" + nameUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neighbour neighbour = (Neighbour) o;
        return Objects.equals(id, neighbour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" + name + ", " + isFavourite + '}';
    }
}
