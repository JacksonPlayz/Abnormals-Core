package com.teamabnormals.abnormals_core.core.util;

import com.teamabnormals.abnormals_core.client.screen.SlabfishHatScreen;

import net.minecraft.client.gui.screen.CustomizeSkinScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ACHooks {

	/**
	 * Called via ASM - customization_screen.js Transformer
	 */
	public static void addSlabfishButton(CustomizeSkinScreen screen, int i) {
		Method addButton = ObfuscationReflectionHelper.findMethod(Screen.class, "func_230480_a_", Widget.class);
		try {
			addButton.invoke(screen, new Button(screen.width / 2 - 155 + i % 2 * 160, screen.height / 6 + 24 * (i >> 1), 150, 20, new TranslationTextComponent("abnormals_core.screen.slabfish_settings"), (button) -> {
				screen.getMinecraft().displayGuiScreen(new SlabfishHatScreen(screen));
			}, (button, stack, mouseX, mouseY) -> screen.renderTooltip(stack, screen.getMinecraft().fontRenderer.trimStringToWidth(new TranslationTextComponent("abnormals_core.screen.slabfish_settings.tooltip", new StringTextComponent("patreon.com/teamabnormals").modifyStyle(style -> style.setColor(Color.fromHex("#FF424D")))), 200), mouseX, mouseY)));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
