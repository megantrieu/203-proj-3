/*
Action: ideally what our various entities might do in our virutal world
 */

public abstract class Action
{
   //protected final ActionKind kind;
   protected final ActiveEntity entity;
   protected final WorldModel world;
   protected final ImageStore imageStore;
   protected final int repeatCount;

   public Action(ActiveEntity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public Entity getEntity(){
      return entity;
   }

   protected abstract void executeAction(EventScheduler scheduler);

}
