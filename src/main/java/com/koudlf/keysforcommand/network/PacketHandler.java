package com.koudlf.keysforcommand.network;

import com.koudlf.keysforcommand.KeysForCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.Objects;
import java.util.Set;

import static com.koudlf.keysforcommand.KeysForCommand.CHANNEL;

public class PacketHandler {

    public static void registerMessages() {
        CHANNEL.messageBuilder(PressedMessage.class)
                .encoder(PressedMessage::encode)
                .decoder(PressedMessage::decode)
                .consumerMainThread(PressedMessage::handle)
                .add();
        CHANNEL.messageBuilder(KeyScoreboardMessage.class)
                .encoder(KeyScoreboardMessage::encode)
                .decoder(KeyScoreboardMessage::decode)
                .consumerMainThread(KeyScoreboardMessage::handle)
                .add();
    }

    public static void sendToServer(int opCode, int inputMode) {
        CHANNEL.send(new PressedMessage(opCode, inputMode), PacketDistributor.SERVER.noArg());
    }

    public static void sendToClient(Set<Integer> keys, Set<Integer> mouse, Set<Integer> other, ServerPlayer player) {
        boolean keyPressDebug = Objects.requireNonNull(player.getServer()).getWorldData().getGameRules().getBoolean(KeysForCommand.KEYPRESSDEBUG);
        CHANNEL.send(new KeyScoreboardMessage(keys, mouse, other, keyPressDebug), PacketDistributor.PLAYER.with(player));
    }
}
