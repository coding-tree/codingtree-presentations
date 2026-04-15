package pl.codingtree.javanluzie.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLoop {
    private static final Logger logger = LoggerFactory.getLogger(GameLoop.class);
    private final GameWorld world;
    private final GameConfig config;
    private boolean running;
    private int maxTicks;

    public GameLoop(GameWorld world, GameConfig config, int maxTicks) {
        this.world = world;
        this.config = config;
        this.maxTicks = maxTicks;
    }

    public void start() {
        running = true;
        logger.info("=== {} -- START ===", config.title());

        int tick = 0;
        long lastTime = System.nanoTime();

        while (running && tick < maxTicks) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            tick++;
            logger.debug("--- Tick {} (dt: {}s) ---", tick, String.format("%.4f", deltaTime));

            world.update(deltaTime);
            world.render();

            if (world.getEntities().isEmpty()) {
                logger.warn("Swiat jest pusty! Koniec gry.");
                running = false;
            }
        }

        logger.info("=== GAME OVER ===");
    }

    public void stop() {
        running = false;
    }
}
