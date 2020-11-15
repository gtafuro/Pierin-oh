package it.gftblues.sixdofarm.joints;

import it.gftblues.sixdofarm.PierinhoCommand;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
 * Base class that represents a joint of the robotic arm with its actions and
 * min and max value (degrees) associated with each servo motor.
 * 
 * This is useful to provide a consistent way throughout the code to describe
 * the robotic arm parts, related actions and limits.
 *
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class Joint implements Comparable<Joint> {

  /**
   * Joint's name.
   */
  private String name;
  
  /**
   * Joint's actions set.
   */
  private final Set<String> actions = new HashSet<>();
  
  /**
   * Joint's commands map.
   */
  private final Map<String, PierinhoCommand> commands = new HashMap<>();

  /**
   * Gets the name.
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   * @param name The name.
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the action's set.
   * @return The action's set.
   */
  public final Set<String> getActions() {
    return actions;
  }

  /**
   * Gets the command's map.
   * @return 
   */
  public final Map<String, PierinhoCommand> getCommands() {
    return commands;
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
    hash = 83 * hash + Objects.hashCode(this.name);
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
    final Joint other = (Joint) obj;
    return Objects.equals(this.name, other.name);
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
  public int compareTo(Joint o) {
    if (o == null) {
      throw new NullPointerException();
    }
    return name.compareTo(o.name);
  }
}
