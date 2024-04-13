package com.koudlf.keysforcommand.network;

import com.koudlf.keysforcommand.KeyTranslator;
import com.koudlf.keysforcommand.KeysForCommand;
import com.koudlf.keysforcommand.events.ServerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PressedMessage {

    public static final int KEY = 0;
    public static final int MOUSE = 1;
    public static final int OTHER = 2;

    private final int keyOpCode;
    private final int inputMode;

    public PressedMessage(int keysOpCode, int inputMode) {
        this.keyOpCode = keysOpCode;
        this.inputMode = inputMode;
    }

    public static void encode(PressedMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.keyOpCode);
        buffer.writeInt(message.inputMode);
    }

    public static PressedMessage decode(FriendlyByteBuf buffer) {
        return new PressedMessage(buffer.readInt(), buffer.readInt());
    }

    public static void handle(PressedMessage message, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            String opCode;
            Objective objective = null;
            Player player = ctx.getSender();

            if (player == null)
                return;

            switch (message.inputMode) {
                case KEY -> {
                    opCode = "key_" + KeyTranslator.translateKeyCode(message.keyOpCode);
                    objective = ServerEvents.serverKeys.get(message.keyOpCode);
                }
                case MOUSE -> {
                    opCode = "mouse_" + KeyTranslator.translateMouseCode(message.keyOpCode);
                    objective = ServerEvents.serverMouse.get(message.keyOpCode);
                }
                case OTHER -> {
                    opCode = "other_" + KeyTranslator.translateOtherCode(message.keyOpCode);
                    objective = ServerEvents.serverOther.get(message.keyOpCode);
                }
                default -> opCode = "unknown";
            }

            if (player.getServer().getWorldData().getGameRules().getBoolean(KeysForCommand.KEYPRESSDEBUG))
                player.sendSystemMessage(player.getDisplayName().copy().append(" pressed: " + opCode));

            if (objective != null)
                player.getScoreboard().getOrCreatePlayerScore(player, objective).add(1);
        });
        ctx.setPacketHandled(true);
    }
}
