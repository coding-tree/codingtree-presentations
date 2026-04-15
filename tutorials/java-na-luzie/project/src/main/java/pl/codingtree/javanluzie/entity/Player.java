package pl.codingtree.javanluzie.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Entity {
    private static final Logger logger = LoggerFactory.getLogger(Player.class);
    private int level;
    private int experience;

    public Player(String name, int health, int attackPower) {
        super(name, health, attackPower);
        this.level = 1;
        this.experience = 0;
    }

    public void gainExperience(int xp) {
        experience += xp;
        logger.info("{} zdobywa {} XP! (Lacznie: {})", getName(), xp, experience);
        if (experience >= level * 100) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience = 0;
        logger.info("*** {} awansuje na poziom {}! ***", getName(), level);
    }

    public int getLevel() { return level; }

    @Override
    public String toString() {
        return super.toString() + " LVL: " + level;
    }
}
