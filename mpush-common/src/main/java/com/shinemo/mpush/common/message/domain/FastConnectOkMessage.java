package com.shinemo.mpush.common.message.domain;

import com.shinemo.mpush.api.connection.Connection;
import com.shinemo.mpush.api.protocol.Packet;
import com.shinemo.mpush.common.message.BaseMessage;
import com.shinemo.mpush.common.message.ByteBufMessage;

import io.netty.buffer.ByteBuf;

/**
 * Created by ohun on 2015/12/28.
 */
public final class FastConnectOkMessage extends ByteBufMessage {
    public int heartbeat;

    public FastConnectOkMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    public static FastConnectOkMessage from(BaseMessage src) {
        return new FastConnectOkMessage(src.createResponse(), src.getConnection());
    }

    @Override
    public void decode(ByteBuf body) {
        heartbeat = decodeInt(body);
    }

    @Override
    public void encode(ByteBuf body) {
        encodeInt(body, heartbeat);
    }

    public FastConnectOkMessage setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        return this;
    }
}
