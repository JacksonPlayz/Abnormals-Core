package com.teamabnormals.abnormals_core.core.library.endimator.instructions;

import com.mojang.serialization.Codec;
import com.teamabnormals.abnormals_core.core.library.endimator.EndimatorEntityModel;

/**
 * @author SmellyModder (Luke Tonon)
 *
 * @param <IS> - The type of {@link EndimationInstruction}.
 */
public abstract class EndimationInstruction<IS extends EndimationInstruction<?>> {
	public static final Codec<EndimationInstruction<?>> CODEC = EndimationInstructions.REGISTRY.dispatchStable(instruction -> instruction, EndimationInstruction::codecFor);
	
	private final Codec<IS> codec;
	
	public EndimationInstruction(Codec<IS> codec) {
		this.codec = codec;
	}
	
	public abstract void process(EndimatorEntityModel<?> model);
	
	public Codec<IS> getCodec() {
		return this.codec;
	}
	
	@SuppressWarnings("unchecked")
	private static <C extends EndimationInstruction<?>> Codec<C> codecFor(C instruction) {
		return (Codec<C>) instruction.getCodec();
	}
}