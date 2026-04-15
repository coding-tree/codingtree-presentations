package pl.codingtree.javanluzie.item;

import java.util.Objects;

public class Item {
    private final String name;
    private final double weight;
    private final Rarity rarity;

    public Item(String name, double weight, Rarity rarity) {
        this.name = name;
        this.weight = weight;
        this.rarity = rarity;
    }

    public String getName() { return name; }
    public double getWeight() { return weight; }
    public Rarity getRarity() { return rarity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && rarity == item.rarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rarity);
    }

    @Override
    public String toString() {
        return name + " [" + rarity.getDisplayName() + ", " + weight + "kg]";
    }
}
