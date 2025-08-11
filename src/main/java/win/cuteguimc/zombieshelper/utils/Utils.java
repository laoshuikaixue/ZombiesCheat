package win.cuteguimc.zombieshelper.utils;

import kotlin.text.Regex;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;

import java.awt.*;
import java.util.Locale;

public class Utils {
    private static Regex regex = new Regex("(?:§.)*(SHOPPING SPREE|DOUBLE GOLD|MAX AMMO|INSTA KILL|CARPENTER|BONUS GOLD)");

    public static boolean isTarget(Entity entity) {
        return entity instanceof IAnimals && !(entity instanceof EntityWither) && entity instanceof EntityLivingBase && !(entity instanceof EntityVillager) && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0;
    }

    public static boolean isPowerUP(Entity entity) {
        return entity instanceof EntityArmorStand && regex.containsMatchIn(entity.getName());
    }

    public static Color getPowerUPColor(Entity entity) {
        Color color = Color.WHITE;
        if (!(entity instanceof EntityArmorStand)) return null;
        String name = entity.getName().toLowerCase(Locale.ROOT);
        if (name.contains("shopping")) color = new Color(106, 0, 255);
        else if (name.contains("double")) color = new Color(255, 217, 0);
        else if (name.contains("max ammo")) color = Color.BLUE;
        else if (name.contains("carpenter")) color = Color.GREEN;
        else if (name.contains("bonus")) color = new Color(255, 128, 0);
        return color;
    }
}
