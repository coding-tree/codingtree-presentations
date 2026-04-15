package pl.codingtree.javanluzie.item;

public class Armor extends Item {
    private final int defense;

    public Armor(String name, double weight, Rarity rarity, int defense) {
        super(name, weight, rarity);
        this.defense = defense;
    }

    public int getDefense() { return defense; }

    @Override
    public String toString() {
        return super.toString() + " DEF: " + defense;
    }
}
