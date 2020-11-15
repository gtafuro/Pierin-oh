package it.gftblues.sixdofarm.joints;

import it.gftblues.sixdofarm.PierinhoCommand;
import java.util.Map;
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
 * Elbow joint.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class Elbow  extends Joint {
  
  /**
   * Action Move Elbow Vertically.
   */
  public static final String ACTION_EV = "EV";


  /**
   * Constructor.
   */
  public Elbow() {
    Set<String> actions = getActions();
    Map<String, PierinhoCommand> commands = getCommands();
    
    setName("elbow");
    String action;
    action = ACTION_EV;
    actions.add(action);
    commands.put(action, new PierinhoCommand(action, -90, 90));
  }
}
