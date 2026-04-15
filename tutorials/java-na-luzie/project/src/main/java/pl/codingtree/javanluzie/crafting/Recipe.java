package pl.codingtree.javanluzie.crafting;

import pl.codingtree.javanluzie.item.Item;
import java.util.List;

public class Recipe {
    private final String name;
    private final List<Item> ingredients;
    private final Item result;

    public Recipe(String name, List<Item> ingredients, Item result) {
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
        this.result = result;
    }

    public String getName() { return name; }
    public List<Item> getIngredients() { return ingredients; }
    public Item getResult() { return result; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Przepis: ").append(name).append("\n");
        sb.append("  Skladniki: ");
        for (Item i : ingredients) {
            sb.append(i.getName()).append(", ");
        }
        sb.append("\n  Wynik: ").append(result);
        return sb.toString();
    }
}
