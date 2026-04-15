package pl.codingtree.javanluzie.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.entity.Entity;
import java.util.ArrayList;
import java.util.List;

public class GameWorld implements Updatable, Renderable {
    private static final Logger logger = LoggerFactory.getLogger(GameWorld.class);
    private final List<Entity> entities;
    private final GameConfig config;

    public GameWorld(GameConfig config) {
        this.entities = new ArrayList<>();
        this.config = config;
    }

    public void addEntity(Entity entity) {
        if (entities.size() < config.maxEntities()) {
            entities.add(entity);
            logger.info("[World] Dodano: {}", entity.getName());
        } else {
            logger.warn("[World] Swiat pelny! Max: {}", config.maxEntities());
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        logger.info("[World] Usunieto: {}", entity.getName());
    }

    public List<Entity> getEntities() {
        return List.copyOf(entities);
    }

    public void removeDeadEntities() {
        entities.removeIf(e -> !e.isAlive());
    }

    @Override
    public void update(double deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
        removeDeadEntities();
    }

    @Override
    public void render() {
        if (config.debugMode()) {
            logger.debug("[Debug] Entities: {}", entities.size());
        }
        for (Entity entity : entities) {
            entity.render();
        }
    }
}
