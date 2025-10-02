package win.cuteguimc.zombieshelper.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.utils.RenderUtils;
import win.cuteguimc.zombieshelper.utils.Utils;

import java.awt.*;
import java.util.Objects;

import static win.cuteguimc.zombieshelper.listener.AutoReviveListener.reviveablePlayers;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        try {
            if (ZombiesHelperConfig.esp) {
                Color targetcolor = new Color(56, 199, 231);
                Color color = new Color(255, 43, 28);
                Color reviveableColor = Color.ORANGE;
                for (Entity entity : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
                    boolean isReviveable;
                    synchronized (reviveablePlayers) {
                        isReviveable = reviveablePlayers.stream().anyMatch(entityPlayer -> entityPlayer.getEntityId() == entity.getEntityId());
                    }
                    if (Utils.isTarget(entity) && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= ZombiesHelperConfig.espRange) {
                        RenderUtils.drawEntityBox(entity, (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= 8) ? color : targetcolor);
                    }
                    if (isReviveable) {
                        float[] rgb = new float[3];
                        rgb[0] = reviveableColor.getRed();
                        rgb[1] = reviveableColor.getGreen();
                        rgb[2] = reviveableColor.getBlue();
                        RenderUtils.drawEntityBox(entity, reviveableColor);
                        RenderUtils.drawTracert(entity, rgb);
                    }
                    if (Utils.isPowerUP(entity)) {
                        RenderUtils.drawBiggerEntityBox(entity, Objects.requireNonNull(Utils.getPowerUPColor(entity)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
