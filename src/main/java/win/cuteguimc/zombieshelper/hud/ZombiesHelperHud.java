package win.cuteguimc.zombieshelper.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import net.minecraft.client.Minecraft;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.utils.Utils;

import java.util.stream.Collectors;

public class ZombiesHelperHud extends SingleTextHud {
    public ZombiesHelperHud() {
        super("Nearby Entity", true);
    }

    @Override
    public String getText(boolean example) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return "World not loaded";
        else {
            int entityAmount = (int) Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(entity -> Utils.isTarget(entity) && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= 8).count();
            return entityAmount + "";
        }
    }
}
