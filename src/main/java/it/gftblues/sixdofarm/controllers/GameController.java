package it.gftblues.sixdofarm.controllers;

/**
 * Copyright 2020 Gabriele Tafuro
 * 
 * This file is part of Pierin-oh!.
 *
 * Pierin-oh! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  Pierin-oh! is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import it.gftblues.utils.FileUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;


/**
 * Game controller.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class GameController {
  
  /**
   * Hatswitch positions.
   */
  public enum Hatswitch {Left, Top, Right, Bottom};

  public static final String WIRELESS_GAMEPAD ="Wireless Gamepad";
  public static final String MS_XBOX_360_WIRELESS = "Controller (Xbox 360 Wireless Receiver for Windows)";
  public static final String MS_SIDEWINDER = "Microsoft® SideWinder® Plug & Play Game P";
  public static final String NINTENDO_SWITCH = "Joy-Con";
  public static final String MS_SIDEWINDER_FORCE_FEEDBACK_2 ="SideWinder Force Feedback 2 Joystick";
  
  public static final String COMPONENT_SLIDER = "Slider";
  public static final String COMPONENT_BUTTON = "Button";
  public static final String COMPONENT_HAT_SWITCH = "Hat Switch";
  public static final String COMPONENT_X_AXIS = "X Axis";
  public static final String COMPONENT_Y_AXIS = "Y Axis";
  public static final String COMPONENT_Z_ROTATION = "Z Rotation";

  private Map<String, Controller> controllers;
  private String lastControllerName;
  private final List<GameControllerListener> listeners;
//  private boolean[] elements;
//  static final long HEARTBEATMS = 100;
  
  private Map<String, Hatswitch> hatswitchBehaviours;
  
//  private float xAxisPercentage = 0;
//  private float yAxisPercentage = 0;
//  private float hatSwitchPosition = 0;

  public GameController() {
    loadLibraries();
//    makeHatswitchBehaviourDB();
    searchForControllers();
    listeners = new ArrayList<>();
    initialize();
  }

  private void loadLibraries() {
    String osPlatform = System.getProperty("os.name");
    try {
      if (osPlatform.contains("Windows")) {
        FileUtils.loadJarDll("/lib/jinput-dx8_64.dll");
        FileUtils.loadJarDll("/lib/jinput-raw_64.dll");
      } else if (osPlatform.contains("Linux")) {
        FileUtils.loadJarDll("/lib/libjinput-linux64.so");
      } else {
        Logger.getLogger(FaceRecognitionDevice.class.getName()).log(
                Level.SEVERE, 
                null, 
                String.format("Platform %s not supported.",osPlatform)
        );
      }
    } catch (IOException ex) {
        Logger.getLogger(
                FaceRecognitionDevice.class.getName()).log(
                        Level.SEVERE,
                        null,
                        ex
        );
    }
  }

  private void initialize() {
    /**
     * Activate the code below just for testing.
     */
/*    addListener((float xAxisPercentage1, float yAxisPercentage1, float hatswitch, boolean[] buttons) -> {
      System.out.println(String.format("Game controller input\nx%%: %.3f\ny%%: %.3f\nhat switch: %.3f", xAxisPercentage1, yAxisPercentage1, hatswitch));
      for (int i = 0; i < buttons.length; i++) {
        if (buttons[i]) {
          System.out.println(String.format("Button %d: pressed", i+1));
        }
      }
      System.out.println("Total buttons: "+buttons.length);
    });*/
    
    new Thread(() -> {
//      try {
        while (true) {
          if (!controllers.isEmpty()) {
            poll();
/*            ListIterator<GameControllerListener> iter = listeners.listIterator();
            while(iter.hasNext()) {
              iter.next().actionPerformed(
                      xAxisPercentage,
                      yAxisPercentage,
                      hatSwitchPosition,
                      elements
              );
            }
            Thread.sleep(HEARTBEATMS);*/
          }
        }
/*      } catch (InterruptedException ex) {
        Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
      }*/
    }).start();
  }
  
/*  private void makeHatswitchBehaviourDB() {
    hatswitchBehaviours = new HashMap<>();
    hatswitchBehaviours.put(WIRELESS_GAMEPAD, Hatswitch.Left);
    hatswitchBehaviours.put(MS_XBOX_360_WIRELESS, Hatswitch.Left);
    hatswitchBehaviours.put(MS_SIDEWINDER, Hatswitch.Left);
    hatswitchBehaviours.put(NINTENDO_SWITCH, Hatswitch.Left);
    hatswitchBehaviours.put(MS_SIDEWINDER_FORCE_FEEDBACK_2, Hatswitch.Left);
  }*/
  
  public Hatswitch getHatswitchBehaviour() {
    if (controllers.isEmpty()) {
      return Hatswitch.Bottom;
    }
    return hatswitchBehaviours.get(lastControllerName);
  }

  public void addListener(GameControllerListener listener) {
    listeners.add(listener);
  }
  
  public void removeListener(GameControllerListener listener) {
    listeners.remove(listener);
  }

  /**
    * Search (and save) for controllers of type Controller.Type.STICK,
    * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
    */
  public final void searchForControllers() {
//    Controller[] foundControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
    // Be aware that creating a new environment is fairly expensive
    Controller[] foundControllers;
    try {
      foundControllers = createDefaultEnvironment().getControllers();
      controllers = new HashMap<>();
      hatswitchBehaviours = new HashMap<>();
  //    int next = 0;
      for (Controller controller : foundControllers) {
        if (
                controller.getType() == Controller.Type.STICK ||
                controller.getType() == Controller.Type.GAMEPAD ||
                controller.getType() == Controller.Type.WHEEL ||
                controller.getType() == Controller.Type.FINGERSTICK ||
                (controller.getType() == Controller.Type.UNKNOWN &&
                controller.getName().startsWith("Nintendo RVL"))
        ) {
          String systemControlName = controller.getName();
          String controllerName = String.format(
                  "%s %s %02d",
                  systemControlName, 
                  controller.getType(),
                  controller.getPortNumber());
          // Add new controller to the list of all controllers.
          controllers.put(controllerName, controller);
          lastControllerName = systemControlName;
          if (systemControlName.compareTo(WIRELESS_GAMEPAD) == 0) {
            hatswitchBehaviours.put(systemControlName, Hatswitch.Top);
          } else if (systemControlName.compareTo(MS_XBOX_360_WIRELESS) == 0) {
            hatswitchBehaviours.put(systemControlName, Hatswitch.Left);
          } else if (systemControlName.compareTo(MS_SIDEWINDER) == 0) {
            hatswitchBehaviours.put(MS_SIDEWINDER, Hatswitch.Left);
          } else if (systemControlName.compareTo(NINTENDO_SWITCH) == 0) {
            hatswitchBehaviours.put(NINTENDO_SWITCH, Hatswitch.Bottom);
          } else if (systemControlName.compareTo(MS_SIDEWINDER_FORCE_FEEDBACK_2) == 0) {
            hatswitchBehaviours.put(MS_SIDEWINDER_FORCE_FEEDBACK_2, Hatswitch.Left);
          } else {
            hatswitchBehaviours.put(systemControlName, Hatswitch.Left);
          }
          // Add new controller to the list on the window.
        }
      }
    } catch (ReflectiveOperationException ex) {
      Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
    * Given value of axis in percentage.
    * 
    * Percentages increases from left/top to right/bottom.
    * If idle (in centre) returns 50, if joystick axis is pushed to the left/top
    * edge returns 0 and if it's pushed to the right/bottom returns 100.
    * 
    * @param axisValue
    *        The value of the axis.
    * 
    * @return value of axis in percentage.
    */
  public int getAxisValueInPercentage(float axisValue) {
    return (int)(((2 - (1 - axisValue)) * 100) / 2);
  }

  /**
   * Polls all available game controllers for status changes and notifies its
   * listeners.
   */
  public void poll() {
    controllers.forEach((k,v) -> {
      if (v.poll()) {

/******************************************************************************/        
        Event event = new Event();
        EventQueue queue = v.getEventQueue();

        while (queue.getNextEvent(event)) {
          /* Get event component */
          Component comp = event.getComponent();
/*          System.out.println("Poll: "+comp.getName());
          System.out.println("Slider: "+event.getValue());*/
          ListIterator<GameControllerListener> iter = listeners.listIterator();
          while(iter.hasNext()) {
            if (comp.isAnalog()) {
              iter.next().analogActionPerformed(comp.getName(), event.getValue());
            } else {
//              System.out.println("\n\n"+comp.getName()+" - "+event.getValue()+"\n\n");
              iter.next().digitalActionPerformed(comp.getName(), event.getValue() == 1.0f);
            }
          }
        }
/******************************************************************************/        
        
/*GFT        Component[] components = v.getComponents();
        if ( components != null && components.length > 0) {
          elements = new boolean[components.length];
          for(int i = 0; i < elements.length; i++) {
            elements[i] = false;
          }

          for (Component component : components) {
            Component.Identifier componentIdentifier = component.getIdentifier();

            // Buttons
            //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
            if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
              // Is button pressed?
              boolean isItPressed = true;
              if(component.getPollData() == 0.0f) {
                isItPressed = false;
              }
              int bntNum = Integer.parseUnsignedInt(componentIdentifier.getName());
              elements[bntNum] = isItPressed;
//              System.out.println("Button "+bntNum+" is "+(isItPressed ? "" : "not")+" pressed.");

              // Button index
              String buttonIndex;
              buttonIndex = component.getIdentifier().toString();

              // We know that this component was button so we can skip to next component.
              continue;
            }

            // Hat switch
            if(componentIdentifier == Component.Identifier.Axis.POV){
              hatSwitchPosition = component.getPollData();

              // We know that this component was hat switch so we can skip to next component.
              continue;
            }

            // Axes
            if(component.isAnalog()){
              float axisValue = component.getPollData();
//              System.out.printf(String.format("\nAxis value: %f", axisValue));
//              int axisValueInPercentage = getAxisValueInPercentage(axisValue);

              // X axis
              if(componentIdentifier == Component.Identifier.Axis.X) {
                xAxisPercentage = axisValue;
                continue; // Go to next component.
              }
              // Y axis
              if(componentIdentifier == Component.Identifier.Axis.Y) {
                yAxisPercentage = axisValue;
                continue; // Go to next component.
              }

              // Other axis
//              axisValueInPercentage;
            }
          }
        }*/
      }
    });
  }

  /**
   * Gets all available game controllers' names.
   * @return A {@code Set<String>} with the controllers' names.
   */
  public Set<String> getControllerNames() {
    return controllers.keySet();
  }

  /**
   * Creates a new default environment for game controllers.
   * 
   * https://stackoverflow.com/questions/17413690/java-jinput-rescan-reload-controllers
   */
  private static ControllerEnvironment createDefaultEnvironment() 
          throws ReflectiveOperationException {
    // Find constructor (class is package private, so we can't access it directly)
    Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>)
        Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];

    // Constructor is package private, so we have to deactivate access control checks
    constructor.setAccessible(true);

    // Create object with default constructor
    return constructor.newInstance();
  }
}
