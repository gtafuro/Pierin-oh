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
 * <p>The {@code CollisionDetector} is used to test whether moving the robotic 
 * arm from one position to another one would cause a collision of the clamp's 
 * tip with any part of the robotic arm itself or with the plane where it lies 
 * on.</p>
 * 
 * <p>The {@code CollisionDetector} does not allow the collocation of the 
 * clamp's tip below the plane where the robotic arm lies on.</p>
 * 
 * <p>At present, the {@code CollisionDetector} capabilities are very limited 
 * and it has to be improved.</p>
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class CollisionDetector {

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
   * Default constructor.
   */
  CollisionDetector() {
  }

  /**
   * Returns the height of the base.
   * @return The height of the base.
   */
  public double getBaseHeight() {
    return baseHeight;
  }

  /**
   * Sets the height of the base.
   * @param baseHeight 
   *        The height of the base.
   */
  public void setBaseHeight(double baseHeight) {
    this.baseHeight = baseHeight;
  }

  /**
   * Returns the length of the shoulder.
   * @return The length of the shoulder.
   */
  public double getShoulderLength() {
    return shoulderLength;
  }

  /**
   * Sets the length of the shoulder.
   * @param shoulderLength 
   *        The height of the shoulder.
   */
  public void setShoulderLength(double shoulderLength) {
    this.shoulderLength = shoulderLength;
  }

  /**
   * Returns the length of the elbow.
   * @return The length of the elbow.
   */
  public double getElbowLength() {
    return elbowLength;
  }

  /**
   * Sets the length of the elbow.
   * @param elbowLength 
   *        The height of the elbow.
   */
  public void setElbowLength(double elbowLength) {
    this.elbowLength = elbowLength;
  }

  /**
   * Returns the length of the clamp.
   * @return The length of the clamp.
   */
  public double getClampLength() {
    return clampLength;
  }

  /**
   * Sets the length of the clamp.
   * @param clampLength 
   *        The height of the clamp.
   */
  public void setClampLength(double clampLength) {
    this.clampLength = clampLength;
  }
  
  /**
   * Calculates the horizontal distance of the clamp's tip.
   * @param shDeg
   *        The angle where the shoulder should be moved to.
   * @param elDeg
   *        The angle where the elbow should be moved to.
   * @param wrDeg
   *        The angle where the wrist should be moved to.
   * @return The horizontal distance from the centre of the arm.
   */
  public double horizontalDistance(int shDeg, int elDeg, int wrDeg) {
    double shSin = Math.sin(Math.toRadians(shDeg));
    double elSin = Math.sin(Math.toRadians(shDeg+elDeg));
    double weSin = Math.sin(Math.toRadians(shDeg+elDeg+wrDeg));

    return   shoulderLength*shSin
            +elbowLength*elSin
            +clampLength*weSin;
  }
  
  /**
   * Calculates the vertical distance of the clamp's tip from the plane where 
   * the robotic arm lies on.
   * @param shDeg
   *        The angle where the shoulder should be moved to.
   * @param elDeg
   *        The angle where the elbow should be moved to.
   * @param wrDeg
   *        The angle where the wrist should be moved to.
   * @return The vertical distance of the clamp's tip from the plane where the 
   *         robotic arm lies.
   */
  public double verticalDistance(int shDeg, int elDeg, int wrDeg) {
    double shCos = Math.cos(Math.toRadians(shDeg));
    double elCos = Math.cos(Math.toRadians(shDeg+elDeg));
    double wrCos = Math.cos(Math.toRadians(shDeg+elDeg+wrDeg));
  
    return   baseHeight
            +shoulderLength*shCos
            +elbowLength*elCos
            +clampLength*wrCos;
  }

  /**
   * Simulates the moving of the robotic arm from a position to another and
   * forecasts a possible collision.
   * @param shFrom
   *        The present angle where the shoulder is currently positioned.
   * @param elFrom
   *        The present angle where the elbow is currently positioned.
   * @param wrFrom
   *        The present angle where the wrist is currently positioned.
   * @param shTo
   *        The angle where the shoulder should be moved to.
   * @param elTo
   *        The angle where the elbow should be moved to.
   * @param wrTo
   *        The angle where the wrist should be moved to.
   * @return {@code true} if there will be a collision, {@code false} otherwise. 
   */
  public boolean collision(
          int shFrom, 
          int elFrom, 
          int wrFrom, 
          int shTo,
          int elTo,
          int wrTo
  ) {
    double ver = verticalDistance(shTo, elTo, wrTo);
    if (ver > 0) {
      double hor = horizontalDistance(shTo, elTo, wrTo);
      double prevHor = horizontalDistance(shFrom, elFrom, wrFrom);
/*      System.out.println(String.format(
              "----------\n"+
              "\nshFrom = %d"+
              "\nelFrom = %d"+
              "\nwrFrom = %d"+
              "\nshTo = %d"+
              "\nelTo = %d"+
              "\nwrTo = %d"+
              "\ntotal = %d"+
              "\nhor = %.3f"+
              "\nver = %.3f"+
              "\nprevHor = %.3f", 
              shFrom, 
              elFrom, 
              wrFrom, 
              shTo, 
              elTo, 
              wrTo, 
              (shTo+elTo+wrTo), 
              hor*1000.0d, 
              ver*1000.0d, 
              prevHor)
      );*/
      if (
              ver >= baseHeight || 
              (hor >= 0 && prevHor >= 0) || 
              (hor < 0 && prevHor < 0)
      ) {
        return false;
      }
    }
    
    return true;
  }
  
  /**
   * Ignore.  Not a real entry point.  Used only for testing reasons.
   * @param args
   *        Arguments not used.
   */
  public static void main(String args[]) {
    SixDOFArmConfiguration conf = new SixDOFArmConfiguration();
    CollisionDetector cd = new CollisionDetector();
    cd.setBaseHeight(conf.getBaseHeight());
    cd.setClampLength(conf.getClampLength());
    cd.setElbowLength(conf.getElbowLength());
    cd.setShoulderLength(conf.getShoulderLength());
    for (int i = -90; i <= 90; i++) {
/*      System.out.println(
              String.format(
                      "%d\t%d\t%d\t%.03f\t%.03f", 
                      i, 
                      0, 
                      0, 
                      cd.horizontalDistance(0, 0, i),
                      cd.verticalDistance(0, 0, i)
              )
        );*/
    }
  }
}
