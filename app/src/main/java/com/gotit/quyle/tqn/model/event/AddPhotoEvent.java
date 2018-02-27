package com.gotit.quyle.tqn.model.event;

import com.gotit.quyle.tqn.model.PhotoInfoModel;

/**
 * Created by QUYLE on 1/31/18.
 */

public class AddPhotoEvent extends BaseServiceEvent<String>  {
    public AddPhotoEvent(String item, Throwable exception) {
        super(item, exception);
    }
}
