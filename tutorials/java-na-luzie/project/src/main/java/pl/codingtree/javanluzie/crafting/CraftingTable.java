package pl.codingtree.javanluzie.crafting;

import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.Item;

public class CraftingTable {

    public static boolean craft(Recipe recipe, Inventory<Item> inventory) {
        System.out.println("\n--- Craftowanie: " + recipe.getName() + " ---");

        // Check ingredients
        for (Item ingredient : recipe.getIngredients()) {
            if (!inventory.contains(ingredient)) {
                System.out.println("Brakuje: " + ingredient.getName() + "!");
                return false;
            }
        }

        // Remove ingredients
        for (Item ingredient : recipe.getIngredients()) {
            inventory.remove(ingredient);
        }

        // Add result
        inventory.add(recipe.getResult());
        System.out.println("Stworzono: " + recipe.getResult() + "!");
        return true;
    }
}
