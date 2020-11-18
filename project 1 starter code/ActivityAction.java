public class ActivityAction extends Action{

    public ActivityAction(ActiveEntity entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        super(entity, world, imageStore, 0);
    }

    @Override
    protected void executeAction(EventScheduler scheduler) {
        (this.entity).executeActivity(this.world,
                this.imageStore, scheduler);
    }
}