package it.gftblues.sixdofarm;

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

import it.gftblues.sixdofarm.controllers.PointerDevice;
import java.util.Set;
import it.gftblues.sixdofarm.ui.SixDOFArmControllerUI;

/**
 * An interface for PointerController objects used to let a UI to communicate
 * with it.
 * 
 * Please remember that each servo can move from 0° to 180°. 
 *
 * @author Gabriele Tafuro
 *
 * @see PointerDevice
 *
 * @since 1.0
 */
public interface SixDOFArmControllerInterface {

  /**
   * Provides an array of serial port names.
   * 
   * @return an array of serial port names.
   */
  public String[] getSerialPortNames();  

  /**
   * Gets the Pointer defined String that signifies that no serial port is 
   * available.
   * 
   * @return The 'no available serial port' string.
   */
  public String getNoPortAvailableString();
  
  /**
   * Sets a user interface by which the Pointer can communicate with the 
   * utilizer.
   * 
   * @param ui
   *        A reference to a {@code SixDOFArmControllerUI} user interface.
   */
  public void setUI(SixDOFArmControllerUI ui);

  /**
   * Notifies that the pointing device (mouse) has moved.
   * Eventually, this method should comprise all devices that are used to move
   * the pointer.
   * 
   * @param evt
   *        a {@code PointDevice} event
   */
  @Deprecated
  public void pointerDeviceHasMoved(PointerDevice evt);
  
  /**
   *  Gets the used serial port name.
   * 
   * @return  the use serial port name.
   */
  public String getSerialPortName();
  
  /**
   * Connects to the robotic arm.
   */
  public void connectArm();
  
  /**
   * Disconnects from the robotic arm.
   */
  public void disconnectArm();
  
  /**
   * Checks whether the robotic arm is connected.
   * 
   * @return {@code true} if connected, {@code false} otherwise.
   */
  public boolean isArmConnected();
  
  /**
   * Moves the robotic arm to the left hand side, horizontally to the ground.
   */
  @Deprecated
  public void moveLeft();
  
  /**
   * Moves the robotic arm to the right hand side, parallel to the ground.
   */
  @Deprecated
  public void moveRight();

  /**
   * Moves the robotic arm up, facing the front.
   */
  @Deprecated
  public void moveUp();

  /**
   * Moves the robotic arm down, facing the front.
   */
  @Deprecated
  public void moveDown();
  
  /**
   * Moves the robotic arm to the front, parallel to the ground.
   */
  @Deprecated
  public void moveToFront();

  /**
   * Arbitrarily robotic moves the arm at {@code x} degrees horizontally and {@code y}
   * degrees vertically.
   * 
   * @param x
   *        the horizontal degrees where to move (from 0 to 180).
   * @param y 
   *        the vertical degrees where to move (from 0 to 180).
   */
  @Deprecated
  public void moveTo(int x, int y);
  
  /**
   * Moves the robotic arm to the off position.
   * It is has the same effect of {@code moveTO(90, 18)}.
   */
  public void moveToOffPosition();
  
  /**
   * Sends a {@code command} to the robotic arm.
   * 
   * For instance, to move the arm 85° horizontally and 25° vertically, we can
   * either use {@code moveTo(85, 25)} or {@code sendCommand("085,025\n")} 
   * (please note the leading 0 for sending 25°: all numbers must be of  of 
   * three digits). A further example: for toggling the green led on the tip of 
   * the arm, we can send the command {@code toggleGreenLed()} or {@code 
   * sendCommand('G')}.
   * 
   * @param command
   *        a {@code String} containing the command.
   */
  public void sendCommand(String command);
  
  /**
   * Forces to look for game controllers.
   */
  public void searchForGameControllers();
  
  /**
   * Returns a set of active game controllers.
   * 
   * @return A {@code Set<String>} with controller's names.
   */
  public Set<String> getGameControllerNames();
  
  /**
   * Enable/disable game controllers.
   * 
   * Please note that if game controllers are active, the arm will, when moved,
   * always return to the default game controller position.
   * 
   * @param enabled 
   *        If set to {@code trure} will enable the game controllers, {@code 
   *        false} will disable them.
   */
  public void enableGameControllers(boolean enabled);

  /**
   * Verifies whether game controllers are enabled.
   * 
   * @return {@code true} if game controllers are enabled, {@code false} 
   * otherwise.
   */
  public boolean areGameControlelrsEnabled();
  
  /**
   * Notifies to the {@code SixDOFArmControllerInterface} implementation thar the
   * user interface is ready to interact with both user and the
   * {@code SixDOFArmControllerInterface} itself.
   */
  public void notifyUIReady();

  /**
   * Tells to the {@code SixDOFArmControllerInterface} whether to use the
   * face-recognition feature (if available) or not.
   * 
   * @param use
   *        {@code true} will enable face-recognition, {@code false} will 
   *        disable it.
   */
  public void useFaceRecognition(boolean use);
  
  /**
   * Sets the classifier file path.
   * 
   * The classifier is an XML file required for the use of face-recognition.
   * 
   * @param absolutePath
   *        The path for the classifier file.
   */
  public void setClassifierFilePath(String absolutePath);
  
  /**
   * Notifies that it is required to put the arm in the rest (off) position.
   * 
   * Upon receiving this notification the {@code SixDOFArmControllerInterface} may
   * not take any action (it depends on the arm).  
   */
  public void shutdown();

  /**
   * Commands the robotic arm to close its clamp.
   */
  public void closeClamp();

  /**
   * Commands the robotic arm to open its clamp.
   */
  public void openClamp();
  
  /**
   * Commands the robotic arm to set its clamp aperture.
   * @param degrees
   *        The degrees that the arm ha to be set to.
   */
  public void setClamp(int degrees);
  
  /**
   * Commands the robotic arm to rotate its wrist to a specific angle. 
   * @param rotation
   *        The angle of rotation.
   */
  public void rotateWrist(int rotation);

  /**
   * Commands the robotic arm to move horizontally its shoulder.
   * @param deg
   *        The angle of the new position.
   */
  public void moveShoulderHorizontally(int deg);

  /**
   * Commands the robotic arm to move its shoulder vertically.
   * 
   * @param deg
   *        The angle of the new position.
   * @return {@code true} if the collision detection system allows the movement,
   *         {@code false} otherwise.
   */
  public boolean moveShoulderVertically(int deg);

  /**
   * Commands the robotic arm to move its elbow vertically.
   * 
   * @param deg
   *        The angle of the new position.
   * @return {@code true} if the collision detection system allows the movement,
   *         {@code false} otherwise.
   */
  public boolean moveElbowVertically(int deg);
  
  /**
   * Commands the robotic arm to move its wrist vertically.
   * 
   * @param deg
   *        The angle of the new position.
   * @return {@code true} if the collision detection system allows the movement,
   *         {@code false} otherwise.
   */
  public boolean moveWristVertically(int deg);

  /**
   * Sets the mass (weight) that the shoulder has to lift along with its length 
   * (in meters) and the servo motor torque (in kg). 
   * @param mass
   *        The mass (weight in kg) that has to be lifted.
   * @param length
   *        The length of the shoulder (in meters).
   * @param torque 
   *        The torque (in kg) of the servo motor.
   */
  public void setMathShoulder(double mass, double length, double torque);

  /**
   * Sets the mass (weight) that the wrist has to lift along with its length 
   * (in meters) and the servo motor torque (in kg). 
   * @param mass
   *        The mass (weight in kg) that has to be lifted.
   * @param length
   *        The length of the wrist (in meters).
   * @param torque 
   *        The torque (in kg) of the servo motor.
   */
  public void setMathWrist(double mass, double length, double torque);

  /**
   * Sets the mass (weight) that the elbow has to lift along with its length 
   * (in meters) and the servo motor torque (in kg). 
   * @param mass
   *        The mass (weight in kg) that has to be lifted.
   * @param length
   *        The length of the elbow (in meters).
   * @param torque 
   *        The torque (in kg) of the servo motor.
   */
  public void setMathElbow(double mass, double length, double torque);

  /**
   * Sets the clamp's length.
   * @param len 
   *        The length of the clamp (in meters).
   */
  public void setClampLength(double len);

  /**
   * Sets the elbow's length.
   * @param len 
   *        The length of the clamp (in meters).
   */
  public void setElbowLength(double len);

  /**
   * Sets the shoulder's length.
   * @param len 
   *        The length of the clamp (in meters).
   */
  public void setShoulderLength(double len);

  /**
   * Sets the base's height.
   * @param len 
   *        The length of the clamp (in meters).
   */
  public void setBaseHeight(double len);

  /**
   * Sets the gravity of the celestial body where the software is used.
   * 
   * @param gravity
   *        The gravity value.
   */
  public void setGravity(double gravity);

  /**
   * Sets the minimum time that has to pass between commands sent to the robotic
   * @param timeBetweenCommands
   *        The time in milliseconds.
   */
  public void setTimeBetweenCommands(int timeBetweenCommands);

  /**
   * Sets the full log file pathname.
   * @param pathname The log file pathname.
   */
  public void setLogFilePathname(String pathname);

  /**
   * Saves the software configuration.
   */
  public void saveConfiguration();

  public void runProgram(String program);
}
