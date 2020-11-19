import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Lion extends AnimatedEntity{
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();

    public Lion(String id, int resourceLimit, int resourceCount,
                Point position, int actionPeriod, int animationPeriod,
                List<PImage> images)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public Point nextPositionLion(WorldModel world,
                                  Point destPos)
    {
//        int horiz = Integer.signum(destPos.x - this.position.x);
//        Point newPos = new Point(this.position.x + horiz,
//                this.position.y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.y - this.position.y);
//            newPos = new Point(this.position.x,
//                    this.position.y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;


        //PathingStrategy strategy = new SingleStepPathingStrategy();

        List<Point> nextPoints = strategy.computePath(getPosition(), destPos, p -> (world.withinBounds(p) && !world.isOccupied(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
        if (nextPoints.size() == 0) {
            return getPosition();
        }
        return nextPoints.get(0);
    }

    protected abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
