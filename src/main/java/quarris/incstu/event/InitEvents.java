package quarris.incstu.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quarris.incstu.ModRef;
import quarris.incstu.capability.Soul;

public class InitEvents {

    @SubscribeEvent
    public void doCapabilities(AttachCapabilitiesEvent<Entity> e) {
        e.addCapability(ModRef.createRes("soul"), new Soul(e.getObject()));
    }

}
