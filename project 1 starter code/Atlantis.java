import processing.core.PImage;

import java.util.List;

public class Atlantis extends AnimatedEntity {


    private static int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    public Atlantis(String id, Point position,
                    List<PImage> images) {
        super(id, position, images, 0, 0, 0, 0);
    }

    protected int getRepeatCount() {
        return ATLANTIS_ANIMATION_REPEAT_COUNT;
    }

    protected void executeActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    @Override
    public int getAnimationPeriod() {
        return 0;
    }

    @Override
    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new AnimationAction(this, null, null, ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

}
