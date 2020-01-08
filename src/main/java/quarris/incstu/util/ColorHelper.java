package quarris.incstu.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ColorHelper {

    /**
     * Originally created by Ellpeck.
     * Slightly modified by Quarris.
     *
     * Returns a hex representation of a color which cycles based on the given time.
     * The time given should be changing, for example {@link World#getTotalWorldTime()}
     * You can imagine it as a circle spectrum of colors and the time (mod 255) being the position on the circle.
     *
     * @param time The position at which the rainbow is at.
     * @return The hex representation of the rainbow position with respective of given time.
     */
    public static int rainbow(int time) {
        if (time > 255) {
            time = time % 255;
        }
        if (time < 85F) {
            return rgb((time * 3F) / 255F, (255F - time * 3F) / 255F, 0F);
        }
        else if (time < 170F) {
            return rgb((255F - (time -= 85F) * 3F) / 255F, 0F, (time * 3F) / 255F);
        }
        else {
            return rgb(0F, ((time -= 170F) * 3F) / 255F, (255F - time * 3F) / 255F);
        }
    }

    /**
     * Does an average blend of all the colors.
     * @param colors the colors to blend.
     * @return the blended color.
     */
    public static int blend(int... colors) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        for (int i : colors) {
            sumR += i >> 16 & 0xff;
            sumG += i >> 8 & 0xff;
            sumB += i & 0xff;
        }
        return rgb(sumR/colors.length, sumG/colors.length, sumB/colors.length);
    }

    /**
     * @see ColorHelper#blend(int...)
     */
    public static int blend(List<Integer> colors) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        for (int i : colors) {
            sumR += i >> 16 & 0xff;
            sumG += i >> 8 & 0xff;
            sumB += i & 0xff;
        }
        return rgb(sumR/colors.size(), sumG/colors.size(), sumB/colors.size());
    }

    public static int brightness(int color, float mult) {
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;

        r *= mult;
        g *= mult;
        b *= mult;

        return rgb(r, g, b);
    }

    /**
     * Brightens the given color.
     */
    public static int lighter(int color) {
        return lighter(color, 0.1F);
    }

    public static int lighter(int color, float mult) {
        if (mult < 0) {
            return color;
        }
        mult += 1;
        return brightness(color, mult);
    }

    /**
     * Darkens the given color.
     */
    public static int darker(int color) {
        return darker(color, 0.1F);
    }

    public static int darker(int color, float mult) {
        if (mult < 0) {
            return color;
        }
        return brightness(color, 1/mult);
    }

    private static int rgb(float r, float g, float b) {
        return MathHelper.rgb(r, g, b);
    }

    private static int rgb(int r, int g, int b) {
        return MathHelper.rgb(r, g, b);
    }
}
