import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity{

    public AnimatedEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public abstract int getAnimationPeriod();

    protected void scheduleActions(EventScheduler scheduler,
                                   WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction(this,world, imageStore,0),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                new AnimationAction(this, null, null, this.getRepeatCount()),
                this.animationPeriod);
    }

    protected abstract int getRepeatCount();

    protected void executeActivity(WorldModel world,ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
}
