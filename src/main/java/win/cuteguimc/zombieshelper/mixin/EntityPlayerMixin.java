package win.cuteguimc.zombieshelper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
    @Inject(method = "isInvisibleToPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z", at = @At("RETURN"), cancellable = true)
    private void onIsInvisibleToPlayer(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (ZombiesHelperConfig.hidePlayer) {
            cir.setReturnValue(false);
        }
    }
}
