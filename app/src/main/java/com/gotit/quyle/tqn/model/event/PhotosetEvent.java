package com.gotit.quyle.tqn.model.event;


import com.gotit.quyle.tqn.model.PhotoSetModel;

import java.util.List;

/**
 * Created by QUYLE on 1/11/18.
 */

public class PhotosetEvent extends BaseServiceEvent<List<PhotoSetModel>> {

    public PhotosetEvent(List<PhotoSetModel> item, Throwable exception) {
        super(item, exception);
    }
}