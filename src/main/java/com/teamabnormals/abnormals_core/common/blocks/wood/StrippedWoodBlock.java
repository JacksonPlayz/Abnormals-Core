package com.teamabnormals.abnormals_core.common.blocks.wood;

import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;

public class StrippedWoodBlock extends RotatedPillarBlock {
	public StrippedWoodBlock(Properties properties) {
        super(properties);
    }
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		ItemStackUtils.fillAfterItemForGroup(this.asItem(), Items.STRIPPED_WARPED_HYPHAE, group, items);
	}
}
