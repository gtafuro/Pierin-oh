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
 * SerialCommunicatorListener
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */

public interface SerialCommunicatorListener {
  /**
   * Notifies to the listener that an error has occurred.
   * 
   * @param error
   *        The error to be notified.
   */
  public void notifyError(String error);

  /**
   * Notifies to the listener that there is a new message from the arm.
   * 
   * Often, messages are just the feedback of that actually the arm has done or 
   * the state of the arm itself or its parts.
   * 
   * @param message
   *        The message to be notified.
   */
  public void notifyMessage(String message);
}
