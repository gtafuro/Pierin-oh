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

/**
 * Data configuration for the robotic arm handling.
 *
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class SixDOFArmConfiguration {
  /**
   * The value of Earth's gravity.
   */
  public static final double EARTH_GRAVITY = 9.807d;
  
  /**
   * Default minimum time that has to pass between to commands
   */
  public static final int DEFAULT_TIME_BETWEEN_COMMANDS = 600; //  110ms is the registered minimum speed that allow fluid work.
  
  /**
   * The gravity value to use for calculations.
   */
  private double gravity = EARTH_GRAVITY;

  /**
   * A default name for a classifier file.
   */
  private String opencvClassifierPathname = "lbpcascade_frontalface.xml";

  /**
   * The height of the base.
   */
  private double baseHeight = 0.135d;
  
  /**
   * The length of the shoulder.
   */
  private double shoulderLength = 0.105d;

  /**
   * The length of the elbow.
   */
  private double elbowLength = 0.100d;

  /**
   * The length of the clamp.
   */
  private double clampLength = 0.155d;

  /**
   * The value of mass the shoulder should be able to lift used in the 
   * calculations.
   */
  private double mathShoulderMass = 7.28d;

  /**
   * The length of the shoulder used in the calculations.
   */
  private double mathShoulderLength = 0.35d;

  /**
   * The torque of the servo motor of the shoulder used in the calculations.
   */
  private double mathShoulderTorque = 25.0d;
  
  /**
   * The value of mass the elbow should be able to lift used in the 
   * calculations.
   */
  private double mathElbowMass = 3.83d;

  /**
   * The length of the elbow used in the calculations.
   */
  private double mathElbowLength = 0.25d;

  /**
   * The torque of the servo motor of the elbow used in the calculations.
   */
  private double mathElbowTorque = 9.40d;
  
  /**
   * The value of mass the wrist should be able to lift used in the 
   * calculations.
   */
  private double mathWristMass = 7.98f;

  /**
   * The length of the wrist used in the calculations.
   */
  private double mathWristLength = 0.12f;

  /**
   * The torque of the servo motor of the wrist used in the calculations.
   */
  private double mathWristTorque = 9.40f;
  
  /**
   * Minimum time that has to pass between to commands
   */
  private int timeBetweenCommands = DEFAULT_TIME_BETWEEN_COMMANDS;
  
  /**
   * Log file pathname.
   */
  private String logFilePathname = null;

  /**
   * Default constructor.
   */
  public SixDOFArmConfiguration() {
  }
  
  /**
   * Constructor.
   * @param rhs 
   *        The object to copy.
   */
  public SixDOFArmConfiguration(SixDOFArmConfiguration rhs) {
    gravity = rhs.gravity;

    opencvClassifierPathname = rhs.opencvClassifierPathname;

    baseHeight = rhs.baseHeight;
    shoulderLength = rhs.shoulderLength;
    elbowLength = rhs.elbowLength;
    clampLength = rhs.clampLength;

    mathShoulderMass = rhs.mathShoulderMass;
    mathShoulderLength = rhs.mathShoulderLength;
    mathShoulderTorque = rhs.mathShoulderTorque;

    mathElbowMass = rhs.mathElbowMass;
    mathElbowLength = rhs.mathElbowLength;
    mathElbowTorque = rhs.mathElbowTorque;

    mathWristMass = rhs.mathWristMass;
    mathWristLength = rhs.mathWristLength;
    mathWristTorque = rhs.mathWristTorque;
  }

  /**
   * Gets the gravity value.
   * @return The gravity value.
   */
  public double getGravity() {
    return gravity;
  }

  /**
   * Sets the gravity value.
   * @param gravity 
   *        The gravity value.
   */
  public void setGravity(double gravity) {
    this.gravity = gravity;
  }

  /**
   * Gets the OpenCV classifier pathname.
   * @return The OpenCV classifier pathname.
   */
  public String getOpencvClassifierPathname() {
    return opencvClassifierPathname;
  }

  /**
   * Sets the OpenCV classifier pathname.
   * @param opencvClassifierPathname
   *        The OpenCV classifier pathname.
   */
  public void setOpencvClassifierPathname(String opencvClassifierPathname) {
    this.opencvClassifierPathname = opencvClassifierPathname;
  }

  /**
   * Gets the height of the base.
   * @return The height of the base. 
   */
  public double getBaseHeight() {
    return baseHeight;
  }

  /**
   * Sets the height of the base.
   * @param height The height of the base.
   */
  public void setBaseHeight(double height) {
    this.baseHeight = height;
  }

  /**
   * Gets the shoulder length.
   * @return The shoulder length.
   */
  public double getShoulderLength() {
    return shoulderLength;
  }

  /**
   * Sets the shoulder length.
   * @param length 
   *        The shoulder length.
   */
  public void setShoulderLength(double length) {
    this.shoulderLength = length;
  }

  /**
   * Gets the elbow length.
   * @return The elbow length.
   */
  public double getElbowLength() {
    return elbowLength;
  }

  /**
   * Sets the elbow length.
   * @param length The elbow length.
   */
  public void setElbowLength(double length) {
    this.elbowLength = length;
  }

  /**
   * Gets the length of the clamp.
   * @return The length of the clamp.
   */
  public double getClampLength() {
    return clampLength;
  }

  /**
   * Sets the length of the clamp.
   * @param length
   *        The length of the clamp.
   */
  public void setClampLength(double length) {
    this.clampLength = length;
  }

  /**
   * Gets the mass of the shoulder.
   * @return The mass of the shoulder.
   */
  public double getMathShoulderMass() {
    return mathShoulderMass;
  }

  /**
   * Sets the mass of the shoulder.
   * @param mass 
   *        The mass of the shoulder.
   */
  public void setMathShoulderMass(double mass) {
    this.mathShoulderMass = mass;
  }

  /**
   * Gets the length of the shoulder used for the calculations.
   * @return The length of the shoulder used for the calculations.
   */
  public double getMathShoulderLength() {
    return mathShoulderLength;
  }

  /**
   * Sets the length of the shoulder used for the calculations.
   * @param length The length of the shoulder used for the calculations.
   */
  public void setMathShoulderLength(double length) {
    this.mathShoulderLength = length;
  }

  /**
   * Gets the value of the torque of the shoulder used for calculations.
   * @return The value of the torque of the shoulder used for calculations.
   */
  public double getMathShoulderTorque() {
    return mathShoulderTorque;
  }

  /**
   * Gets the value of the torque of the shoulder used for calculations.
   * @param torque The value of the torque of the shoulder used for 
   * calculations.
   */
  public void setMathShoulderTorque(double torque) {
    this.mathShoulderTorque = torque;
  }

  /**
   * Gets the value of the mass of the elbow for calculations.
   * @return The value of the mass of the elbow for calculations.
   */
  public double getMathElbowMass() {
    return mathElbowMass;
  }

  /**
   * Sets the value of the mass of the elbow for calculations.
   * @param mass
   *        The value of the mass of the elbow for calculations.
   */
  public void setMathElbowMass(double mass) {
    this.mathElbowMass = mass;
  }

  /**
   * Gets the length of the elbow elbow for calculations.
   * @return The length of the elbow elbow for calculations.
   */
  public double getMathElbowLength() {
    return mathElbowLength;
  }

  /**
   * Sets the length of the elbow elbow for calculations.
   * @param length The length of the elbow elbow for calculations.
   */
  public void setMathElbowLength(double length) {
    this.mathElbowLength = length;
  }

  /**
   * Gets the value of the torque of the elbow used for calculations.
   * @return The value of the torque of the elbow used for calculations.
   */
  public double getMathElbowTorque() {
    return mathElbowTorque;
  }

  /**
   * Sets the value of the torque of the elbow used for calculations.
   * @param torque The value of the torque of the elbow used for calculations.
   */
  public void setMathElbowTorque(double torque ){
    this.mathElbowTorque = torque;
  }

  /**
   * Gets the value of the mass of the wrist for calculations.
   * @return The value of the mass of the wrist for calculations.
   */
  public double getMathWristMass() {
    return mathWristMass;
  }

  /**
   * Sets the value of the mass of the wrist for calculations.
   * @param mass The value of the mass of the wrist for calculations.
   */
  public void setMathWristMass(double mass) {
    this.mathWristMass = mass;
  }

  /**
   * Gets the length of the wrist elbow for calculations.
   * @return The length of the wrist elbow for calculations.
   */
  public double getMathWristLength() {
    return mathWristLength;
  }

  /**
   * Sets the length of the wrist elbow for calculations.
   * @param length The length of the wrist elbow for calculations.
   */
  public void setMathWristLength(double length) {
    this.mathWristLength = length;
  }

  /**
   * Gets the value of the torque of the wrist used for calculations.
   * @return The value of the torque of the wrist used for calculations.
   */
  public double getMathWristTorque() {
    return mathWristTorque;
  }

  /**
   * Sets the value of the torque of the wrist used for calculations.
   * @param torque The value of the torque of the wrist used for calculations.
   */
  public void setMathWristTorque(double torque) {
    this.mathWristTorque = torque;
  }

  /**
   * Gets the minimum time that has to pass between commands sent to the robotic
   * arm.
   * @return The time in milliseconds.
   */
  public int getTimeBetweenCommands() {
    return timeBetweenCommands;
  }

  /**
   * Sets the minimum time that has to pass between commands sent to the robotic
   * arm.
   * @param timeBetweenCommands
   *        The time in milliseconds.
   */
  public void setTimeBetweenCommands(int timeBetweenCommands) {
    this.timeBetweenCommands = timeBetweenCommands;
  }

  /**
   * Gets the full log file pathname.
   * @return The log file pathname.
   */
  public String getLogFilePathname() {
    return logFilePathname;
  }

  /**
   * Sets the full log file pathname.
   * @param pathname The log file pathname.
   */
  public void setLogFilePathname(String pathname) {
    this.logFilePathname = pathname;
  }
}
