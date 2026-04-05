package dev.lucasfransson.shrinkmechanic.world.objects;

public interface ISignalReceiver extends IHasInputs {

	void trigger(SignalEmitter source, int strength);
	void untrigger(SignalEmitter source);

}
