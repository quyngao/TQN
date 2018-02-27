package com.gotit.quyle.tqn.model;

/**
 * Created by QUYLE on 1/8/18.
 */

public class Photo {
    private String name;
    private int numOfSongs;
    private String thumbnail;

    public Photo() {
    }

    public Photo(PhotoModel photoModel) {
        this.name = "TQN";
        this.thumbnail = photoModel.getImageUrl(ImageSize.LARGE);
        this.numOfSongs = 0;
    }

    public Photo(String name, int numOfSongs, String thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
