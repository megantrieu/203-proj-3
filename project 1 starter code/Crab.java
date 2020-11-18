
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends AnimatedEntity{
    private static final String QUAKE_KEY = "quake";
    private PathingStrategy strategy = new AStarPathingStrategy();


    public Crab(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
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
        List<Point> nextPoints = strategy.computePath(getPosition(), destPos, p -> (world.withinBounds(p) && !world.isOccupied(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
        if (nextPoints.size() == 0) {
            return getPosition();
        }
        return nextPoints.get(0);
    }


}