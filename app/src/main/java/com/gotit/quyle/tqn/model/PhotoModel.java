package com.gotit.quyle.tqn.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gturedi on 7.02.2017.
 */
public class PhotoModel
        extends BaseModel {

    // https://www.flickr.com/services/api/misc.urls.html
    // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
    private static final String IMAGE_URL = "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";

    public long id;
    public String secret;
    public String server;
    public int farm;
    //public String owner;
    //public String title;

    public String getImageUrl(ImageSize size) {
        return String.format(IMAGE_URL, farm, server, id, secret, size.getValue());
    }

    public static String getJsonString(List<PhotoModel> subjects) {
        String json = new Gson().toJson(subjects);
        return json;
    }

    public static List<PhotoModel> getPhotoModelJson(String arrays) {
        Type listType = new TypeToken<ArrayList<PhotoModel>>() {
        }.getType();
        List<PhotoModel> list = new Gson().fromJson(arrays, listType);
        return list;
    }

}