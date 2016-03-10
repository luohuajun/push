package com.shinemo.mpush.common.message.domain;

import com.shinemo.mpush.api.connection.Connection;
import com.shinemo.mpush.api.protocol.Packet;
import com.shinemo.mpush.common.ErrorCode;
import com.shinemo.mpush.common.message.BaseMessage;
import com.shinemo.mpush.common.message.ByteBufMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import static com.shinemo.mpush.api.protocol.Command.ERROR;

/**
 * Created by ohun on 2015/12/28.
 */
public final class ErrorMessage extends ByteBufMessage {
    public byte cmd;
    public byte code;
    public String reason;

    public ErrorMessage(byte cmd, Packet message, Connection connection) {
        super(message, connection);
        this.cmd = cmd;
    }

    public ErrorMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    @Override
    public void decode(ByteBuf body) {
        cmd = decodeByte(body);
        code = decodeByte(body);
        reason = decodeString(body);
    }

    @Override
    public void encode(ByteBuf body) {
        encodeByte(body, cmd);
        encodeByte(body, code);
        encodeString(body, reason);
    }

    public static ErrorMessage from(BaseMessage src) {
        return new ErrorMessage(src.getPacket().cmd, new Packet(ERROR
                , src.getPacket().sessionId), src.getConnection());
    }

    public static ErrorMessage from(Packet src, Connection connection) {
        return new ErrorMessage(src.cmd, new Packet(ERROR, src.sessionId), connection);
    }


    public ErrorMessage setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ErrorMessage setErrorCode(ErrorCode code) {
        this.code = code.errorCode;
        this.reason = code.errorMsg;
        return this;
    }

    @Override
    public void send() {
        super.sendRaw();
    }

    @Override
    public void close() {
        sendRaw(ChannelFutureListener.CLOSE);
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "reason='" + reason + '\'' +
                ", code=" + code +
                ", packet=" + getPacket() +
                '}';
    }
}
