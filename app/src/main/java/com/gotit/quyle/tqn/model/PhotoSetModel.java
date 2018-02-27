package com.gotit.quyle.tqn.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QUYLE on 1/11/18.
 */

public class PhotoSetModel extends BaseModel {

    private static final String IMAGE_URL = "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";

    public long id;
    public String secret;
    public String server;
    public String primary;
    public int farm;

    public ContentModel title;
    public ContentModel description;
    public int photos;
    public int videos;

    public String getImageUrl() {
        return String.format(IMAGE_URL, farm, server, primary, secret, ImageSize.MEDIUM.getValue());
    }


    public static String getJsonString(List<PhotoSetModel> subjects) {
        String json = new Gson().toJson(subjects);
        return json;
    }

    public static List<PhotoSetModel> getPhotoSetModelJson(String arrays) {
        Type listType = new TypeToken<ArrayList<PhotoSetModel>>() {
        }.getType();
        List<PhotoSetModel> list = new Gson().fromJson(arrays, listType);
        return list;
    }
}
