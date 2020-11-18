public class AnimationAction extends Action{
    public AnimationAction(ActiveEntity entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, null, null, repeatCount);
    }

    @Override
    protected void executeAction(EventScheduler scheduler)
    {
        ((AnimatedEntity)this.entity).nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    new AnimationAction(entity, null, null, Math.max(this.repeatCount - 1, 0)),
                    ((AnimatedEntity)this.entity).getAnimationPeriod());
        }
    }


}
