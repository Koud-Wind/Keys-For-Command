package com.koudlf.keysforcommand;

import com.koudlf.keysforcommand.events.ClientEvents;
import com.koudlf.keysforcommand.events.ServerEvents;
import com.koudlf.keysforcommand.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

@Mod(KeysForCommand.MODID)
public class KeysForCommand {

    public static final String MODID = "keys_for_command";

    public static final GameRules.Key<GameRules.BooleanValue> KEYPRESSDEBUG = GameRules.register("keyPressDebug", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

    public static final SimpleChannel CHANNEL = ChannelBuilder.named(new ResourceLocation(MODID, MODID))
            .optionalServer()
            .simpleChannel();

    public KeysForCommand() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        //modEventBus.addListener(this::commonSetup);
        eventBus.register(this);
        eventBus.register(new ServerEvents());

        //STAT_TYPES.register(modEventBus);
        PacketHandler.registerMessages();
    }

    //private void commonSetup(final FMLCommonSetupEvent event) {
    //    CriteriaTriggers.register("key", new KeysTrigger());
    //}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        event.getServer().getWorldData().getGameRules().getBoolean(KEYPRESSDEBUG);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }
    }

}
