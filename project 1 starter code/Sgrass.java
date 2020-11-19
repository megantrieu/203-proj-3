import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass extends MonkeyandGrass{

    private static final String Monkey_KEY = "Monkey";
    private static final String Monkey_ID_PREFIX = "Monkey -- ";
    private static final int Monkey_CORRUPT_MIN = 20000;
    private static final int Monkey_CORRUPT_MAX = 30000;

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
            ActiveEntity Monkey = new Monkey(Monkey_ID_PREFIX + this.id,
                    openPt.get(), Monkey_CORRUPT_MIN +
                    rand.nextInt(Monkey_CORRUPT_MAX - Monkey_CORRUPT_MIN),
                    imageStore.getImageList(Monkey_KEY));
            world.addEntity(Monkey);
            Monkey.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world,imageStore, 0),
                this.actionPeriod);
    }
}
