import processing.core.PImage;

import java.util.List;

public class Fire extends AnimatedEntity{
    private static final String FIRE_KEY = "fire";
    private static int FIRE_ANIMATION_REPEAT_COUNT = 5;
    public Fire(String id, Point position,
                    List<PImage> images) {
        super(id, position, images, 0, 0, 0, 0);
    }

    @Override
    public int getAnimationPeriod() {
        return 0;
    }

    @Override
    protected int getRepeatCount() {
        return 0;
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new AnimationAction(this, null, null, FIRE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
