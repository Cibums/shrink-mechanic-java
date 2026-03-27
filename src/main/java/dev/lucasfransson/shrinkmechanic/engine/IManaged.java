package dev.lucasfransson.shrinkmechanic.engine;

public interface IManaged {
	void destroy();
	void setDestroyCallback(Runnable destroyCallback);
}
