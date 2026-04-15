package pl.codingtree.javanluzie.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dragon extends Monster {
    private static final Logger logger = LoggerFactory.getLogger(Dragon.class);

    public Dragon(String name, int health, int attackPower) {
        super(name, health, attackPower, "Smocza Luska");
    }

    @Override
    public void attack(Entity target) {
        logger.info("{} zieje ogniem na {}!", getName(), target.getName());
        target.takeDamage(getAttackPower() * 2);
    }

    @Override
    public String toString() {
        return "\uD83D\uDC09 " + super.toString();
    }
}
