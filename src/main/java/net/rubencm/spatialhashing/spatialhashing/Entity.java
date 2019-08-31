package net.rubencm.spatialhashing.spatialhashing;

import lombok.Getter;
import lombok.Setter;

public class Entity {

    /**
     * Origin
     */
    private Position origin;

    /**
     * Destination
     */
    private Position destination;

    /**
     * Radius (will be viewing radius)
     */
    @Getter
    @Setter
    private int radius;

    /**
     * Speed (units of distance (pixels in this case)/second)
     */
    private int speed;

    private boolean isMoving;
    private long startMovement;

    public Entity(Position position, int radius, int speed) {
        this.origin = this.destination = position;
        this.radius = radius;
        this.speed = speed;

        this.isMoving = false;
    }

    private int getDistance(Position o, Position d) {
        // Distance between 2 points formula
        return (int) Math.sqrt(Math.pow(d.getX() - o.getX(), 2) + Math.pow(d.getY() - o.getY(), 2));
    }

    public Position getPosition() {

        // If the entity isn't moving, the current point is the origin
        if (!isMoving) {
            return origin;
        }

        // Distance between origin and destination
        int distance = getDistance(origin, destination);
        // Time in seconds since the travel started
        float time = (System.currentTimeMillis() - startMovement)/1000f;
        // Distance traveled
        int traveled = (int)(speed * time);

        // If have stopped
        if(traveled >= distance) {
            origin = destination;
            isMoving = false;

            return origin;
        }

        // If still in movement
        float td = (float)traveled/(float)distance;
        return new Position(
                origin.getX()+(int)((destination.getX()-origin.getX())*td),
                origin.getY()+(int)((destination.getY()-origin.getY())*td)
        );
    }

    public void setPosition(Position position) {
        origin = getPosition();
        destination = position;
        isMoving = true;
        startMovement = System.currentTimeMillis();
    }
}
