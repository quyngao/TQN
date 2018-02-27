package com.gotit.quyle.tqn.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoSetModel;

import java.util.List;

/**
 * Created by QUYLE on 1/14/18.
 */

public class PrefManager {

    private static PrefManager sIntance;
    private SharedPreferences mPreference;

    private PrefManager(Context context) {
        mPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefManager getInstance(Context context) {
        if (sIntance == null) {
            sIntance = new PrefManager(context);
        }

        return sIntance;
    }

    public void setString(String key, String value) {
        mPreference.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return mPreference.getString(key, "");
    }

    public void setInt(String key, int value) {
        mPreference.edit().putInt(key, value).commit();
    }

    public int getInt(String key) {
        return mPreference.getInt(key, 0);
    }

    public void setBoolean(String key, boolean value) {
        mPreference.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return mPreference.getBoolean(key, false);
    }

    public void clearPreferences() {
        mPreference.edit().clear().commit();
    }


    public void savePhotosets(List<PhotoSetModel> photoItems) {
        String photosetJson = PhotoSetModel.getJsonString(photoItems);
        this.setString(PrefConstans.EXTRA_PHOTOSETS, photosetJson);
    }

    public List<PhotoSetModel> getPhotoset() {
        return PhotoSetModel.getPhotoSetModelJson(this.getString(PrefConstans.EXTRA_PHOTOSETS));
    }

    public void savePhotobyId(List<PhotoModel> photoItems, long id) {
        String photos = PhotoModel.getJsonString(photoItems);
        this.setString(String.format(PrefConstans.EXTRA_PHOTOSET_BY_ID, id), photos);
    }

    public List<PhotoModel> getPhotos(long id) {
        return PhotoModel.getPhotoModelJson(this.getString(String.format(PrefConstans.EXTRA_PHOTOSET_BY_ID, id)));
    }

    public int getToDay() {
        return this.getInt(PrefConstans.EXTRA_TO_DAY);
    }

    public String getQuoteToDay() {
        return this.getString(PrefConstans.EXTRA_QUOTE_TODAY);
    }

    public void setToDay(int day) {
        this.setInt(PrefConstans.EXTRA_TO_DAY, day);
    }

    public void setQuote(String quote) {
        this.setString(PrefConstans.EXTRA_QUOTE_TODAY, quote);
    }

}
