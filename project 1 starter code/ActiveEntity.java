import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity{
    protected final int actionPeriod;
    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {
        return this.actionPeriod;
    }
    protected void scheduleActions(EventScheduler scheduler,
                                            WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore, 0),
                this.actionPeriod);
        scheduler.scheduleEvent(this, new AnimationAction(this, null, null, 0),
                ((AnimatedEntity)this).getAnimationPeriod());
    };
    protected abstract void executeActivity(WorldModel world,ImageStore imageStore, EventScheduler scheduler);

}
