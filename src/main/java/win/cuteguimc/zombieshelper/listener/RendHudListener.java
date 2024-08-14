package win.cuteguimc.zombieshelper.listener;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.HudRenderEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.mixin.MinecraftAccessor;
import win.cuteguimc.zombieshelper.utils.RenderUtils;
import win.cuteguimc.zombieshelper.utils.RotationUtils;
import win.cuteguimc.zombieshelper.utils.Utils;

import java.awt.*;
import java.util.Comparator;
import java.util.stream.Collectors;

import static java.lang.Math.toRadians;
import static net.minecraft.util.MathHelper.*;
import static win.cuteguimc.zombieshelper.utils.RotationUtils.getYawToPoint;

public class RendHudListener {
    private static final int HALF_PI_DEGREES_INT = 90;
    private static final float TWO_PI_DEGREES_FLOAT = 360.0F;
    private static final float RGB_MAX_FLOAT = 255.0F;
    private static double WIDTH = 0.03D;
    private static double HEIGHT = 5;

    public RendHudListener() {
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onRenderHudEvent(HudRenderEvent event) {
        if (!ZombiesHelperConfig.esp) return;
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;
        float playerYaw = player.rotationYaw;
        WIDTH = 0.03D * (135D / ZombiesHelperConfig.espArrowRadius);

        ScaledResolution resolution = new ScaledResolution(minecraft);
        double width = resolution.getScaledWidth();
        double height = resolution.getScaledHeight();
        double centerX = width / 2.0D;
        double centerY = height / 2.0D;


        int radius = (int) ZombiesHelperConfig.espArrowRadius;
        Color color = new Color(255, 43, 28);

        //endregion

        for (Entity entity : minecraft.theWorld.getLoadedEntityList().stream().sorted(Comparator.comparingDouble(entity ->
                toRadians(((
                        getYawToPoint(
                                entity.posX, entity.posZ) +
                                TWO_PI_DEGREES_FLOAT) %
                        TWO_PI_DEGREES_FLOAT - playerYaw +
                        TWO_PI_DEGREES_FLOAT) %
                        TWO_PI_DEGREES_FLOAT -
                        HALF_PI_DEGREES_INT)
        )).collect(Collectors.toList())) {
            if (!Utils.isTarget(entity)) continue;
            if (minecraft.thePlayer.getDistanceToEntity(entity) > 8) continue;

            double distEntity2Player = entity.getDistanceToEntity(player);

            RenderUtils.start2D();
            GL11.glPushMatrix();
            switch (minecraft.gameSettings.guiScale){
                case 0:
                    GlStateManager.scale(0.5,0.5,0.5);
                    break;
                case 1:
                    GlStateManager.scale(2,2,2);
                    break;
                case 3:
                    GlStateManager.scale(0.6666666666666667,0.6666666666666667,0.6666666666666667);

            }
            GL11.glLineWidth(4);

            float alpha = 1.0F - clamp_float(color.getAlpha() / RGB_MAX_FLOAT * (float) distEntity2Player * 3.0F, 0, 255) / RGB_MAX_FLOAT;


            Color temp = color;

            float red = (temp.getRGB() >> 16 & 0xFF) / RGB_MAX_FLOAT;
            float green = (temp.getRGB() >> 8 & 0xFF) / RGB_MAX_FLOAT;
            float blue = (temp.getRGB() & 0xFF) / RGB_MAX_FLOAT;

            GL11.glColor4f(red, green, blue, alpha);
            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

            float yawToEntity = (RotationUtils.getYawToPoint(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((MinecraftAccessor) minecraft).getTimer().elapsedPartialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((MinecraftAccessor) minecraft).getTimer().elapsedPartialTicks) + TWO_PI_DEGREES_FLOAT) % TWO_PI_DEGREES_FLOAT;
            float yawDiff = (yawToEntity - playerYaw + TWO_PI_DEGREES_FLOAT) % TWO_PI_DEGREES_FLOAT - HALF_PI_DEGREES_INT;
            double yawDiffRad = toRadians(yawDiff);

            GL11.glVertex2d(centerX + cos((float) yawDiffRad) * radius, centerY + sin((float) yawDiffRad) * radius);
            GL11.glVertex2d(centerX + cos((float) (yawDiffRad + WIDTH)) * (radius - HEIGHT), centerY + sin((float) (yawDiffRad + WIDTH)) * (radius - HEIGHT));
            GL11.glVertex2d(centerX + cos((float) (yawDiffRad - WIDTH)) * (radius - HEIGHT), centerY + sin((float) (yawDiffRad - WIDTH)) * (radius - HEIGHT));
            GL11.glVertex2d(centerX + cos((float) yawDiffRad) * radius, centerY + sin((float) yawDiffRad) * radius);

            GL11.glEnd();
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glPopMatrix();
            RenderUtils.stop2D();
        }
    }
}
