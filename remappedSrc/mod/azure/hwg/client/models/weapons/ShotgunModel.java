package mod.azure.hwg.client.models.weapons;

import mod.azure.hwg.HWGMod;
import mod.azure.hwg.item.weapons.ShotgunItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShotgunModel extends AnimatedGeoModel<ShotgunItem> {
	@Override
	public Identifier getModelLocation(ShotgunItem object) {
		return new Identifier(HWGMod.MODID, "geo/shotgun.geo.json");
	}

	@Override
	public Identifier getTextureLocation(ShotgunItem object) {
		return new Identifier(HWGMod.MODID, "textures/items/shotgun.png");
	}

	@Override
	public Identifier getAnimationFileLocation(ShotgunItem animatable) {
		return new Identifier(HWGMod.MODID, "animations/shotgun.animation.json");
	}
}
