package win.cuteguimc.zombieshelper.listener;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

public class AutoSwitchListener {
    private final Minecraft mc = Minecraft.getMinecraft();

    public AutoSwitchListener() {
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onTickEvent(TickEvent event) {
        if (!ZombiesHelperConfig.autoSwitch) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.thePlayer.ticksExisted % 2 == 0) {
            mc.thePlayer.inventory.currentItem = (int) ZombiesHelperConfig.firstSlot - 1;
        } else {
            mc.thePlayer.inventory.currentItem = (int) ZombiesHelperConfig.secondSlot - 1;
        }
        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
    }
}
