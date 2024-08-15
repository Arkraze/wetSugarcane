package ark.sugarwater.wetsugarcane;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class WetSugarcane implements ModInitializer {
	public static final String MODID = "wetsugarcane";
	
	public static final Block WATERWEED = Registry.register(Registries.BLOCK,
			id("waterweed"),
			new WaterweedBlock(FabricBlockSettings.copyOf(Blocks.PINK_PETALS).sounds(BlockSoundGroup.MOSS_BLOCK).nonOpaque()));
	public static final Item WATERWEED_ITEM = Registry.register(Registries.ITEM,
			id("waterweed"),
			new AmphibiousPlaceableItem(WATERWEED, new Item.Settings()));
	
	
	public static Identifier id (String name) {
		return Identifier.of(MODID, name);
	}
	
	@Override public void onInitialize () {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.addAfter(Items.PINK_PETALS, WATERWEED_ITEM));
		CompostingChanceRegistry.INSTANCE.add(WATERWEED_ITEM, .3f);
	}
}
