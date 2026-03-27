package dev.lucasfransson.shrinkmechanic.items;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	private List<Item> items = new ArrayList<>();

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		this.items.add(item);
	}
}
