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
 * {@code Dimension} provides width and height of a 2d object.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class Dimension {

  /**
   * Width
   */
  private int width;
  
  /**
   * Height
   */
  private int height;

  /**
   * Default constructor.
   */
  public Dimension() {
  }

  /**
   * Constructor.
   * 
   * @param width
   *        Dimension width.
   * @param height 
   *        Dimension height.
   */
  public Dimension(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Get the value of height
   *
   * @return the value of height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Set the value of height
   *
   * @param height new value of height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Get the value of width
   *
   * @return the value of width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Set the value of width
   *
   * @param width new value of width
   */
  public void setWidth(int width) {
    this.width = width;
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
    hash = 37 * hash + this.width;
    hash = 37 * hash + this.height;
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
    final Dimension other = (Dimension) obj;
    if (this.width != other.width) {
      return false;
    }
    return this.height == other.height;
  }
}
