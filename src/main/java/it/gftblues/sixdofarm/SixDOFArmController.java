package it.gftblues.sixdofarm;

/**
 * This file is part of Pierin-oh!
 * 
 * Pierin-oh! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pierin-oh! is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright 2020 Gabriele Tafuro
 */

import it.gftblues.sixdofarm.controllers.FaceRecognitionDevice;
import it.gftblues.sixdofarm.controllers.FaceRecognitionDeviceListener;
import it.gftblues.sixdofarm.controllers.GameController;
import it.gftblues.sixdofarm.controllers.GameControllerListener;
import it.gftblues.sixdofarm.controllers.PointerDevice;
import it.gftblues.sixdofarm.controllers.SerialCommunicator;
import it.gftblues.sixdofarm.controllers.SerialCommunicatorListener;
import it.gftblues.sixdofarm.joints.Clamp;
import it.gftblues.sixdofarm.joints.Elbow;
import it.gftblues.sixdofarm.joints.Joint;
import it.gftblues.sixdofarm.joints.Shoulder;
import it.gftblues.sixdofarm.joints.Wrist;
import it.gftblues.sixdofarm.ui.SixDOFArmControllerGUI;
import java.awt.image.BufferedImage;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.gftblues.sixdofarm.ui.SixDOFArmControllerUI;
import it.gftblues.sixdofarm.ui.SixDOFArmResources;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * The {@code SixDOFArmController} is the main class that governs the robotic
 * arm.
 * @author Gabriele Tafuro
 */
public class SixDOFArmController
        implements SixDOFArmControllerInterface,
                   SerialCommunicatorListener,
                   GameControllerListener,
                   FaceRecognitionDeviceListener {

  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Pierin-oh";

  /**
   * Line feed.
   */
  private static final String LF = "\n";
  
  /**
   * The reference to a {@code SixDOFArmControllerUI} user interface.
   */
  private SixDOFArmControllerUI ui;
  
  /**
   * The serial communicator used to send commands to the arm and receive 
   * feedback.
   */
  private final SerialCommunicator comm;
  
  /**
   * The game controller.
   */
  private final GameController gameController = new GameController();

  /**
   * The previous horizontal position coming from the game controller.
   * 
   * Implementation note: Along with {@code oldY} it is used to avoid repeatedly
   * sending the same position to the arm.
   */
  private int oldGameControllerX = 0;

  /**
   * The previous vertical position coming from the game controller.
   * 
   * Implementation note: Along with {@code oldX} it is used to avoid repeatedly
   * sending the same position to the arm.
   */
  private int oldGameControllerY = 0;

  /**
   * Old value for the value provided by the game controller for the clamp. 
   */
  private int oldGameControllerClamp = 0;
  
  /**
   * Milliseconds from the previous command.
   */
  private long prevMilliseconds = System.currentTimeMillis();
  
  /**
   * Used to check whether the game controller's actions has to be taken into 
   * account.
   */
  private boolean useGameControllers = false;

  /**
   * The reference to the {@code FaceRecognitionDevice} object used for 
   * face-recognition.
   */
  private final FaceRecognitionDevice recognizer;
  
  /**
   * Used to check whether or not use the face-recognition feature.
   */
  private boolean useFaceRecognition = false;

  /**
   * Last horizontal shoulder position.
   */
  private int lastShoulderHorPos;

  /**
   * Last vertical shoulder position.
   */
  private int lastShoulderVerPos;

  /**
   * Last vertical elbow position.
   */
  private int lastElbowVerPos;

  /**
   * Last vertical wrist position.
   */
  private int lastWristVerPos;

  /**
   * Collision detector.
   */
  private CollisionDetector collisionDetector;

  /**
   * {@code SixDOFArmConfiguration} configuration.
   */
  private SixDOFArmConfiguration config;

  /**
   * Dispatcher.
   */
  private Dispatcher dispatcher = null;

  /**
   * Robotic arm's joints.
   */
  private static final Map<String, Joint> joints = getJoints();
  
  /**
   * The logger.
   */
  private static final Logger logger = Logger.getLogger(APPLICATION_NAME);
  
  /**
   * File handler for the log file.
   */
  private FileHandler loggerFile;
  
  /**
   * Get the joints of the robotic arm.
   * @return A {@code Map<String, Joint>} containing all joints.
   */
  public static final Map<String, Joint> getJoints() {
    if (joints != null) {
      return joints;
    }
    Map<String, Joint> jnts = new HashMap<>();
    Joint joint;
    joint = new Shoulder();
    jnts.put(joint.getName(), joint);
    joint = new Elbow();
    jnts.put(joint.getName(), joint);
    joint = new Wrist();
    jnts.put(joint.getName(), joint);
    joint = new Clamp();
    jnts.put(joint.getName(), joint);
    return jnts;
  }

  /**
   * Constructor.
   */
  public SixDOFArmController() {
    loadConfiguration();
    Set<String> ctrlNames = gameController.getControllerNames();
    if (ctrlNames.size() > 0) {
      useGameControllers = true;
    }
    gameController.addListener(this);
    comm = new SerialCommunicator();
    comm.addListener(this);
    initialize();
    recognizer = new FaceRecognitionDevice(
            config.getOpencvClassifierPathname()
    );
    recognizer.addFaceRecognitionDeviceListener(this);
  }

  /**
   * Sets the Pierin-oh! log file.
   */
  private void setLogger() {
    String path = config.getLogFilePathname();
    if (path == null) {
      path = System.getProperty("user.home")+File.separator+"Pierin-oh.log";
      config.setLogFilePathname(path);
      ui.setLogFilePathname(path);
    }
    
    try {  
      loggerFile = new FileHandler(path, true);
    } catch (IOException | SecurityException ex) {
      Logger.getLogger(
              SixDOFArmController.class.getName()
      ).log(Level.SEVERE, null, ex);
    }
    logger.addHandler(loggerFile);
    SimpleFormatter formatter = new SimpleFormatter();  
    loggerFile.setFormatter(formatter);  
    logger.info(SixDOFArmResources.getString("LOG_MESSAGE_PIERINHO_STARTED"));
  }

  /**
   * Initialize the arm controller.
   */
  private void initialize() {
    collisionDetector = new CollisionDetector();
    collisionDetector.setBaseHeight(config.getBaseHeight());
    collisionDetector.setClampLength(config.getClampLength());
    collisionDetector.setElbowLength(config.getElbowLength());
    collisionDetector.setShoulderLength(config.getShoulderLength());
    dispatcher = new Dispatcher(this);
    dispatcher.setTimeBetweenCommands(config.getTimeBetweenCommands());
    dispatcher.setComm(comm);
  }

  /**
   * Sends a command to the arm (serial port)
   * .
   * @param command
   *        A {@code String} with the command to be sent to the arm.
   */
  private void sendToPort(String command) {
    checkCommand(command);
    dispatcher.addCommand(command);
//    comm.writeToPort(command);
  }

  /**
   * Sends a command to the arm (serial port)
   * .
   * @param command
   *        An {@code int} with the single character command to be sent to the 
   *        arm.
   */
  @Deprecated
  public void sendToPort(int command) {
    String sCommand = ""+(char)command;
    checkCommand(sCommand);
    dispatcher.addCommand(""+sCommand);
//      comm.writeToPort(command);
  }

  /**
   * Checks the commands sent to the arm to take any further action, if 
   * required.
   * 
   * Implementation note: When the green button status changes, a notification 
   * is sent to the UI to give a feedback to the user.
   * 
   * @param command 
   *        The command to be checked.
   */
  private void checkCommand(String command) {
/*    CharSequence cs = "G"; 
    if (command.contains(cs)) {
      ui.setGreenButton((greenLedOn = !greenLedOn));
    }*/
  }

  /**
   * Gets an array with all available serial ports' names.
   * @return An array of {@code String} with the serial ports' names.
   */
  @Override
  public String[] getSerialPortNames() {
    List<String> ports = comm.getPortList();
    if (ports != null && !ports.isEmpty()) {
      return ports.toArray(new String[0]);
    }
    String[] none = new String[1];
    none[0] = SixDOFArmResources.getString("NO_PORT_AVAILABLE");
    return none;
  }

  /**
   * Gets the 'no port available' {@code String}.
   * @return The 'no port available' {@code String}.
   */
  @Override
  public String getNoPortAvailableString() {
    return SixDOFArmResources.getString("NO_PORT_AVAILABLE");
  }

  /**
   * Sets the user interface and sends to it the default settings.
   * @param ui The user interface.
   */
  @Override
  public void setUI(SixDOFArmControllerUI ui) {
    this.ui = ui;
    ui.setSerialPortNames(getSerialPortNames());
    ui.setSerialDataRates(Arrays.stream(SerialCommunicator.PORT_SPEEDS)
              .mapToObj(String::valueOf)
              .toArray(String[]::new)
    );
    ui.setSerialDataRates(Arrays.stream(SerialCommunicator.PORT_SPEEDS)
              .mapToObj(String::valueOf)
              .toArray(String[]::new)
    );
    ui.setSerialDataBits(Arrays.stream(SerialCommunicator.DATA_BITS)
              .mapToObj(String::valueOf)
              .toArray(String[]::new)
    );
    ui.setSerialParities(getNames(SerialCommunicator.Parity.class)
    );
    ui.setSerialStopBits(Arrays.stream(SerialCommunicator.STOP_BIT)
              .mapToObj(String::valueOf)
              .toArray(String[]::new)
    );
  }

  @Deprecated
  @Override
  public void pointerDeviceHasMoved(PointerDevice pd) {                                  
    int x = pd.getX();
    int y = pd.getY();
    int[] buttons = pd.getButtons();
    /*
     * If the pointer device is not in the previous position and the 2nd button
     * is not pressed
     */
    if (
            (x != oldGameControllerX || y != oldGameControllerY) && 
            (buttons == null || buttons.length <= 1 || buttons[1] <= 0)
    ) {
      Dimension dim = ui.getTargetDimension();
      int w = dim.getWidth();
      int h = dim.getHeight();
      //      x : width = deg : 180
      int degX = x*180/w;
      int degY = 180-(y*180/h);
      String degs = formatDegrees(degX)+","+formatDegrees(degY);
      sendToPort(degs);
//      System.out.println("Sent "+degs);
    }
/*    if (buttons != null && buttons.length > 1) {
      System.out.println(String.format("Button 1 has been pressed %d time(s).",buttons[1]));
    }*/
    oldGameControllerX = x;
    oldGameControllerY = y;
    try {
      TimeUnit.MILLISECONDS.sleep(15l);
    } catch (InterruptedException ex) {
      logger.log(Level.SEVERE, null, ex);
    }
  }                                 

  /**
   * Gets the name of the selected serial port.
   * @return The name of the selected serial port.
   */
  @Override
  public String getSerialPortName() {
    return comm.getComPortName();
  }

  /**
   * Connects the arm through the connected serial port.
   */
  @Override
  public void connectArm() {
    try {
      comm.setComPortName(ui.getSerialPortName());
      comm.setDataBit(ui.getSerialDataBit());
      comm.setDataRate(ui.getSerialDataRate());
      comm.setParity(ui.getSerialParity());
      comm.setStopBit(ui.getSerialStopBit());
      comm.setTimeOut(ui.getSerialTimeout());
      comm.connect();
      moveToStartPosition();
    } catch (Exception ex) {
      notifyError(
              String.format(
                      SixDOFArmResources.getString("MSG_COULD_NOT_CONNECT_TO"), 
                      comm.getComPortName()
              )
      );
    }
  }
  
  /**
   * Gets the {@code String} associated to the given label.
   * 
   * Note: If the user interface has not been set, a 'No UI.' {@code String} is 
   * returned.
   * 
   * @param label
   *        The label of the required string.
   * @return The string associated to the {@code label}.
   */
  public String getExistingLabel(String label) {
    if (ui != null) {
      return SixDOFArmResources.getString(label);
    } else {
      return "No UI.";
    }
  }

  /**
   * Disconnects from the serial port.
   */
  @Override
  public void disconnectArm() {
    this.useGameControllers = false;
    ui.setGameCOntrollerEnabled(false);
    this.useFaceRecognition = false;
    ui.setUseFaceRecognition(false);
    moveToOffPosition();
    comm.close();
  }

  /**
   * Check whether the arm is connected through the serial port.
   * @return {@code true} if connected, {@code false} otherwise.
   */
  @Override
  public boolean isArmConnected() {
//    return true;
    if (comm == null) {
      return false;
    }
    return comm.isConnected();
  }

  @Deprecated
  @Override
  public void moveLeft() {
    sendToPort('L');
  }

  @Deprecated
  @Override
  public void moveRight() {
    sendToPort('R');
  }

  @Deprecated
  @Override
  public void moveUp() {
    sendToPort('U');
  }

  @Deprecated
  @Override
  public void moveDown() {
    sendToPort('D');
  }

  @Deprecated
  @Override
  public void moveToFront() {
    sendToPort("090,090");
  }

  @Deprecated
  @Override
  public void moveTo(int x, int y) {
    sendToPort(String.format("%03d,%03d",x,y));
  }

  /**
   * Moves the arm to its start position.
   */
  public void moveToStartPosition() {
    boolean useGameCtrlrs = this.useGameControllers;
    this.useGameControllers = false;
    int deg;

    /**
      * "Stand" clear message when the robotic arm is about to move to its starting
      * position.
      * 
      * <strong>CAUTION:</strong> It is an essential message, as the program does 
      * not know how the arm's joints are positioned before issuing the first 
      * command to each of them, so the arm can swing quite fast and hurt someone.
      * See SixDOFArm.properties file.
      */
    ui.showWarningMessage(SixDOFArmResources.getString("MSG_STAND_CLEAR"));

    deg = 0;
    moveElbowVertically(deg);

    deg = 0;
    moveWristVertically(deg);

    deg = 0;
    moveShoulderVertically(deg);

    deg = 90;
    moveElbowVertically(deg);

    deg = -90;
    moveWristVertically(deg);

    deg = 90;
    moveShoulderVertically(deg);
    
    deg = 0;
    moveShoulderHorizontally(deg);
    
    deg = -20;
    rotateWrist(deg);

    deg = 20;
    rotateWrist(deg);

    deg = 0;
    rotateWrist(deg);

    Joint clamp = joints.get("clamp");
    PierinhoCommand pc = clamp.getCommands().get(Clamp.ACTION_CS);

    deg = pc.getMin();
    setClamp(deg);
    
    deg = pc.getMax();
    setClamp(deg);

    deg = (pc.getMax()+pc.getMin())/2;
    setClamp(deg);
    
    this.useGameControllers = useGameCtrlrs;
  }

  /**
   * Moves the arm's shoulder horizontally.
   * @param deg
   *        The amount in degrees where to move the arm.
   */
  @Override
  public void moveShoulderHorizontally(int deg) {
//    System.out.println("Trying SH"+deg);
    double pos = horizontalPosition(deg, lastElbowVerPos, lastWristVerPos);
/*    System.out.println(String.format(
              "Horizontal position: %.3f",
              pos
      ));*/
    lastShoulderHorPos = deg;
    sendToPort(Shoulder.ACTION_SH+(-lastShoulderHorPos));
    ui.setShoulderHorizontalPosition(deg);
  }

  /**
   * Moves the arm's shoulder vertically.
   * @param deg 
   *        The amount in degrees where to move the arm.
   */
  @Override
  public boolean moveShoulderVertically(int deg) {
//    System.out.println("Trying SV"+deg);
    if (collisionDetector.collision(
            lastShoulderVerPos, lastElbowVerPos, lastWristVerPos,
            deg, lastElbowVerPos, lastWristVerPos)
    ) {
      double pos = collisionDetector.verticalDistance(
              lastShoulderVerPos, 
              lastElbowVerPos, 
              deg
      );
      /**
       * When raised, this error reports that the provided command would cause a 
       * collision of the arm with itself or the plane where it lies on.
       * See SixDOFArm.properties file.
       */
      ui.showError(String.format(
              SixDOFArmResources.getString("ERROR_DANGEROUS_MOVE"),
              pos
      ));
      return false;
    } else {
      lastShoulderVerPos = deg;
      sendToPort(Shoulder.ACTION_SV+(-lastShoulderVerPos));
      ui.setShoulderVerticalPosition(deg);
    }
    return true;
  }

  /**
   * Moves the arm's elbow vertically.
   * @param deg 
   *        The amount in degrees where to move the arm.
   */
  @Override
  public boolean moveElbowVertically(int deg) {
//    System.out.println("Trying EV"+deg);
    if (collisionDetector.collision(
            lastShoulderVerPos, lastElbowVerPos, lastWristVerPos,
            lastShoulderVerPos, deg, lastWristVerPos)
    ) {
      double pos = collisionDetector.verticalDistance(
              lastShoulderVerPos, 
              lastElbowVerPos, 
              deg
      );
      ui.showError(String.format(
              SixDOFArmResources.getString("ERROR_DANGEROUS_MOVE"),
              pos
      ));
      return false;
    } else {
      lastElbowVerPos = deg;
      sendToPort(Elbow.ACTION_EV+lastElbowVerPos);
      ui.setElbowVerticalPosition(deg);
    }
    return true;
  }

  /**
   * Moves the arm's wrist vertically.
   * @param deg 
   *        The amount in degrees where to move the arm.
   */
  @Override
  public boolean moveWristVertically(int deg) {
//    System.out.println("Trying WV"+deg);
    if (collisionDetector.collision(
            lastShoulderVerPos, lastElbowVerPos, lastWristVerPos,
            lastShoulderVerPos, lastElbowVerPos, deg)
    ) {
      double pos = collisionDetector.verticalDistance(
              lastShoulderVerPos, 
              lastElbowVerPos, 
              deg
      );
      ui.showError(String.format(
              SixDOFArmResources.getString("ERROR_DANGEROUS_MOVE"),
              pos
      ));
      return false;
    } else {
      lastWristVerPos = deg;
      sendToPort(Wrist.ACTION_WV+lastWristVerPos);
      ui.setWristVerticalPosition(deg);
    }
    return true;
  }

  /**
   * Moves the arm to the off position.
   */
  @Deprecated
  @Override
  public void moveToOffPosition() {
    sendToPort('O');
  }

  /**
   * Closes the arm's clamp.
   */
  @Override
  public void closeClamp() {
    sendToPort(Clamp.ACTION_CC);
    Joint clamp = joints.get("clamp");
    ui.setClampAperturelPosition(Clamp.MAX);
  }

  /**
   * Opens the arm's clamp.
   */
  @Override
  public void openClamp() {
    sendToPort(Clamp.ACTION_CO);
    ui.setClampAperturelPosition(Clamp.MIN);
  }

  /**
   * Sets the arm's clamp aperture.
   * @param degrees 
   *        The amount of the aperture in degrees.
   */
  @Override
  public void setClamp(int degrees) {
    sendToPort(Clamp.ACTION_CS+degrees);
    ui.setClampAperturelPosition(degrees);
}

  /**
   * Rotates the arm's wrist.
   * @param rotation 
   *        The amount in degrees to rotate the wrist.
   */
  @Override
  public void rotateWrist(int rotation) {
    sendToPort(Wrist.ACTION_WR+rotation);
    ui.setWristRotationalPosition(rotation);
  }

  /**
   * Sends a command to the robotic arm through the serial port.
   * @param command 
   *        The command to be sent to the robotic arm.
   */
  @Override
  public void sendCommand(String command) {
    sendToPort(command);
  }

  @Override
  public void searchForGameControllers() {
    gameController.searchForControllers();
  }

  @Override
  public Set<String> getGameControllerNames() {
    return gameController.getControllerNames();
  }

  @Override
  public void enableGameControllers(boolean enable) {
    useGameControllers = enable;
  }

  @Override
  public boolean areGameControlelrsEnabled() {
    return useGameControllers;
  }

  @Override
  public void notifyUIReady() {
    ui.setSerialPortName(getSerialPortName());
    ui.setSerialDataRate(9600);
    ui.setSerialDataBit(8);
    ui.setSerialParity(SerialCommunicator.Parity.None);
    ui.setSerialStopBit(1);
    ui.setSerialTimeout(2000);
    
    ui.setTimeBetweenCommands(config.getTimeBetweenCommands());
    ui.setLogFilePathname(config.getLogFilePathname());
    
    ui.setClassifierFilename(config.getOpencvClassifierPathname());

    ui.setUseFaceRecognition(useFaceRecognition);

    ui.setGravity(config.getGravity());
    ui.setClampLength(config.getClampLength());
    ui.setElbowLength(config.getElbowLength());
    ui.setShoulderLength(config.getShoulderLength());
    ui.setBaseHeight(config.getBaseHeight());
    ui.setShoulderMaths(
            config.getMathShoulderMass(),
            config.getMathShoulderLength(),
            config.getMathShoulderTorque()
    );
    ui.setElbowMaths(
            config.getMathElbowMass(),
            config.getMathElbowLength(),
            config.getMathElbowTorque());
    ui.setWristMaths(
            config.getMathWristMass(),
            config.getMathWristLength(),
            config.getMathWristTorque());
    setLogger();
  }

  @Override
  public void useFaceRecognition(boolean use) {
    useFaceRecognition = use;
    if (useFaceRecognition) {
      if (!recognizer.isRunning())
      recognizer.startRecognition();
    } else {
      recognizer.stopRecognition();
      ui.videoHasStopped();
    }
  }

  @Override
  public void setClassifierFilePath(String absolutePath) {
    config.setOpencvClassifierPathname(absolutePath);
  }

  @Override
  public void shutdown() {
    if (comm.isConnected()) {
      comm.writeToPort('O');
    }
    logger.log(
            Level.INFO, 
            SixDOFArmResources.getString("LOG_MESSAGE_PIERINHO_STOPPED")
    );
  }

  @Override
  public void notifyError(String message) {
    ui.showError(message);
  }

  @Override
  public void notifyMessage(String message) {
//    System.out.println(message);
/*    if (message.compareToIgnoreCase(GREEN_LED_ON) == 0) {
      greenLedOn = true;
    } else if (message.compareToIgnoreCase(GREEN_LED_OFF) == 0) {
      greenLedOn = false;
    }*/
    ui.sendFeedback(message);
  }

/*  public void actionPerformed(
          float xAxisPercentage, 
          float yAxisPercentage, 
          float hatswitch, 
          boolean[] buttons
  ) {

    if (isArmConnected() && useGameControllers) {
      int x;
      int y;
      String axis = null;
      int buttonPressed = -1;
      for(int i = 0; i < buttons.length; i++) {
        if (buttons[i]) {
          buttonPressed = i;
          break;
        }
      }

      if (buttonPressed > -1) {
        if (buttons[0]) {
//GFT          sendToPort('G');
        }
        if (buttons[1]) {
//GFT          sendToPort('F');
        }
        if (buttons[2]) {
//GFT          sendToPort('O');
        }
      } else if (hatswitch == 0.0f) {
        x = (int)((xAxisPercentage+1.0f)*90.0f)-90;
        y = (int)((yAxisPercentage+1.0f)*90.0f)-90;
        moveShoulderHorizontally(x);
        moveShoulderVertically(y);
        axis = String.format("%03d,%03d", x, y);
//        System.out.println(axis);
//GFT        sendToPort(axis);
      } else {
//        System.out.println(String.format("Hatswitch: %f", hatswitch));
        it.gftblues.sixdofarm.Point point = getFromHatswitch(hatswitch);
        axis = String.format("%03d,%03d", point.getX(), point.getY());
//        System.out.println(axis);
//GFT        sendToPort(axis);
      }
//      System.out.println("axis: "+axis);
   }
  }

  it.gftblues.sixdofarm.Point getFromHatswitch(float hatswitch) {
    Set<String> cns = gameController.getControllerNames();
    if (cns.size() > 0) {
      String name = cns.toArray(new String[0])[0];
      if (name != null) {
        double degrees = hatswitch*360.0d;
        int x;
        int y;
        switch (gameController.getHatswitchBehaviour()) {
          case Left:
            y = 180-(int)(degrees > 180.0d ? Math.abs(270.0d-degrees) : Math.abs(90.0d+degrees));
            x = (int)(degrees > 180.0d ? Math.abs(360.0d-degrees) : 180-Math.abs(180.0d-degrees));
            break;
          case Top:
            x = (int)(Math.abs(270.0d-degrees));
            y = (int)(Math.abs(degrees-180.0d));
            break;
          case Right:
            x = (int)(degrees > 180.0d ? Math.abs(270.0d-degrees) : Math.abs(90.0d-degrees));
            y = (int)(degrees > 180.0d ? Math.abs(360.0d-degrees) : Math.abs(180.0d-degrees));
            break;
          case Bottom:
          default:
            x = (int)(degrees > 180.0d ? Math.abs(270.0d-degrees) : Math.abs(90.0d-degrees));
            y = (int)(degrees > 180.0d ? Math.abs(360.0d-degrees) : Math.abs(180.0d-degrees));
        }
        return new it.gftblues.sixdofarm.Point(x, y);
      }
    }
    return new it.gftblues.sixdofarm.Point();
  }*/

  @Override
  public void analogActionPerformed(String component, float value) {
    long millis = System.currentTimeMillis();
    if (
            isArmConnected() && 
            useGameControllers && 
            millis-prevMilliseconds > 50
    ) {
      prevMilliseconds = millis;
      if (component.compareTo(GameController.COMPONENT_SLIDER) == 0) {
        System.out.println("slider.");

        int clamp = Math.round(Math.round(value*70.0f))+20;
/*        if (clamp >= 20 && clamp <= 90) {
          System.out.println(String.format("Analog\tClamp\t%d", clamp));
        } else {
          System.out.println(String.format("Analog\tClamp\t%.3f\terror", value));
        }*/
        if (
                clamp > oldGameControllerClamp+2 || 
                clamp < oldGameControllerClamp-2
        ) {
          oldGameControllerClamp = clamp;
          setClamp(clamp);
        }
      } else if (component.compareTo(GameController.COMPONENT_X_AXIS) == 0) {
        System.out.println("x axis.");
        int iValue = (int)((value+1.0f)*90.0f)-90;
        if (iValue >= -90 && iValue <= 90) {
          if (iValue > oldGameControllerX+5 || iValue < oldGameControllerX-5) {
//            System.out.println(String.format("Analog\tX axis\t%d", iValue));
            oldGameControllerX = iValue;
            moveShoulderHorizontally(-iValue);
          }
        } else {
          System.out.println(
                  String.format("Analog\tX axis\t%.3f\terror", value)
          );
        }
      } else if (component.compareTo(GameController.COMPONENT_Y_AXIS) == 0) {
        System.out.println("y axis.");
        int iValue = (int)((value+1.0f)*90.0f)-90;
        if (iValue >= -90 && iValue <= 90) {
          if (iValue > oldGameControllerY+5 || iValue < oldGameControllerY-5) {
//            System.out.println(String.format("Analog\tY axis\t%d", iValue));
            oldGameControllerY = iValue;
            moveShoulderVertically(-iValue);
          }
        } else {
          System.out.println(
                  String.format("Analog\tY axis\t%.3f\terror", value)
          );
        }
      } else if (
              component.compareTo(GameController.COMPONENT_Z_ROTATION) == 0
      ) {
        System.out.println("z rotation.");
        int iValue = (int)((value+1.0f)*90.0f)-90;
        if (iValue >= -90 && iValue <= 90) {
//          System.out.println(String.format("Analog\tWrist rotate\t%d", iValue));
        } else {
          System.out.println(
                  String.format("Analog\tWrist rotate\t%.3f\terror", value)
          );
        }
      } else {
        System.out.println(
                String.format("Analog\tUnknown %s\t%.3f", component, value)
        );
      }
    }
  }

  @Override
  public void digitalActionPerformed(String component, boolean value) {
  /*    System.out.println(
              "Digital\t"
                      +component+"\t"+(value ? "on" : "off")
      );*/
    if (isArmConnected() && useGameControllers) {
    }
  }

  @Override
  public void actionPerformed(
          Point point, 
          Dimension dimension, 
          BufferedImage image
  ) {
    int px = point.getX();
    int py = point.getY();
    int x = (int)(90-((px+dimension.getWidth()/2.0d)*9/32)); //  180/640 = 9/32
    int y = (int)(90-((py+dimension.getHeight()/2.0d)*3/8)); //  180/480 = 3/8
    ui.setVideoFrame(image);
    if (useFaceRecognition) {
      if (x >= -90 && x <= 90 && x != oldGameControllerX) {
        moveShoulderHorizontally(x);
      }
      if (y >= -90 && y <= 90 && y != oldGameControllerY) {
        moveShoulderVertically(y);
      }
    }
  }

  /**
   * Starts the application.
   * 
   * Implementation note: Creates a {@code PointerController} object and, then, 
   * a {@code PointerControllerGUI} that is passed as UI to the former.
   * 
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    SixDOFArmController pc = new SixDOFArmController();
    
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(() -> {
      new SixDOFArmControllerGUI(pc).setVisible(true);
    });
  }

  /**
   * Takes an {@code enum} in input and returns an array of {@code String}
   * containing the values.
   * 
   * @param e
   *        The enum to transform in a {@code String} array.
   * 
   * @return
   *        A {@code String} array with the corresponding values of the passed
   *        {@code enum}.
   */
  public static String[] getNames(Class<? extends Enum<?>> e) {
    return Arrays.stream(
            e.getEnumConstants()
    ).map(Enum::name).toArray(String[]::new);
  }

  /**
   * Transforms the degrees in a {@code String} with three characters with 
   * leading zeroes.
   * 
   * @param deg
   *        An {@code int} with the angle (in degrees, from 0° to 180°).
   * @return 
   *        A three character {@code String} with leading zeroes.
   */
  private static String formatDegrees(int deg) {
    return String.format("%03d", deg);
  }

  private void verticalPositionCheck() {
    System.out.println(String.format("%.3f", verticalPosition( 0,  0,  0)));
    System.out.println(String.format("%.3f", verticalPosition( 0,  0, 90)));
    System.out.println(String.format("%.3f", verticalPosition( 0, 90,  0)));
    System.out.println(String.format("%.3f", verticalPosition( 0, 90, 90)));
    System.out.println(String.format("%.3f", verticalPosition(90,  0,  0)));
    System.out.println(String.format("%.3f", verticalPosition(90,  0, 90)));
    System.out.println(String.format("%.3f", verticalPosition(90, 90,  0)));
    System.out.println(String.format("%.3f", verticalPosition(90, 90, 90)));
  }

  private double horizontalPosition(int shDeg, int elDeg, int wrDeg) {
    return collisionDetector.horizontalDistance(shDeg, elDeg, wrDeg);
  }

   private double verticalPosition(int shDeg, int elDeg, int wrDeg) {
    return collisionDetector.verticalDistance(shDeg, elDeg, wrDeg);
  }

  public SixDOFArmConfiguration getConfiguration() {
    return new SixDOFArmConfiguration(config);
  }

  public void setConfiguration(SixDOFArmConfiguration config) {
    if (config != null) {
      this.config = new SixDOFArmConfiguration(config);
    }
  }

  private void loadConfiguration() {
    FileInputStream oInStream;
    String filename = getConfigurationFilename();
    try {
      oInStream = new FileInputStream(filename);
    } catch (FileNotFoundException ex) {
      oInStream = null;
      if (ui != null) {
        /**
         * Error raised if no configuration find is found.
         * See SixDOFArm.properties file.
         */
        ui.showError(
                String.format("ERROR_CONFIGURATION_FILE_NOT_FOUND", filename)
        );
      } else {
        System.err.println(
                String.format("ERROR_CONFIGURATION_FILE_NOT_FOUND", filename)
        );
      }
    }

    if ( oInStream != null ) {
      try (XMLDecoder e = new XMLDecoder(new BufferedInputStream(oInStream))) {
        Object oReadObj = null;
        try {
          oReadObj = e.readObject();
        } catch ( NoSuchElementException oEvt ) {
          Logger.getLogger(SixDOFArmController.class.getName()).log(
                  java.util.logging.Level.SEVERE, 
                  null, 
                  oEvt
          );
        }
        if ( oReadObj instanceof SixDOFArmConfiguration ) {
          config = (SixDOFArmConfiguration)oReadObj;
        }
      }        
    } else {
      config = new SixDOFArmConfiguration();
    }
  }

  @Override
  public void saveConfiguration() {
      FileOutputStream oOutStream = null;
      String ostrFilename = getConfigurationFilename();

      try {
          oOutStream = new FileOutputStream(ostrFilename);
      } catch (FileNotFoundException ex) {
          Logger.getLogger(SixDOFArmController.class.getName()).log(
                  java.util.logging.Level.SEVERE, 
                  null, 
                  ex
          );
      }

      try (XMLEncoder e = new XMLEncoder(new BufferedOutputStream(oOutStream))
      ) {
        e.setPersistenceDelegate(
                URL.class,
                new PersistenceDelegate() {
                  @Override
                  protected Expression instantiate(
                          Object oldInstance, 
                          Encoder out
                  ) {
                    return new Expression(
                            oldInstance, 
                            oldInstance.getClass(), 
                            "new", 
                            new Object[]{ oldInstance.toString() }
                    );
                  }
                });
        e.writeObject(config);
        if (ui != null) {
          /**
           * "Configuration saved" message.
           * See SixDOFArm.properties file.
           */
          ui.showMessage("MSG_CONFIGURATION_SAVED");
        }
      }
    }

    private String getConfigurationFilename() {
        return System.getProperty( "user.home" )+
                System.getProperty( "file.separator" )+
                APPLICATION_NAME+".conf"+".xml";
    }

  @Override
  public void setMathShoulder(double mass, double length, double torque) {
    config.setMathShoulderMass(mass);
    config.setMathShoulderLength(length);
    config.setMathShoulderTorque(torque);
  }

  @Override
  public void setMathWrist(double mass, double length, double torque) {
    config.setMathWristMass(mass);
    config.setMathWristLength(length);
    config.setMathWristTorque(torque);
  }

  @Override
  public void setMathElbow(double mass, double length, double torque) {
    config.setMathElbowMass(mass);
    config.setMathElbowLength(length);
    config.setMathElbowTorque(torque);
  }

  @Override
  public void setClampLength(double len) {
    config.setClampLength(len);
  }

  @Override
  public void setElbowLength(double len) {
    config.setElbowLength(len);
  }

  @Override
  public void setShoulderLength(double len) {
    config.setShoulderLength(len);
  }

  @Override
  public void setBaseHeight(double len) {
    config.setBaseHeight(len);
  }

  @Override
  public void setGravity(double gravity) {
    config.setGravity(gravity);
  }

  @Override
  public void setTimeBetweenCommands(int timeBetweenCommands) {
    config.setTimeBetweenCommands(timeBetweenCommands);
    dispatcher.setTimeBetweenCommands(timeBetweenCommands);
  }

  @Override
  public void setLogFilePathname(String pathname) {
    config.setLogFilePathname(pathname);
  }

  public boolean isSimulation() throws Exception {
    if (dispatcher == null) {
      throw new Exception(
              SixDOFArmResources.getString("EXEPTION_DISPATCHER_NOT_AVAILABLE")
      );
    }
    return dispatcher.isSimulation();
  }

  public void setSimulation(boolean simulation) throws Exception {
    if (dispatcher == null) {
      throw new Exception(
              SixDOFArmResources.getString("EXEPTION_DISPATCHER_NOT_AVAILABLE")
      );
    }
    dispatcher.setSimulation(simulation);
  }

  /**
   * Log a message.
   * <p>
   * If the logger is currently enabled for the given message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   *
   * @param   level   One of the message level identifiers, e.g., SEVERE
   * @param   msg     The string message (or a key in the message catalog)
   */
  public void log(Level level, String msg) {
    logger.log(level, msg);
  }
  
  public void log(Level level, String msg, Object param1) {
    logger.log(level, SixDOFArmResources.getString(msg), param1);
  }

  /**
   * 
   * @param program
   *        Text of a Pierin-oh! program to be executed.
   *        Every command has to terminate with a LINE-FEED (\n)
   */
  @Override
  public void runProgram(String program) {
    if (isArmConnected()) {
      String[] lines = program.split(LF);
      PierinhoLanguage lang = new PierinhoLanguage(this);
      String pathname = ui.getProgramPathname();
      if (pathname == null || pathname.length() == 0) {
        pathname = SixDOFArmResources.getString("COMMON_UNKNOWN");
      }
      logger.log(
              Level.INFO, 
              SixDOFArmResources.getString("LOG_EXECUTING_PROGRAM"), 
              pathname
      );
      ui.notifyProgramMessage(
              Level.INFO,
              SixDOFArmResources.getString("LOG_EXECUTING_PROGRAM")
      );
      int len = lines.length;
      boolean aborted = false;
      for (int i = 0; i < len; i++) {
        if (!lines[i].matches("^\\s*$")) {
          if (!PierinhoLanguage.isComment(lines[i])) {
            if (!lang.executeCommand(lines[i], i)) {
              aborted = true;
              break;
            }
          }
        }
      }
      if (aborted) {
        logger.log(
                Level.SEVERE, 
                SixDOFArmResources.getString("LOG_ABORTED_PROGRAM"),
                pathname
        );
        ui.notifyProgramMessage(
                Level.SEVERE, 
                SixDOFArmResources.getString("LOG_EXECUTING_PROGRAM"),
                pathname
        );
      } else {
        logger.log(
                Level.INFO, 
                SixDOFArmResources.getString("LOG_FINISHED_EXECUTING_PROGRAM"),
                pathname
        );
        ui.notifyProgramMessage(
                Level.SEVERE, 
                SixDOFArmResources.getString("LOG_EXECUTING_PROGRAM"),
                pathname
        );
      }
    } else {
      ui.showError(program);
    }
  }
}
