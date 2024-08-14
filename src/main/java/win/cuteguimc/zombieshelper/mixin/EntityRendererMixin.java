package win.cuteguimc.zombieshelper.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.utils.RenderUtils;
import win.cuteguimc.zombieshelper.utils.Utils;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        try {
            if (ZombiesHelperConfig.esp) {
                Color targetcolor = new Color(56, 199, 231);
                Color color = new Color(255, 43, 28);
                float[] colors = new float[]{(float) color.getRed(), color.getGreen(), color.getBlue()};
                for (Entity entity : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
                    if (Utils.isTarget(entity) && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= ZombiesHelperConfig.espRange) {
                        RenderUtils.drawEntityBox(entity, (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= 8) ? color : targetcolor);
                    }
                    if (Utils.isPowerUP(entity)) {
                        RenderUtils.drawEntityBox(entity, Objects.requireNonNull(Utils.getPowerUPColor(entity)));
                    }
                }

                for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(entity -> Utils.isTarget(entity) && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= 8).collect(Collectors.toList())) {
                    RenderUtils.drawTracert(entity, colors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
