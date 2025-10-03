package win.cuteguimc.zombieshelper.listener;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemSword;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

public class AutoSendListener {
    private final Minecraft mc = Minecraft.getMinecraft();
    private int tickCounter = 0;

    public AutoSendListener() {
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!ZombiesHelperConfig.autoSend) return;

        tickCounter++;
        if (tickCounter < 7) return;
        tickCounter = 0;

        if (mc.thePlayer.inventory.getCurrentItem() != null) {
            if (!(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword || 
                  mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBook)) {

                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            }
        }
    }
}