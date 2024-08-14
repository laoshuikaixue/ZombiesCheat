package win.cuteguimc.zombieshelper.listener;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.SendPacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.utils.Utils;

public class BlockUseEntityListener {
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean cancelNextSwing = false;

    public BlockUseEntityListener() {
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onPacketSend(SendPacketEvent event) {
        if (!ZombiesHelperConfig.blockUseEntity) return;
        Packet packet = event.packet;
        if ((packet instanceof C08PacketPlayerBlockPlacement &&
                mc.theWorld.getBlockState(((C08PacketPlayerBlockPlacement) packet).getPosition()).getBlock() instanceof BlockChest) ||
                packet instanceof C02PacketUseEntity && !Utils.isTarget(((C02PacketUseEntity) packet).getEntityFromWorld(mc.theWorld))) {
            event.isCancelled = true;
            if (mc.thePlayer.inventory.getCurrentItem().getItem().getItemUseAction(mc.thePlayer.inventory.getCurrentItem()) != EnumAction.NONE) {
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                if (packet instanceof C08PacketPlayerBlockPlacement) {
                    cancelNextSwing = true;
                }
            }
        } else if (packet instanceof C0APacketAnimation && cancelNextSwing) {
            cancelNextSwing = false;
            event.isCancelled = true;
        }
    }
}
