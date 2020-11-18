import processing.core.PImage;

import java.util.List;

public abstract class FishAndGrass extends ActiveEntity{
    public FishAndGrass(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    @Override
    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore,0),
                this.actionPeriod);
    }
}
