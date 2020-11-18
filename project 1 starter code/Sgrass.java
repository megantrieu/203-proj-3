import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass extends FishAndGrass{

    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;

    public Sgrass(String id, Point position, int actionPeriod,
                                List<PImage> images)
    {
        super(id, position, images, 0, 0,
                actionPeriod, 0);
    }

    protected void executeActivity(WorldModel world,ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            ActiveEntity fish = new Fish(FISH_ID_PREFIX + this.id,
                    openPt.get(), FISH_CORRUPT_MIN +
                    rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world,imageStore, 0),
                this.actionPeriod);
    }
}
