package pl.codingtree.javanluzie.item;

public enum Rarity {
    COMMON("Zwykly", "white"),
    UNCOMMON("Niezwykly", "green"),
    RARE("Rzadki", "blue"),
    EPIC("Epicki", "purple"),
    LEGENDARY("Legendarny", "orange");

    private final String displayName;
    private final String color;

    Rarity(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
}
