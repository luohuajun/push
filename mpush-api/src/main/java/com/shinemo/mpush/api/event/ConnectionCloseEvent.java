package com.shinemo.mpush.api.event;

import com.shinemo.mpush.api.connection.Connection;

/**
 * Created by ohun on 2016/1/10.
 */
public final class ConnectionCloseEvent implements Event {
    public final Connection connection;


    public ConnectionCloseEvent(Connection connection) {
        this.connection = connection;
    }
}
