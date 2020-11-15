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
 * Represents a Point in a 2d system coordinates.
 * 
 * In the future it could handle a third coordinate.
 *
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class Point {

  /**
   * The x dimension.
   */
  private int x;

  /**
   * The y dimension.
   */  private int y;

  /**
   * Default constructor
   */
  public Point() {
  }

  /**
   * Constructor.
   * @param x
   *        The x value.
   * @param y 
   *        The y value.
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the x value.
   * @return The x value.
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the x value.
   * @param x 
   *        The x value.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Gets the y value.
   * @return The y value.
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the y value.
   * @param y 
   *        The y value.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * <p>Returns a hash code value for the object. This method is
   * supported for the benefit of hash tables such as those provided by
   * {@link java.util.HashMap}.</p>
   *
   * <p><strong>Implementation notes</strong></p>
   * <p>As far as is reasonably practical, the {@code hashCode} method defined
   * by class {@code Object} returns distinct integers for distinct objects.</p>
   *
   * @return  a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + this.x;
    hash = 37 * hash + this.y;
    return hash;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param   obj   the reference object with which to compare.
   * @return  {@code true} if this object is the same as the obj
   *          argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Point other = (Point) obj;
    if (this.x != other.x) {
      return false;
    }
    return this.y == other.y;
  }  
}
