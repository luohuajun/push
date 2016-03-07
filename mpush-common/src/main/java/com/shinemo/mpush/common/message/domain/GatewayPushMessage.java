package com.shinemo.mpush.common.message.domain;

import com.shinemo.mpush.api.connection.Connection;
import com.shinemo.mpush.common.message.ByteBufMessage;
import com.shinemo.mpush.api.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;

import static com.shinemo.mpush.api.protocol.Command.GATEWAY_PUSH;

/**
 * Created by ohun on 2015/12/30.
 */
public class GatewayPushMessage extends ByteBufMessage {
    public String userId;
    public String content;

    public GatewayPushMessage(String userId, String content, Connection connection) {
        super(new Packet(GATEWAY_PUSH, genSessionId()), connection);
        this.userId = userId;
        this.content = content;
    }

    public GatewayPushMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    @Override
    public void decode(ByteBuf body) {
        userId = decodeString(body);
        content = decodeString(body);
    }

    @Override
    public void encode(ByteBuf body) {
        encodeString(body, userId);
        encodeString(body, content);
    }

    @Override
    public void send() {
        super.sendRaw();
    }

    @Override
    public void send(ChannelFutureListener listener) {
        super.sendRaw(listener);
    }

    @Override
    public String toString() {
        return "GatewayPushMessage{" +
                "userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
