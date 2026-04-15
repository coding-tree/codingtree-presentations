package pl.codingtree.javanluzie.item;

public class Weapon extends Item {
    private final int damage;

    public Weapon(String name, double weight, Rarity rarity, int damage) {
        super(name, weight, rarity);
        this.damage = damage;
    }

    public int getDamage() { return damage; }

    @Override
    public String toString() {
        return super.toString() + " DMG: " + damage;
    }
}
