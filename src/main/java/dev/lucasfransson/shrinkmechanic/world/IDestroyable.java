package dev.lucasfransson.shrinkmechanic.world;

public interface IDestroyable {
	void onDestroy();
	void destroy();
	void setDestroyCallback(Runnable destroyCallback);
}