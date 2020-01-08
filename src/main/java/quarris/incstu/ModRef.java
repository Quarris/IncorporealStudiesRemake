package quarris.incstu;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import quarris.incstu.proxy.IProxy;

public class ModRef {

    public static final String ID = "incstu";
    public static final String NAME = "Incorporeal Studies";
    public static final String VERSION = "0.0.1";

    public static Logger logger;
    public static IProxy proxy = IncorporealStudies.proxy;
    public static final CreativeTabs TAB = new CreativeTabs(ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.SOUL_SAND);
        }
    };

    public static ResourceLocation createRes(String name) {
        return new ResourceLocation(ID, name);
    }

}