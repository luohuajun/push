package com.shinemo.mpush.common.message.handler;

import com.shinemo.mpush.api.connection.Connection;
import com.shinemo.mpush.api.protocol.Packet;
import com.shinemo.mpush.common.message.BaseMessageHandler;
import com.shinemo.mpush.common.message.domain.OkMessage;

/**
 * Created by ohun on 2015/12/30.
 */
public class OkMessageHandler extends BaseMessageHandler<OkMessage> {
    @Override
    public OkMessage decode(Packet packet, Connection connection) {
        return new OkMessage(packet, connection);
    }

    @Override
    public void handle(OkMessage message) {

    }
}
