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

import java.util.Objects;

/**
 * {@code ArmCommand} contains a command that has to be sent to the arm along 
 * with a unique id.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 * 
 */
public class ArmCommand implements Comparable<ArmCommand>{
  
  /**
   * The command to be sent to the robotic arm.
   */
  private final String command;
  
  /**
   * The id which should be unique for each {@code command} issued.
   */
  private final String id;

  /**
   * Constructor
   * @param command
   *        The command that has to be sent to the robotic arm.
   * @param id
   *        A unique id that identifies the command. The uniqueness of the id 
   *        must be granted from the {@code ArmCommand} generator.
   */
  public ArmCommand(String command, String id) {
    this.command = command;
    this.id = id;
  }
  
  /**
   * Default constructor.
   * 
   * <strong>Note</strong>
   * <p>Made private to avoid its use.</p>
   */
  private ArmCommand() {
    this.command = null;
    this.id = null;
  }

  /**
   * Copy constructor
   * 
   * @param rhs
   *        The {@code ArmCommand} object to be copied.
   * 
   * Note: it is an exact copy of the original, hence the {@code id} is also 
   * identical.
   * 
   * <strong>Note</strong>
   * <p>Made private to avoid its use.</p>
   */
  private ArmCommand(ArmCommand rhs) {
    this.command = rhs.command;
    this.id = rhs.id;
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
    hash = 97 * hash + Objects.hashCode(this.id);
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
    final ArmCommand other = (ArmCommand) obj;
    return Objects.equals(this.id, other.id);
  }

  /**
   * returns the {@code command} to be sent to the robotic arm.
   * 
   * @return {@code command}
   */
  public String getCommand() {
    return command;
  }

  /**
   * returns the {@code id} related to the {@code command}.
   * 
   * @return {@code command}
   */
  public String getId() {
    return id;
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
  public int compareTo(ArmCommand o) {
    if (o == null) {
      throw new NullPointerException();
    }
    return id.compareTo(o.id);
  }

  /**
   * <p>Returns a {@code String} in the format:</p>
   * <p><strong>command</strong>&lt;space&gt;[<strong>id</strong>]</p>
   * Example: <blockquote><pre>SV90 [12345678901]</pre></blockquote>
   * @return a formatted {@code String} ready to be consumed.
   */
  public String getMessage() {
    return String.format("%s [%s]", command, id); 
  }
  
  @Override
  public String toString() {
    return String.format("Command:%s id:%s", command, id); 
  }
}
