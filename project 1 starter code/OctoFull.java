import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo{

    public OctoFull(String id, int resourceLimit,
                     Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images)
    {
        super(id,  resourceLimit, resourceLimit,position, animationPeriod, actionPeriod, images);
    }

    protected void executeActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<ActiveEntity> fullTarget = world.findNearest(this.position,
                Atlantis.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            fullTarget.get().scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world,imageStore,0),
                    this.actionPeriod);
        }
    }
    protected boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionOcto(world, target.position);

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

    public void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore)
    {
        ActiveEntity octo = new OctoNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod, this.animationPeriod,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }


    @Override
    protected int getRepeatCount() {
        return 0;
    }

    @Override
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

}