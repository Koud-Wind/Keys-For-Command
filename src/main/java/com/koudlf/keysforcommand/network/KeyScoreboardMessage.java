package com.koudlf.keysforcommand.network;

import com.koudlf.keysforcommand.events.ClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyScoreboardMessage {
    private final Set<Integer> serverKeys;
    private final Set<Integer> serverMouse;
    private final Set<Integer> serverOther;
    private final boolean keyPressDebug;

    public KeyScoreboardMessage(Set<Integer> serverKeys, Set<Integer> serverMouse, Set<Integer> serverOther, boolean keyPressDebug) {
        this.serverKeys = serverKeys;
        this.serverMouse = serverMouse;
        this.serverOther = serverOther;
        this.keyPressDebug = keyPressDebug;
    }

    public static void encode(KeyScoreboardMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.serverKeys.size());
        for (int key : message.serverKeys) {
            buffer.writeInt(key);
        }
        buffer.writeInt(message.serverMouse.size());
        for (int key : message.serverMouse) {
            buffer.writeInt(key);
        }
        buffer.writeInt(message.serverOther.size());
        for (int key : message.serverOther) {
            buffer.writeInt(key);
        }
        buffer.writeBoolean(message.keyPressDebug);
    }

    public static KeyScoreboardMessage decode(FriendlyByteBuf buffer) {
        int keysSize = buffer.readInt();
        Set<Integer> keys = new HashSet<>();
        for (int i = 0; i < keysSize; i++) {
            keys.add(buffer.readInt());
        }
        int mouseSize = buffer.readInt();
        Set<Integer> mouse = new HashSet<>();
        for (int i = 0; i < mouseSize; i++) {
            mouse.add(buffer.readInt());
        }
        int otherSize = buffer.readInt();
        Set<Integer> other = new HashSet<>();
        for (int i = 0; i < otherSize; i++) {
            other.add(buffer.readInt());
        }
        boolean keyPressDebug = buffer.readBoolean();
        return new KeyScoreboardMessage(keys, mouse, other, keyPressDebug);
    }

    public static void handle(KeyScoreboardMessage message, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ClientEvents.clientKeys = message.serverKeys;
            ClientEvents.clientMouse = message.serverMouse;
            ClientEvents.clientOther = message.serverOther;
            ClientEvents.keyPressDebug = message.keyPressDebug;
        });
        ctx.setPacketHandled(true);
    }

}
