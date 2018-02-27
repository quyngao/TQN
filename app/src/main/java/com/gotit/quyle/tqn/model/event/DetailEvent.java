package com.gotit.quyle.tqn.model.event;


import com.gotit.quyle.tqn.model.PhotoInfoModel;

/**
 * Created by gturedi on 7.02.2017.
 */
public class DetailEvent
        extends BaseServiceEvent<PhotoInfoModel> {

    public DetailEvent(PhotoInfoModel item, Throwable exception) {
        super(item, exception);
    }

}