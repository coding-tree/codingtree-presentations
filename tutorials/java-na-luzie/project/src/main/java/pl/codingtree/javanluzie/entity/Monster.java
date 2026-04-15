package pl.codingtree.javanluzie.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Monster extends Entity {
    private static final Logger logger = LoggerFactory.getLogger(Monster.class);
    private String lootDrop;
    private static final Random random = new Random();

    public Monster(String name, int health, int attackPower, String lootDrop) {
        super(name, health, attackPower);
        this.lootDrop = lootDrop;
    }

    public String dropLoot() {
        logger.info("{} upuszcza: {}!", getName(), lootDrop);
        return lootDrop;
    }

    @Override
    public void attack(Entity target) {
        if (random.nextInt(100) < 20) {
            logger.debug("{} pudluje!", getName());
            return;
        }
        super.attack(target);
    }

    public String getLootDrop() { return lootDrop; }
}
