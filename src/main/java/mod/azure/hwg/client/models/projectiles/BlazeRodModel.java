package mod.azure.hwg.client.models.projectiles;

import mod.azure.hwg.HWGMod;
import mod.azure.hwg.entity.projectiles.BlazeRodEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BlazeRodModel extends AnimatedGeoModel<BlazeRodEntity> {
	@Override
	public Identifier getModelLocation(BlazeRodEntity object) {
		return new Identifier(HWGMod.MODID, "geo/blaze_rod_projectile.geo.json");
	}

	@Override
	public Identifier getTextureLocation(BlazeRodEntity object) {
		return new Identifier(HWGMod.MODID, "textures/items/projectiles/blaze_rod_projectile.png");
	}

	@Override
	public Identifier getAnimationFileLocation(BlazeRodEntity animatable) {
		return new Identifier(HWGMod.MODID, "animations/bullet.animation.json");
	}
}
