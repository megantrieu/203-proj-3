import processing.core.PImage;

import java.util.List;

public class Fish extends FishAndGrass{
    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;

    public Fish(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, images, 0, 0, actionPeriod, 0);
    }

    protected void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        ActiveEntity crab = new Crab(this.id + CRAB_ID_SUFFIX,
                pos, this.actionPeriod / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }
}