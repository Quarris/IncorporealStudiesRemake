package quarris.incstu.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.mutable.MutableInt;
import quarris.incstu.capability.Soul;
import quarris.incstu.event.ClientEvents;
import quarris.incstu.mechanics.soul.SoulType;
import quarris.incstu.util.ColorHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @Override
    public void init(FMLInitializationEvent e) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

    @Override
    public void calcSoulColor(Soul soul) {
        if (soul.getSoulTypes().size() > 0) {
            if (soul.getSoulTypes().size() == 1) {
                soul.color = soul.getSoulTypes().keySet().iterator().next().getColor();
            }
            List<Integer> colors = new LinkedList<>();
            for (Map.Entry<SoulType, MutableInt> entry : soul.getSoulTypes().entrySet()) {
                colors.add(entry.getKey().getColor());
            }
            soul.color = ColorHelper.blend(colors);
        }
    }
}
