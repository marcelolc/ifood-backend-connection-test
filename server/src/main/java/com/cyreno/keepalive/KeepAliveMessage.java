package com.cyreno.keepalive;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class KeepAliveMessage {

    private Long restaurantId;

    public KeepAliveMessage(@NotNull long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public static KeepAliveMessage fromBytes(byte[] bytes) {
        Long restaurantId = Long.parseLong(new String(bytes));
        return new KeepAliveMessage(restaurantId);
    }

    public byte[] toBytes() {
        return Long.toString(restaurantId).getBytes();
    }

}
