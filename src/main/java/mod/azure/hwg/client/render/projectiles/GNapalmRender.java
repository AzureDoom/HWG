package mod.azure.hwg.client.render.projectiles;

import mod.azure.hwg.client.models.projectiles.GNapalmModel;
import mod.azure.hwg.entity.projectiles.NapalmGrenadeEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderer.geo.GeoProjectilesRenderer;

public class GNapalmRender extends GeoProjectilesRenderer<NapalmGrenadeEntity> {

	public GNapalmRender(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn, new GNapalmModel());
	}

	protected int getBlockLight(NapalmGrenadeEntity entityIn, BlockPos partialTicks) {
		return 15;
	}

	@Override
	public RenderLayer getRenderType(NapalmGrenadeEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}

}