package dev.lucasfransson.shrinkmechanic.engine;

public interface IGameSystem {
	void tryRegister(Object object);
	void tryUnregister(Object object);
}