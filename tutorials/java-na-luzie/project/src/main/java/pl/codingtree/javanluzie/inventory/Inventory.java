package pl.codingtree.javanluzie.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory<T extends Item> {
    private static final Logger logger = LoggerFactory.getLogger(Inventory.class);
    private final String name;
    private final List<T> items;
    private final int maxSize;

    public Inventory(String name, int maxSize) {
        this.name = name;
        this.items = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public boolean add(T item) {
        if (items.size() >= maxSize) {
            logger.warn("Ekwipunek {} jest pelny!", name);
            return false;
        }
        items.add(item);
        logger.info("+ {} dodano do {}", item.getName(), name);
        return true;
    }

    public boolean remove(T item) {
        boolean removed = items.remove(item);
        if (removed) {
            logger.info("- {} usunieto z {}", item.getName(), name);
        }
        return removed;
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public List<T> getItems() {
        return List.copyOf(items);
    }

    public int getSize() { return items.size(); }
    public int getMaxSize() { return maxSize; }
    public String getName() { return name; }

    public void show() {
        logger.info("=== {} ({}/{}) ===", name, items.size(), maxSize);
        for (T item : items) {
            logger.info("  {}", item);
        }
    }
}
