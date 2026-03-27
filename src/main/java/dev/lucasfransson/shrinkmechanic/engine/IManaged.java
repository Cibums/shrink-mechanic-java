package dev.lucasfransson.shrinkmechanic.engine;

public interface IManaged {
	void setDestroyCallback(Runnable destroyCallback);
}
