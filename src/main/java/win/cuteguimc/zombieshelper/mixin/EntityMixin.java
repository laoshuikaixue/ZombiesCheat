package win.cuteguimc.zombieshelper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isInvisible()Z", at = @At("RETURN"), cancellable = true)
    private void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        if (((Entity) (Object) this) instanceof EntityPlayer && ZombiesHelperConfig.hidePlayer) {
            cir.setReturnValue(true);
        }
    }
}
