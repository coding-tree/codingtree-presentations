package pl.codingtree.javanluzie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.entity.Dragon;
import pl.codingtree.javanluzie.entity.Entity;
import pl.codingtree.javanluzie.entity.Monster;
import pl.codingtree.javanluzie.entity.Player;
import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.*;
import pl.codingtree.javanluzie.stats.Statistics;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== Java na Luzie - Sesja 4: Supermoc Javy ===");

        // Tworzymy bohaterow
        Player hero = new Player("Rycerz", 100, 15);
        List<Monster> monsters = new ArrayList<>(List.of(
            new Monster("Goblin", 30, 5, "Maly Noz"),
            new Monster("Ork", 50, 8, "Topor"),
            new Dragon("Smaug", 80, 10)
        ));

        // Lambda: forEach
        logger.info("Nasi przeciwnicy:");
        monsters.forEach(m -> logger.info("  - {}", m));

        // Walki
        List<Entity> allEntities = new ArrayList<>();
        allEntities.add(hero);
        allEntities.addAll(monsters);

        for (Monster monster : monsters) {
            if (hero.isAlive()) {
                Arena.fight(hero, monster);
            }
        }

        // Streamy na encjach
        logger.info("--- Stream magic ---");
        long deadCount = monsters.stream()
                .filter(m -> !m.isAlive())
                .count();
        logger.info("Pokonani potworzy: {}", deadCount);

        // Zbierz lup z pokonanych
        List<String> loot = monsters.stream()
                .filter(m -> !m.isAlive())
                .map(Monster::dropLoot)
                .toList();
        logger.info("Zebrano lup: {}", loot);

        // Ekwipunek ze streamami
        Inventory<Item> plecak = new Inventory<>("Plecak", 10);
        plecak.add(new Weapon("Miecz Ognia", 3.0, Rarity.EPIC, 25));
        plecak.add(new Weapon("Sztylet", 1.0, Rarity.COMMON, 8));
        plecak.add(new Armor("Tarcza Smoka", 5.0, Rarity.LEGENDARY, 30));
        plecak.add(new Item("Mikstura HP", 0.3, Rarity.UNCOMMON));
        plecak.add(new Item("Zloto", 0.1, Rarity.RARE));

        // Statystyki -- boss fight ze streamami!
        Statistics.printEntityStats(allEntities);
        Statistics.printInventoryStats(plecak);

        logger.info("=== Koniec sesji 4 ===");
        logger.info("Gotowy na LWJGL? Twoja przygoda dopiero sie zaczyna!");
    }
}
