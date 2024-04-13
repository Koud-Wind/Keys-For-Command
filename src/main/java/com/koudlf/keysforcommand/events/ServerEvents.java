package com.koudlf.keysforcommand.events;

import com.koudlf.keysforcommand.KeyTranslator;
import com.koudlf.keysforcommand.network.PacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ServerEvents {

    public static final Map<Integer, Objective> serverKeys = new HashMap<>();
    public static final Map<Integer, Objective> serverMouse = new HashMap<>();
    public static final Map<Integer, Objective> serverOther = new HashMap<>();

    private MinecraftServer server;

    @SubscribeEvent
    public void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

        server = player.server;

        update(player.server);
        PacketHandler.sendToClient(serverKeys.keySet(), serverMouse.keySet(), serverOther.keySet(), player);
    }

    @SubscribeEvent
    public void onReloadServer(AddReloadListenerEvent event) {
        if (server == null)
            return;
        update(server);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PacketHandler.sendToClient(serverKeys.keySet(), serverMouse.keySet(), serverOther.keySet(), player);
        }
    }

    private void update(MinecraftServer server) {
        serverKeys.clear();
        serverMouse.clear();
        serverOther.clear();

        for (Objective s : server.getScoreboard().getObjectives()) {
            String name = s.getName().toLowerCase();
            if (name.startsWith("key_")) {
                serverKeys.put(KeyTranslator.translateKeyCode(name.substring(4)), s);
            } else if (name.startsWith("mouse_")) {
                serverMouse.put(KeyTranslator.translateMouseCode(name.substring(6)), s);
            } else if (name.startsWith("other_")) {
                serverOther.put(KeyTranslator.translateOtherCode(name.substring(6)), s);
            }
        }
    }

}
