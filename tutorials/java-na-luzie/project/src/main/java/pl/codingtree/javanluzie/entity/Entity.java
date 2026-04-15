package pl.codingtree.javanluzie.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.engine.Renderable;
import pl.codingtree.javanluzie.engine.Updatable;

public class Entity implements Renderable, Updatable {
    private static final Logger logger = LoggerFactory.getLogger(Entity.class);
    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;

    public Entity(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
    }

    public void attack(Entity target) {
        logger.info("{} atakuje {}!", name, target.getName());
        target.takeDamage(attackPower);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
        logger.debug("{} otrzymuje {} obrazen! HP: {}/{}", name, damage, health, maxHealth);
        if (health > 0 && health < maxHealth * 0.2) {
            logger.warn("{} ma malo HP! ({}/{})", name, health, maxHealth);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttackPower() { return attackPower; }

    @Override
    public void render() {
        logger.info("  [{}] HP: {}/{} ATK: {}", name, health, maxHealth, attackPower);
    }

    @Override
    public void update(double deltaTime) {
        // Base entities don't do anything on update — subclasses can override
    }

    @Override
    public String toString() {
        return name + " [HP: " + health + "/" + maxHealth + ", ATK: " + attackPower + "]";
    }
}
