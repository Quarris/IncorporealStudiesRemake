package quarris.incstu.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import quarris.incstu.ModRef;
import quarris.incstu.capability.ModCaps;
import quarris.incstu.capability.Soul;
import quarris.incstu.event.InitEvents;
import quarris.incstu.registry.SoulRegistry;

public interface IProxy {

    void preInit(FMLPreInitializationEvent e);

    void init(FMLInitializationEvent e);

    void postInit(FMLPostInitializationEvent e);

    default void preInitDef(FMLPreInitializationEvent e) {
        ModRef.logger = e.getModLog();
        preInit(e);
        ModCaps.initCaps();
        SoulRegistry.init();
        MinecraftForge.EVENT_BUS.register(new InitEvents());
    }

    default void initDef(FMLInitializationEvent e) {
        init(e);
    }

    default void postInitDef(FMLPostInitializationEvent e) {
        postInit(e);
    }

    void calcSoulColor(Soul soul);
}
