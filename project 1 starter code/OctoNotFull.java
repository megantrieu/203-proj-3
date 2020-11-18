import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo{

    public OctoNotFull(String id, int resourceLimit,
                         Point position, int actionPeriod, int animationPeriod,
                         List<PImage> images)
    {
        super(id, resourceLimit,0,position, actionPeriod , animationPeriod, images);
    }

    @Override
    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<ActiveEntity> notFullTarget = world.findNearest(this.position,
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world, imageStore, 0),
                    this.actionPeriod);
        }
    }



    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            ActiveEntity octo = new OctoFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod, this.animationPeriod,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    protected int getRepeatCount() {
        return 0;
    }

    protected boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPositionOcto(world, target.position);

            if (!this.getPosition().equals(nextPos))
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

    @Override
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }
}