package com.gotit.quyle.tqn.model;

/**
 * Created by gturedi on 10.02.2017.
 */
public enum ImageSize {

    LARGE_SQUARE,
    MEDIUM,
    LARGE;


    public String getValue() {
        if (this == MEDIUM) return "z";
        else if(this == LARGE_SQUARE) return "q";
        return "h";
    }

}