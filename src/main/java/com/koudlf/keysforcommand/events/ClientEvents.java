package com.koudlf.keysforcommand.events;

import com.koudlf.keysforcommand.network.PacketHandler;
import com.koudlf.keysforcommand.network.PressedMessage;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static com.koudlf.keysforcommand.network.PressedMessage.MOUSE;
import static com.koudlf.keysforcommand.network.PressedMessage.OTHER;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    public static Set<Integer> clientKeys = new HashSet<>();
    public static Set<Integer> clientMouse = new HashSet<>();
    public static Set<Integer> clientOther = new HashSet<>();

    public static boolean keyPressDebug = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (hasGui() || event.getAction() == InputConstants.RELEASE || (!keyPressDebug && !clientKeys.contains(event.getKey())))
            return;
        PacketHandler.sendToServer(event.getKey(), PressedMessage.KEY);
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton event) {
        if (hasGui() || event.getAction() == InputConstants.RELEASE || (!keyPressDebug && !clientMouse.contains(event.getButton())))
            return;
        PacketHandler.sendToServer(event.getButton(), MOUSE);
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseScrollingEvent event) {
        if (hasGui() || (!keyPressDebug && !clientOther.contains(1)))
            return;
        PacketHandler.sendToServer(1, OTHER);
    }

    private boolean hasGui() {
        return Minecraft.getInstance().screen != null;
    }
}