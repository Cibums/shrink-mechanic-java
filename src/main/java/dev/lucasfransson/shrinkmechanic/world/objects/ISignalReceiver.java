package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Set;

public interface ISignalReceiver extends IHasInputs {

	/**
	 * @param source   the immediate upstream emitter (not the root source)
	 * @param strength signal strength at this point in the chain
	 * @param visited  per-emit set used for cycle prevention; receivers
	 *                 should check/add themselves before propagating
	 */
	void trigger(SignalEmitter source, int strength,
			Set<SignalEmitter> visited);

	void untrigger(SignalEmitter source);

}
