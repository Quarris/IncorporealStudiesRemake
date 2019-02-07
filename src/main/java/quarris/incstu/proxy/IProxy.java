package quarris.incstu.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

    void preInit(FMLPreInitializationEvent e);

    void init(FMLInitializationEvent e);

    void postInit(FMLPostInitializationEvent e);

    default void preInitDef(FMLPreInitializationEvent e) {
        preInit(e);
    }

    default void initDef(FMLInitializationEvent e) {
        init(e);
    }

    default void postInitDef(FMLPostInitializationEvent e) {
        postInit(e);
    }
}
