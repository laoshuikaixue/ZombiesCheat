package win.cuteguimc.zombieshelper.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import win.cuteguimc.zombieshelper.mixin.MinecraftAccessor;

public class RotationUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float getYawToPoint(double posX, double posZ) {
        Minecraft instance = mc;
        double xDiff = posX - (instance.thePlayer.lastTickPosX + (instance.thePlayer.posX - instance.thePlayer.lastTickPosX) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks), zDiff = posZ - (instance.thePlayer.lastTickPosZ + (instance.thePlayer.posZ - instance.thePlayer.lastTickPosZ) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks), dist = MathHelper.sqrt_double(
                xDiff * xDiff + zDiff * zDiff);
        return (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
    }
}
