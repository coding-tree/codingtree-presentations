package pl.codingtree.javanluzie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.entity.Entity;
import pl.codingtree.javanluzie.entity.Monster;
import pl.codingtree.javanluzie.entity.Player;

public class Arena {
    private static final Logger logger = LoggerFactory.getLogger(Arena.class);

    public static void fight(Entity fighter1, Entity fighter2) {
        logger.info("========================================");
        logger.info("  ARENA: {} vs {}", fighter1.getName(), fighter2.getName());
        logger.info("========================================");

        int round = 1;
        while (fighter1.isAlive() && fighter2.isAlive()) {
            logger.debug("--- Runda {} ---", round);
            fighter1.attack(fighter2);
            if (fighter2.isAlive()) {
                fighter2.attack(fighter1);
            }
            logger.debug("{}", fighter1);
            logger.debug("{}", fighter2);
            round++;
        }

        Entity winner = fighter1.isAlive() ? fighter1 : fighter2;
        logger.info("{} wygrywa!", winner.getName());

        if (winner instanceof Player player && fighter2 instanceof Monster monster) {
            player.gainExperience(50);
            monster.dropLoot();
        }
    }
}
