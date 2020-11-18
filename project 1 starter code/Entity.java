import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
   //private final EntityKind kind;
   protected final String id;
   protected Point position;
   protected final List<PImage> images;
   protected int imageIndex;
   protected final int resourceLimit;
   protected int resourceCount;
   protected final int actionPeriod;
   protected final int animationPeriod;
   protected final Random rand = new Random();



   public Entity(String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      //this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }
   public PImage getCurrentImage()
   {
         return (this.getImage().get(this.getImageIndex()));
   }

   public int getImageIndex() {
      return this.imageIndex;
   }

   public List<PImage> getImage() {
      return this.images;
   }

   public Point getPosition(){
      return this.position;
   }

   public void setPosition(Point position){
      this.position = position;
   }

}