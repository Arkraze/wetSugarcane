package ark.sugarwater.wetsugarcane;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class WetSugarcaneClient implements ClientModInitializer {
	public void onInitializeClient () {
		BlockRenderLayerMap.INSTANCE.putBlock(WetSugarcane.WATERWEED, RenderLayer.getCutout());
	}
}
