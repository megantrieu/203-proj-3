
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Snake extends AnimatedEntity{
    private static final String QUAKE_KEY = "quake";
    private PathingStrategy strategy = new AStarPathingStrategy();

    public Snake(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<ActiveEntity> crabTarget = world.findNearest(this.position, Sgrass.class);
        long nextPeriod = this.actionPeriod;

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().position;

            if (this.moveToCrab(world, crabTarget.get(), scheduler))
            {
                ActiveEntity quake = new Quake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this,world, imageStore,0),
                nextPeriod);
    }


    @Override
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    @Override
    protected int getRepeatCount() {
        return 0;
    }

    public boolean moveToCrab(WorldModel world,
                              Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionCrab(world, target.position);

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionCrab(WorldModel world, Point destPos)
    {
//        int horiz = Integer.signum(destPos.x - this.position.x);
//        Point newPos = new Point(this.position.x + horiz,
//                this.position.y);
//
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 ||
//                (occupant.isPresent() && !(occupant.get() instanceof Fish)))
//        {
//            int vert = Integer.signum(destPos.y - this.position.y);
//            newPos = new Point(this.position.x, this.position.y + vert);
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 ||
//                    (occupant.isPresent() && !(occupant.get() instanceof Fish)))
//            {
//                newPos = this.position;
//            }
//        }
//        return newPos;
        //PathingStrategy strategy = new SingleStepPathingStrategy();

        List<Point> nextPoints = strategy.computePath(getPosition(), destPos, p -> (world.withinBounds(p) && !world.isOccupied(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
        if (nextPoints.size() == 0) {
            return getPosition();
        }
        return nextPoints.get(0);
    }


}