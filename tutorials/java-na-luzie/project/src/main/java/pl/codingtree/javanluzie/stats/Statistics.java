package pl.codingtree.javanluzie.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.entity.Entity;
import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.Item;
import pl.codingtree.javanluzie.item.Rarity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Statistics {
    private static final Logger logger = LoggerFactory.getLogger(Statistics.class);

    public static void printEntityStats(List<Entity> entities) {
        logger.info("=== Statystyki Encji ===");

        long alive = entities.stream()
                .filter(Entity::isAlive)
                .count();
        logger.info("Zyje: {}/{}", alive, entities.size());

        Optional<Entity> strongest = entities.stream()
                .filter(Entity::isAlive)
                .max(Comparator.comparingInt(Entity::getAttackPower));
        strongest.ifPresent(e ->
                logger.info("Najsilniejszy: {} (ATK: {})", e.getName(), e.getAttackPower()));

        Optional<Entity> mostHealth = entities.stream()
                .filter(Entity::isAlive)
                .max(Comparator.comparingInt(Entity::getHealth));
        mostHealth.ifPresent(e ->
                logger.info("Najwiecej HP: {} ({}/{})", e.getName(), e.getHealth(), e.getMaxHealth()));

        entities.stream()
                .filter(Entity::isAlive)
                .sorted(Comparator.comparingInt(Entity::getHealth).reversed())
                .forEach(e -> logger.debug("  {} - HP: {}", e.getName(), e.getHealth()));
    }

    public static void printInventoryStats(Inventory<Item> inventory) {
        logger.info("=== Statystyki Ekwipunku ===");

        List<Item> items = inventory.getItems();
        logger.info("Przedmiotow: {}/{}", items.size(), inventory.getMaxSize());

        double totalWeight = items.stream()
                .mapToDouble(Item::getWeight)
                .sum();
        logger.info("Laczna waga: {}kg", String.format("%.1f", totalWeight));

        Map<Rarity, Long> byRarity = items.stream()
                .collect(Collectors.groupingBy(Item::getRarity, Collectors.counting()));
        byRarity.forEach((rarity, count) ->
                logger.info("  {}: {}", rarity.getDisplayName(), count));

        items.stream()
                .filter(i -> i.getRarity() == Rarity.LEGENDARY)
                .map(Item::getName)
                .sorted()
                .forEach(name -> logger.warn("LEGENDARNY: {}", name));
    }
}
