package com.teamabnormals.abnormals_core.core.examples;

import com.teamabnormals.abnormals_core.common.blocks.AbnormalsBeehiveBlock;
import com.teamabnormals.abnormals_core.common.blocks.chest.AbnormalsChestBlock;
import com.teamabnormals.abnormals_core.common.blocks.chest.AbnormalsTrappedChestBlock;
import com.teamabnormals.abnormals_core.common.blocks.sign.ITexturedSign;
import com.teamabnormals.abnormals_core.common.tileentity.*;
import com.teamabnormals.abnormals_core.core.AbnormalsCore;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AbnormalsCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExampleTileEntityRegistry {
	public static final RegistryHelper HELPER = AbnormalsCore.REGISTRY_HELPER;

	public static final RegistryObject<TileEntityType<AbnormalsSignTileEntity>> SIGN = HELPER.createTileEntity("sign", AbnormalsSignTileEntity::new, () -> collectBlocks(ITexturedSign.class));
	public static final RegistryObject<TileEntityType<AbnormalsBeehiveTileEntity>> BEEHIVE = HELPER.createTileEntity("beehive", AbnormalsBeehiveTileEntity::new, () -> collectBlocks(AbnormalsBeehiveBlock.class));
	public static final RegistryObject<TileEntityType<AbnormalsChestTileEntity>> CHEST = HELPER.createTileEntity("chest", AbnormalsChestTileEntity::new, () -> collectBlocks(AbnormalsChestBlock.class));
	public static final RegistryObject<TileEntityType<AbnormalsTrappedChestTileEntity>> TRAPPED_CHEST = HELPER.createTileEntity("trapped_chest", AbnormalsTrappedChestTileEntity::new, () -> collectBlocks(AbnormalsTrappedChestBlock.class));

	private static Block[] collectBlocks(Class<?> blockClass) {
		return ForgeRegistries.BLOCKS.getValues().stream().filter(blockClass::isInstance).toArray(Block[]::new);
	}
}