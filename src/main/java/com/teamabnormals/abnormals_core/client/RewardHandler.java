package com.teamabnormals.abnormals_core.client;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.teamabnormals.abnormals_core.client.model.SlabfishHatModel;
import com.teamabnormals.abnormals_core.client.renderer.SlabfishHatLayerRenderer;
import com.teamabnormals.abnormals_core.core.AbnormalsCore;
import com.teamabnormals.abnormals_core.core.config.ACConfig;
import com.teamabnormals.abnormals_core.core.util.NetworkUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = AbnormalsCore.MODID, value = Dist.CLIENT)
public class RewardHandler {
	public static final Map<UUID, RewardData> REWARDS = new HashMap<>();

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new Gson();
	private static final String REWARDS_URL = "https://api.minecraftabnormals.com/rewards.json";
	private static final ResourceLocation CAPE_TEXTURE = new ResourceLocation(AbnormalsCore.MODID, "textures/abnormals_cape.png");

	private static final Set<UUID> RENDERED_CAPES = new HashSet<>();

	@SubscribeEvent
	public static void onEvent(RenderPlayerEvent.Post event) {
		PlayerEntity player = event.getPlayer();
		UUID uuid = PlayerEntity.getUUID(player.getGameProfile());

		if (player instanceof AbstractClientPlayerEntity && !RENDERED_CAPES.contains(uuid) && REWARDS.containsKey(uuid) && REWARDS.get(uuid).getTier() >= 99) {
			AbstractClientPlayerEntity clientPlayer = (AbstractClientPlayerEntity) player;
			if (clientPlayer.hasPlayerInfo()) {
				Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = clientPlayer.playerInfo.playerTextures;
				if(!playerTextures.containsKey(MinecraftProfileTexture.Type.CAPE))
				{
					playerTextures.put(MinecraftProfileTexture.Type.CAPE, CAPE_TEXTURE);
					playerTextures.put(MinecraftProfileTexture.Type.ELYTRA, CAPE_TEXTURE);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEvent(ClientPlayerNetworkEvent.LoggedInEvent event) {
		NetworkUtil.updateSlabfish(ACConfig.CLIENT.slabfishHat.get());
	}

	public static void clientSetup(FMLClientSetupEvent event) {
		CompletableFuture.supplyAsync(() -> {
			try (CloseableHttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11").build())
			{
				HttpGet get = new HttpGet(REWARDS_URL);
				try (CloseableHttpResponse response = client.execute(get))
				{
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() != 200)
						throw new IOException("Failed to connect to '" + REWARDS_URL + "'. " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
					return IOUtils.toBufferedInputStream(response.getEntity().getContent());
				}
			} catch (Exception e) {
				LOGGER.error("Failed to get rewards from '" + REWARDS_URL + "'.", e);
			}

			return null;
		}, Util.getServerExecutor()).thenAcceptAsync(stream -> {
			if(stream == null)
				return;

			try (InputStreamReader reader = new InputStreamReader(stream)) {
				JsonObject object = JSONUtils.fromJson(reader);
				for(Map.Entry<String, JsonElement> entry : object.entrySet()) {
					REWARDS.put(UUID.fromString(entry.getKey()), GSON.fromJson(entry.getValue(), RewardData.class));
				}
			} catch (Exception e) {
				LOGGER.error("Failed to parse rewards.", e);
			}
			System.out.println(REWARDS);
		}, Minecraft.getInstance());

		for(PlayerRenderer renderer : Minecraft.getInstance().getRenderManager().getSkinMap().values())
			renderer.addLayer(new SlabfishHatLayerRenderer(renderer, new SlabfishHatModel()));
	}

	public static class RewardData {

		private final String username;
		private final int tier;
		private final SlabfishData slabfish;

		public RewardData(String username, int tier, SlabfishData slabfish) {
			this.username = username;
			this.tier = tier;
			this.slabfish = slabfish;
		}

		public String getUsername() {
			return username;
		}

		public int getTier() {
			return tier;
		}

		public SlabfishData getSlabfish() {
			return slabfish;
		}

		public static class SlabfishData {

			@SerializedName("base")
			private final String typeUrl;
			@SerializedName("sweater")
			private final String sweaterUrl;
			@SerializedName("backpack")
			private final String backpackUrl;

			public SlabfishData(String typeUrl, String sweaterUrl, String backpackUrl) {
				this.typeUrl = typeUrl;
				this.sweaterUrl = sweaterUrl;
				this.backpackUrl = backpackUrl;
			}

			public String getTypeUrl() {
				return typeUrl;
			}

			public String getSweaterUrl() {
				return sweaterUrl;
			}

			public String getBackpackUrl() {
				return backpackUrl;
			}
		}
	}
}
