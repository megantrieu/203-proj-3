import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity{

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake(Point position, List<PImage> images){
        super(QUAKE_ID, position, images, 0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    protected void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction(this,world, imageStore,0),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                new AnimationAction(this, null, null, QUAKE_ANIMATION_REPEAT_COUNT),
                this.animationPeriod);
    }

    protected int getRepeatCount() {
        return QUAKE_ANIMATION_REPEAT_COUNT;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }
}
