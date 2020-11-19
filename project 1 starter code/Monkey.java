import processing.core.PImage;

import java.util.List;

public class Monkey extends MonkeyandGrass{
    private static final String snake_KEY = "snake";
    private static final String snake_ID_SUFFIX = " -- snake";
    private static final int snake_PERIOD_SCALE = 4;
    private static final int snake_ANIMATION_MIN = 50;
    private static final int snake_ANIMATION_MAX = 150;

    public Monkey(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, images, 0, 0, actionPeriod, 0);
    }

    protected void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        ActiveEntity snake = new Snake(this.id + snake_ID_SUFFIX,
                pos, this.actionPeriod / snake_PERIOD_SCALE,
                snake_ANIMATION_MIN +
                        rand.nextInt(snake_ANIMATION_MAX - snake_ANIMATION_MIN),
                imageStore.getImageList(snake_KEY));

        world.addEntity(snake);
        snake.scheduleActions(scheduler, world, imageStore);
    }
}