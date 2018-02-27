package com.gotit.quyle.tqn.service;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.model.AccessTokenModel;
import com.gotit.quyle.tqn.model.PhotoInfoModel;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoSetModel;
import com.gotit.quyle.tqn.model.event.AddPhotoEvent;
import com.gotit.quyle.tqn.model.event.DetailEvent;
import com.gotit.quyle.tqn.model.event.PhotosEvent;
import com.gotit.quyle.tqn.model.event.PhotosetEvent;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by QUYLE on 1/11/18.
 */

public class FlickrClient extends OAuthBaseClient {

    public static final BaseApi REST_API_CLASS = FlickrApi.instance(FlickrApi.FlickrPerm.DELETE);


    public static final String REST_URL = "https://www.flickr.com/services";

    public static final String REST_CONSUMER_KEY = "683a39fa3de96dfdf330aecc67273b72";

    public static final String REST_CONSUMER_SECRET = "6028c801a648f3ec";

    public static final String REST_CALLBACK_URL = "oauth://cprest";

    public static final long ID_FAVORATE_PHOTOSET = 72157689675511292L;


    public FlickrClient(Context context) {

        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
                REST_CALLBACK_URL);
        setBaseUrl("https://api.flickr.com/services/rest");
    }

    public void getPhotoSet(AsyncHttpResponseHandler handler, long idPhotoset) {
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.photosets.getPhotos&photoset_id=" + idPhotoset + "&user_id=154490425%40N08");
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void addPhotoToPhotoSet(AsyncHttpResponseHandler handler, long idPhoto) {
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.photosets.addPhoto");
        RequestParams params = new RequestParams();
        params.put("api_key", REST_CONSUMER_KEY);
        params.put("photoset_id", ID_FAVORATE_PHOTOSET);
        params.put("photo_id", idPhoto);

        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.post(apiUrl, params, handler);
    }


    public void getPhotoSets(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.photosets.getList&user_id=154490425%40N08");
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getDetail(AsyncHttpResponseHandler handler, long id) {
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.photos.getInfo&photo_id=" + id);
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getDetailAsync(long id) {
        getDetail(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject json) {
                Log.d("DEBUG", "result: " + json.toString());
                List<PhotoModel> photoItems = new ArrayList<PhotoModel>();
                try {
                    JSONObject photo = json.getJSONObject("photo");
                    PhotoInfoModel item = getGson().fromJson(photo.toString(), PhotoInfoModel.class);
                    EventBus.getDefault().post(new DetailEvent(item, null));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("debug", e.toString());
                }
                Log.d("DEBUG", "Total: " + photoItems.size());
            }
        }, id);
    }

    public void getImageFromPhotosetAsync(final long photosetId) {
        getPhotoSet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject json) {
                Log.d("DEBUG", "result: " + json.toString());
                List<PhotoModel> photoItems = new ArrayList<PhotoModel>();
                try {
                    JSONArray photos = json.getJSONObject("photoset").getJSONArray("photo");

                    photoItems = PhotoModel.getPhotoModelJson(photos.toString());
                    TQNApplication.getPrefManager().savePhotobyId(photoItems, photosetId);
                    Log.d("DEBUG", "result size " + TQNApplication.getPrefManager().getPhotos(photosetId).size());

                    EventBus.getDefault().post(new PhotosEvent(photoItems, null));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("debug", e.toString());
                }
            }
        }, photosetId);
    }

    public void addPhotoToPhotosetAsync(final long photoId) {
        addPhotoToPhotoSet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject json) {
                Log.d("DEBUG", "result: " + json.toString());
                try {
                    String result = json.getString("stat");
                    Log.d("DEBUG", "result " + result);
                    EventBus.getDefault().post(new AddPhotoEvent(result, null));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("debug", e.toString());
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", "result failure: " + throwable.getMessage());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        }, photoId);


    }

    public void getPhotosetsAsync() {
        getPhotoSets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject json) {
                Log.d("DEBUG", "result: " + json.toString());
                List<PhotoSetModel> photoItems = new ArrayList<PhotoSetModel>();
                try {
                    JSONArray photoset = json.getJSONObject("photosets").getJSONArray("photoset");
                    photoItems = PhotoSetModel.getPhotoSetModelJson(photoset.toString());
                    TQNApplication.getPrefManager().savePhotosets(photoItems);
                    EventBus.getDefault().post(new PhotosetEvent(photoItems, null));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("debug", e.toString());
                }
                Log.d("DEBUG", "Total: " + photoItems.size());
            }
        });
    }


    private Gson getGson() {
        return new GsonBuilder().create();
    }

}