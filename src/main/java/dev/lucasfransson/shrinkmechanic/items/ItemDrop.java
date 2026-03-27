package dev.lucasfransson.shrinkmechanic.items;

import java.util.List;
import java.util.Random;

public class ItemDrop {

	private final Item item;
	private final int minAmount;
	private final int maxAmount;

	private ItemDrop(Item item, int minAmount, int maxAmount) {
		this.item = item;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}

	public static ItemDrop of(Item item, int amount) {
		return new ItemDrop(item, amount, amount);
	}

	public static ItemDrop of(Item item, int minAmount, int maxAmount) {
		return new ItemDrop(item, minAmount, maxAmount);
	}

	public Item getItem() {
		return item;
	}

	public int resolveAmount(Random rnd) {
		if (minAmount == maxAmount)
			return minAmount;
		return minAmount + rnd.nextInt(maxAmount - minAmount + 1);
	}

	public static ItemDrop fromList(List<Item> items) {
		if (items == null || items.isEmpty())
			return null;

		Item first = items.getFirst();
		long count = items.stream()
				.filter(i -> i.getClass() == first.getClass()).count();

		return new ItemDrop(first, (int) count, (int) count);
	}
}
