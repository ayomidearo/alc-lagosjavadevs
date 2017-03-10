package me.ayomidearo.lagosjavadevs.model;

/**
 * Created by aro on 3/10/17.
 */

public class Devs {
    private String username;
    private String profileImage;
    private String profileUrl;

    public Devs(String username, String profileImage, String profileUrl) {
        this.username = username;
        this.profileImage = profileImage;
        this.profileUrl = profileUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
