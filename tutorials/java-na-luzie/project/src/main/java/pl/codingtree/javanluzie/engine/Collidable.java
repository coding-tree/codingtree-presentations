package pl.codingtree.javanluzie.engine;

public interface Collidable {
    double getX();
    double getY();
    double getWidth();
    double getHeight();

    default boolean collidesWith(Collidable other) {
        return getX() < other.getX() + other.getWidth() &&
               getX() + getWidth() > other.getX() &&
               getY() < other.getY() + other.getHeight() &&
               getY() + getHeight() > other.getY();
    }
}
