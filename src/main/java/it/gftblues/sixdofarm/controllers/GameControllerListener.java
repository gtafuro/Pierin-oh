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
 * Game Controller listener.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */

public interface GameControllerListener {

  /**
   * Analog action performed.
   * 
   * @param component
   *        The game controller component that has triggered the action.
   * @param value
   *        The value of the component.
   */
  public void analogActionPerformed(String component, float value);

  /**
   * Digital action performed.
   * 
   * @param component
   *        The game controller component that has triggered the action.
   * @param value
   *        The value of the component. {@code true} means that the component
   *        has been used (for instance a button has been pressed).
   *        {@code false} means that the component is not used (for instance a 
   *        button has been released).
   */
  public void digitalActionPerformed(String component, boolean value);
}
