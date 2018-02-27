package com.gotit.quyle.tqn.model.event;


import com.gotit.quyle.tqn.model.PhotoModel;

import java.util.List;

/**
 * Created by gturedi on 7.02.2017.
 */
public class PhotosEvent
        extends BaseServiceEvent<List<PhotoModel>> {

    public PhotosEvent(List<PhotoModel> item, Throwable exception) {
        super(item, exception);
    }

}