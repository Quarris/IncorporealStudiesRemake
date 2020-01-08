package quarris.incstu;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import quarris.incstu.proxy.IProxy;

@Mod(modid = ModRef.ID, name = ModRef.NAME, version = ModRef.VERSION)
public class IncorporealStudies {

    @SidedProxy(clientSide = "quarris.incstu.proxy.ClientProxy", serverSide = "quarris.incstu.proxy.ServerProxy")
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInitDef(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.initDef(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInitDef(e);
    }
}
