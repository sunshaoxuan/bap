package jp.co.bobb.common.enums;

/**
 * websocket发送消息的动作 枚举
 */
public enum WebSocketMessageActionEnum {

    CONNECT(1, "首次连接，或者重连"),
    CHAT(2, "消息"),
    SIGNED(3, "消息是否送达"),
    KEEPALIVE(4, "心跳"),
    PULL_FRIEND(5, "好友申请");

    public final Integer type;
    public final String content;

    WebSocketMessageActionEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
}
