package it.gftblues.sixdofarm;

import java.util.Objects;

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
 * Command interpreted by the Pierin-oh! language.
 * 
 *
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class PierinhoCommand implements Comparable<PierinhoCommand> {

  /**
   * A command.
   */
  private final String command;
  
  /**
   * If {@code true} the command has a parameter, {@code false} otherwise.
   */
  private final boolean parameter;

  /**
   * The minimum value possible for the amount (degrees).
   */
  private final int min;

  /**
   * The maximum value possible for the amount (degrees).
   */
  private final int max;

  /**
   * Constructor
   * @param command
   *        A command of the Pierin-oh! language.
   * @param min
   *        The minimum valid value.
   * @param max
   *        The maximum valid value.
   */
  public PierinhoCommand(String command, int min, int max) {
    this.command = command;
    this.parameter = true;
    this.min = min;
    this.max = max;
  }
  
  /**
   * Constructor
   * @param command
   *        A command of the Pierin-oh! language.
   */
  public PierinhoCommand(String command) {
    this.command = command;
    this.parameter = false;
    this.min = 0;
    this.max = 0;
  }
  
  /**
   * The command has a parameter
   * @return {@code true} if the command has a parameter, {@code false}
   * otherwise.
   */
  public boolean hasParameter() {
    return parameter;
  }
  
  /**
   * Gets the command.
   * @return The command.
   */
  public String getCommand() {
    return command;
  }
  
  /**
   * Gets the minimum amount (degrees) that can be accepted.
   * @return The minimum amount (degrees) that can be accepted.
   */
  public int getMin() {
    return min;
  }

  
  /**
   * Gets the maximum amount (degrees) that can be accepted.
   * @return The maximum amount (degrees) that can be accepted.
   */
  public int getMax() {
    return max;
  }

  /**
   * Generate the hash-code for the object.
   * @return The hash-code.
   */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.command);
    return hash;
  }

  /**
   * Compares an object with {@code this} one.
   * @param obj
   *        The object to be compared.
   * @return {@code true} if the {@code obj} is the same as {@code this} one, 
   * {@code false} otherwise.
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
    final PierinhoCommand other = (PierinhoCommand) obj;
    return Objects.equals(this.command, other.command);
  }

  /**
    * Compares this object with the specified object for order.  Returns a
    * negative integer, zero, or a positive integer as this object is less
    * than, equal to, or greater than the specified object.
    *
    * @return  a negative integer, zero, or a positive integer as this object
    *          is less than, equal to, or greater than the specified object.
    *
    * @throws NullPointerException if the specified object is null
    */
  @Override
  public int compareTo(PierinhoCommand o) {
    if (o == null) {
      throw new NullPointerException();
    }
    return command.compareTo(o.command);
  }
}
