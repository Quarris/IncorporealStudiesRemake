package quarris.incstu.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import quarris.incstu.capability.ModCaps;
import quarris.incstu.capability.Soul;

import java.util.List;

public class ClientEvents {

    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent e) {
        EntityPlayer p = Minecraft.getMinecraft().player;
        if (p != null && p.getHeldItemMainhand().getItem() == Items.STICK) {
            renderStuff(e.getPartialTicks(), 8);
        }
    }

    public void renderStuff(float partialTicks, double alphaDist) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        EntityPlayer player = mc.player;

        if (player != null) {
            float x = ActiveRenderInfo.getRotationX();
            float z = ActiveRenderInfo.getRotationZ();
            float yz = ActiveRenderInfo.getRotationYZ();
            float xy = ActiveRenderInfo.getRotationXY();
            float xz = ActiveRenderInfo.getRotationXZ();

            double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            Particle.cameraViewDir = player.getLook(partialTicks);

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.003921569F);
            GlStateManager.disableCull();
            GlStateManager.depthMask(false);
            mc.getTextureManager().bindTexture(Soul.SOUL_TEX);
            Tessellator tessy = Tessellator.getInstance();
            BufferBuilder buffer = tessy.getBuffer();

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableDepth();


            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            AxisAlignedBB box = new AxisAlignedBB(player.getPosition());
            Entity renderer = player;
            if (mc.gameSettings.thirdPersonView > 0) {
                renderer = null;
            }
            List<Entity> list = world.getEntitiesInAABBexcluding(renderer, box.grow(32), null);

            {
                int alpha;
                int col;
                double dist;
                for (Entity e : list) {
                    alpha = 255;
                    col = ModCaps.getSoul(e).getColor();
                    dist = Minecraft.getMinecraft().player.getDistance(e);
                    if (dist > alphaDist) {
                        dist -= alphaDist;
                        alpha = (int) (Math.max(0, 1 - dist / alphaDist) * 255);
                    }
                    if (alpha != 0) {
                        renderSoul(buffer, e, partialTicks, x, xz, z, yz, xy, interpPosX, interpPosY, interpPosZ, col, alpha, ModCaps.getSoul(e).isVacuum());
                    }
                }
            }

            tessy.draw();
            GlStateManager.enableDepth();

            GlStateManager.enableCull();
            GlStateManager.depthMask(true);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);

            GlStateManager.popMatrix();
        }
    }

    public void renderSoul(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, double interpPosX, double interpPosY, double interpPosZ, int color, int alpha, boolean vacuum) {
        double x = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks - interpPosX;
        double y = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks - interpPosY + entityIn.height/2;
        double z = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks - interpPosZ;
        float sc = 0.3F;
        
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        int brightness = (6 << 20) | (6 << 4);
        int sky = brightness >> 16 & 0xFFFF;
        int block = brightness & 0xFFFF;

        double v = vacuum ? 0.5D : 0;

        buffer.pos(x + (-rotationX * sc - rotationXY * sc), y + -rotationZ * sc, z + (-rotationYZ * sc - rotationXZ * sc))
                .tex(0.5D, v+0.5D).color(red, green, blue, alpha)
                .lightmap(sky, block).endVertex();
        buffer.pos(x + (-rotationX * sc + rotationXY * sc), y + (rotationZ * sc), z + (-rotationYZ * sc + rotationXZ * sc))
                .tex(0.5D, v).color(red, green, blue, alpha)
                .lightmap(sky, block).endVertex();
        buffer.pos(x + (rotationX * sc + rotationXY * sc), y + (rotationZ * sc), z + (rotationYZ * sc + rotationXZ * sc))
                .tex(0, v).color(red, green, blue, alpha)
                .lightmap(sky, block).endVertex();
        buffer.pos(x + (rotationX * sc - rotationXY * sc), y + (-rotationZ * sc), z + (rotationYZ * sc - rotationXZ * sc))
                .tex(0, v+0.5D).color(red, green, blue, alpha)
                .lightmap(sky, block).endVertex();
    }
}
