import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Octo extends AnimatedEntity{
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();

    public Octo(String id, int resourceLimit, int resourceCount,
                    Point position, int actionPeriod, int animationPeriod,
                    List<PImage> images)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public Point nextPositionOcto(WorldModel world,
                                  Point destPos)
    {
        List<Point> nextPoints = strategy.computePath(getPosition(), destPos, p -> (world.withinBounds(p) && !world.isOccupied(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
        if (nextPoints.size() == 0) {
            return getPosition();
        }
        return nextPoints.get(0);
    }



    protected abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
