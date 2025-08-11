package win.cuteguimc.zombieshelper.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

import java.util.HashMap;
import java.util.stream.Collectors;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class AutoReviveListener {
    public AutoReviveListener() {
        EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.getMinecraft();
    private HashMap<Integer, Integer> clickedPlayers = new HashMap<>();

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (event.player != mc.thePlayer) return;
        if (!ZombiesHelperConfig.autoRevive) return;
        if (mc.thePlayer.ticksExisted % 10 != 0) return; // Check every 10 ticks

        for (Entity entity : mc.theWorld.getLoadedEntityList().stream()
                .filter((e) -> e instanceof EntityPlayer)
                .filter(e -> e != mc.thePlayer)
                .collect(Collectors.toList())) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.isPlayerSleeping()) {
                clickedPlayers.remove(player.getEntityId());
                continue;
            }
            if (mc.thePlayer.getDistanceToEntity(player) > 5) continue;
            if (clickedPlayers.containsKey(player.getEntityId()) && clickedPlayers.get(player.getEntityId()) >= 10) {
                continue;
            }

            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(player, C02PacketUseEntity.Action.INTERACT));
            if (!clickedPlayers.containsKey(player.getEntityId())) {
                clickedPlayers.put(player.getEntityId(), 0);
                mc.thePlayer.addChatMessage(new ChatComponentText("Auto Reviving..."));
            }
            clickedPlayers.put(player.getEntityId(), clickedPlayers.get(player.getEntityId()) + 1);
        }
    }
}
