package ganymedes01.etfuturum.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow @Final public AxisAlignedBB boundingBox;
    private AxisAlignedBB etfu$savedBB;

    @Inject(method = "moveEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getCollidingBoundingBoxes(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;", ordinal = 0, shift = At.Shift.AFTER),
            slice = @Slice(from = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/Entity;stepHeight:F", ordinal = 1))
    )
    private void saveOldBoundingBox(double x, double y, double z, CallbackInfo ci) {
        etfu$savedBB = this.boundingBox.copy();
        /* TODO the Math.abs call doesn't exist in 1.12, but without it the box would be shrunk. Why don't they need it? */
        this.boundingBox.setBB(this.boundingBox.expand(Math.abs(x), 0, Math.abs(z)));
    }

    @Inject(method = "moveEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/AxisAlignedBB;", ordinal = 0, shift = At.Shift.BEFORE),
            slice = @Slice(from = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/Entity;stepHeight:F", ordinal = 1))
    )
    private void restoreOldBoundingBox(double x, double y, double z, CallbackInfo ci) {
        this.boundingBox.setBB(etfu$savedBB);
        etfu$savedBB = null;
    }
}
