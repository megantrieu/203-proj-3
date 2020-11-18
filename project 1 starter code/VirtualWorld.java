import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   private final String OBSTACLE_KEY = "obstacle";
   private final int OBSTACLE_NUM_PROPERTIES = 4;
   private final int OBSTACLE_ID = 1;
   private final int OBSTACLE_COL = 2;
   private final int OBSTACLE_ROW = 3;

   private final int FISH_NUM_PROPERTIES = 5;
   private final int FISH_ID = 1;
   private final int FISH_COL = 2;
   private final int FISH_ROW = 3;
   private final int FISH_ACTION_PERIOD = 4;

   private final String ATLANTIS_KEY = "atlantis";
   private final int ATLANTIS_NUM_PROPERTIES = 4;
   private final int ATLANTIS_ID = 1;
   private final int ATLANTIS_COL = 2;
   private final int ATLANTIS_ROW = 3;


   private final String SGRASS_KEY = "seaGrass";
   private final int SGRASS_NUM_PROPERTIES = 5;
   private final int SGRASS_ID = 1;
   private final int SGRASS_COL = 2;
   private final int SGRASS_ROW = 3;
   private final int SGRASS_ACTION_PERIOD = 4;
   private final String BGND_KEY = "background";
   private final int BGND_NUM_PROPERTIES = 4;
   private final int BGND_ID = 1;
   private final int BGND_COL = 2;
   private final int BGND_ROW = 3;

   private final int OCTO_NUM_PROPERTIES = 7;
   private final int OCTO_ID = 1;
   private final int OCTO_COL = 2;
   private final int OCTO_ROW = 3;
   private final int OCTO_LIMIT = 4;
   private final int OCTO_ACTION_PERIOD = 5;
   private final int OCTO_ANIMATION_PERIOD = 6;
   private final String OCTO_KEY = "octo";
   private final String FISH_KEY = "fish";

   private final int PROPERTY_KEY = 0;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME,imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         load(in, world);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntity())
      {
         //Only start actions for entities that include action (not those with just animations)
         if((entity.getClass() !=Obstacle.class)) {
            if (((ActiveEntity)entity).getActionPeriod() > 0)
               ((ActiveEntity)entity).scheduleActions(scheduler, world, imageStore);
         }
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }

   private boolean processLine(String line, WorldModel world)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return this.parseBackground(properties, world);
            case OCTO_KEY:
               return this.parseOcto(properties, world);
            case OBSTACLE_KEY:
               return this.parseObstacle(properties, world);
            case FISH_KEY:
               return this.parseFish(properties, world);
            case ATLANTIS_KEY:
               return this.parseAtlantis(properties, world);
            case SGRASS_KEY:
               return this.parseSgrass(properties, world);
         }
      }

      return false;
   }

   private boolean parseSgrass(String [] properties, WorldModel world)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         ActiveEntity entity = new Sgrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties, WorldModel world)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         ActiveEntity entity = new Fish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }


   private boolean parseObstacle(String [] properties, WorldModel world)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = new Obstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseOcto(String [] properties, WorldModel world)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         ActiveEntity entity = new OctoNotFull(properties[OCTO_ID],
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   private boolean parseBackground(String [] properties,
                                   WorldModel world)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties, WorldModel world)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         ActiveEntity entity = new Atlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   private void load(Scanner in, WorldModel world)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }


}
