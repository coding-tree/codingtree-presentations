package pl.codingtree.javanluzie.engine;

public record GameConfig(
    int worldWidth,
    int worldHeight,
    int maxEntities,
    boolean debugMode,
    String title
) {
    public static GameConfig defaultConfig() {
        return new GameConfig(800, 600, 100, false, "Java na Luzie");
    }
}
