package win.cuteguimc.zombieshelper.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;

public class Utils {
    public static boolean isTarget(Entity entity) {
        return entity instanceof IAnimals && !(entity instanceof EntityVillager);
    }
}
