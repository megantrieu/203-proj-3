import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
   protected final String id;
   protected Point position;
   protected final List<PImage> images;
   protected int imageIndex;
   protected final Random rand = new Random();

   public Entity(String id, Point position,
      List<PImage> images)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
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