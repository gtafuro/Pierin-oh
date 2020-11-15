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

/**
 * PointerDevice provides an high-level object to pass to Pointer to determine
 * its behaviour.
 * 
 * It provides x, y (maybe also a 3rd dimension, in the future) position 
 * (or degrees) and an array of int containing the number of times each given 
 * button has been repeatedly pressed.
 * 
 * For instance, if the given device has three buttons, there will be an int 
 * array of size 3, one element for each button reporting the number of times
 * each button has been pressed in fast sequence.
 *
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class PointerDevice {

  /**
   * The x value.
   */
  private int x;

  /**
   * The y value.
   */
  private int y;


  /**
   * The buttons statuses.
   */
  private int[] buttons;

  /**
   * Default constructor.
   */
  public PointerDevice() {
    x = 0;
    y = 0;
    buttons = null;
  }

  /**
   * Constructor.
   * @param x The x value.
   * @param y The y value.
   */
  public PointerDevice(int x, int y) {
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
   * @param x The x value.
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
   * @param y The y value.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Gets the statuses of the pointer's buttons.
   * @return The statuses of the buttons.
   */
  public int[] getButtons() {
    return buttons;
  }

  /**
   * Sets the statuses of the pointer's buttons.
   * @param buttons The statuses of the pointer's buttons.
   */
  public void setButtons(int[] buttons) {
    this.buttons = buttons;
  }
}
