package com.gotit.quyle.tqn.listener;

/**
 * Created by gturedi on 8.02.2017.
 */
public interface RowClickListener<T> {

    void onRowClicked(int row, T item);

}