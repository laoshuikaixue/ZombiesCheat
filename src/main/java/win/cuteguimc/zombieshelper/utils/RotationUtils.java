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

    public static float getPitchToPoint(double posX, double posY, double posZ) {
        Minecraft instance = mc;
        double xDiff = posX - (instance.thePlayer.lastTickPosX + (instance.thePlayer.posX - instance.thePlayer.lastTickPosX) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks);
        double yDiff = posY - (instance.thePlayer.lastTickPosY + (instance.thePlayer.posY - instance.thePlayer.lastTickPosY) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks) - instance.thePlayer.getEyeHeight();
        double zDiff = posZ - (instance.thePlayer.lastTickPosZ + (instance.thePlayer.posZ - instance.thePlayer.lastTickPosZ) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks);
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        return (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
    }

    public static float[] getRotationsToPoint(double posX, double posY, double posZ) {
        float yaw = getYawToPoint(posX, posZ);
        float pitch = getPitchToPoint(posX, posY, posZ);
        return new float[]{yaw, pitch};
    }

    public static double getAngleDifferenceToPoint(double posX, double posY, double posZ) {
        Minecraft instance = mc;
        double xDiff = posX - (instance.thePlayer.lastTickPosX + (instance.thePlayer.posX - instance.thePlayer.lastTickPosX) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks);
        double yDiff = posY - (instance.thePlayer.lastTickPosY + (instance.thePlayer.posY - instance.thePlayer.lastTickPosY) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks);
        double zDiff = posZ - (instance.thePlayer.lastTickPosZ + (instance.thePlayer.posZ - instance.thePlayer.lastTickPosZ) * ((MinecraftAccessor) instance).getTimer().elapsedPartialTicks);
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
}
