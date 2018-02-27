package com.gotit.quyle.tqn.model;

import java.util.ArrayList;

/**
 * Created by QUYLE on 1/24/18.
 */

public class SampleData {

    public static final ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(30);

        for (int i = 0; i < 30; i++) {
            data.add("SAMPLE #");
        }

        return data;
    }
}
