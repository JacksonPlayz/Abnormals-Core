package com.teamabnormals.abnormals_core.core.events;

import net.minecraft.entity.Entity;
import net.minecraft.world.TrackedEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * This event is fired when a {@link net.minecraft.world.TrackedEntity} ticks.
 * @author SmellyModder (Luke Tonon)
 */
public final class EntityTrackingEvent extends EntityEvent {
	private final boolean updating;

	private EntityTrackingEvent(Entity entity, boolean updating) {
		super(entity);
		this.updating = updating;
	}

	public static void onEntityTracking(Entity entity, boolean updating) {
		MinecraftForge.EVENT_BUS.post(new EntityTrackingEvent(entity, updating));
	}

	/**
	 * @return If this was fired when the entity was being updated
	 * @see TrackedEntity#tick()
	 * @see com.teamabnormals.abnormals_core.core.mixin.TrackedEntityMixin
	 */
	public boolean isUpdating() {
		return this.updating;
	}
}
