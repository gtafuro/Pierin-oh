package it.gftblues.sixdofarm;

import it.gftblues.sixdofarm.joints.Clamp;
import it.gftblues.sixdofarm.joints.Elbow;
import it.gftblues.sixdofarm.joints.Joint;
import it.gftblues.sixdofarm.joints.Shoulder;
import it.gftblues.sixdofarm.joints.Wrist;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * The Pierin-oh! language is a trivial coding language to provide commands to
 * the robotic arm.
 *
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class PierinhoLanguage {

  /**
   * Line feed.
   */
  private static final String LF = "\n";

  /**
   * Index of the action reported in the {@code Array[]} returned from the 
   * {@code getValidCommand()} method.
   */
  public static final int COMMAND_ACTION = 0;

  /**
   * Index of the amount reported in the {@code Array[]} returned from the 
   * {@code getValidCommand()} method.
   */
  public static final int COMMAND_AMOUNT = 1;

  /**
   * Index of the possible error reported in the {@code Array[]} returned from the 
   * {@code getValidCommand()} method.
   */
  public static final int COMMAND_ERROR = 2;

  /**
   * String error raised for a command not found.
   */
  public static final String ERROR_NO_COMMAND_FOUND = "ERROR_NO_COMMAND_FOUND";

  /**
   * String error raised for an invalid command.
   */
  public static final String ERROR_INVALID_COMMAND = "ERROR_INVALID_COMMAND";

  /**
   * String error raised for an unexpected.
   */
  public static final String ERROR_UNEXPECTED_VALUE = "ERROR_UNEXPECTED_VALUE";

  /**
   * String error raised for an invalid value for a given command.
   */
  public static final String ERROR_INVALID_VALUE_FOR_COMMAND = 
          "ERROR_INVALID_VALUE_FOR_COMMAND";

  /**
   * String error raised for a possible collision if the command is executed.
   */
  public static final String ERROR_POSSIBLE_COLLISION =
          "ERROR_POSSIBLE_COLLISION";


  /**
   * Map with all valid commands used by the Pierin-oh! language.
   */
  private static final Map<String,PierinhoCommand> commands = populateCommands();

  /**
   * A reference to the arm controller.
   */
  private final SixDOFArmController armController;

  /**
   * Default constructor.
   */
  public PierinhoLanguage() {
    armController = null;
//    populateCommands();
  }

  /**
   * Constructor.
   * @param armController 
   *        The arm controller used for sending command to the robotic arm.
   */
  public PierinhoLanguage(SixDOFArmController armController) {
    this.armController = armController;
//    populateCommands();
  }

  /**
   * Runs the program contained in the file {@code pathname}.
   * @param pathname 
   *        The pathname of the program.
   */
  public void run(String pathname) { 
    if (armController == null) {
      return;
    }
    FileReader fr;
    try {
      fr = new FileReader(pathname);
    } catch (FileNotFoundException ex) {
      fr = null;
      Logger.getLogger(SixDOFArmController.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (fr != null) {
      try (BufferedReader reader = new BufferedReader(fr)) {
        String line;
        boolean endProgram = true;
        StringBuilder program = new StringBuilder();
        while(!endProgram && (line = reader.readLine()) != null) {
          program.append(line).append(LF);
        }
        armController.runProgram(program.toString());
      } catch (IOException ex) {
        Logger.getLogger(SixDOFArmController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  /**
   * Executes a command.
   * @param command
   *        The command to be executed.
   * @param line
   *        The line number of the program.
   * 
   * <strong>Note:</strong>
   * <p>The line is used only to report where an error is located.</p>
   * @return {@code true} if the command is successfully executed, {@code false} 
   * otherwise.
   */
  public boolean executeCommand(String command, int line) {
    if (armController == null) {
      return false;
    }
    String[] cmd = getValidCommand(command);
    int val;
    if (cmd[COMMAND_ERROR] == null) {
      String action = cmd[COMMAND_ACTION];
      val = Integer.parseInt(cmd[COMMAND_AMOUNT]);
      boolean allowed = true;
      if (action.compareTo(Shoulder.ACTION_SH) == 0) {
        armController.moveShoulderHorizontally(val);
      } else if (action.compareTo(Shoulder.ACTION_SV) == 0) {
        allowed = armController.moveShoulderVertically(val);
      } else if (action.compareTo(Elbow.ACTION_EV) == 0) {
        allowed = armController.moveElbowVertically(val);
      } else if (action.compareTo(Wrist.ACTION_WR) == 0) {
        armController.rotateWrist(val);
      } else if (action.compareTo(Wrist.ACTION_WV) == 0) {
        allowed = armController.moveWristVertically(val);
      } else if (action.compareTo(Clamp.ACTION_CO) == 0) {
        armController.openClamp();
      } else if (action.compareTo(Clamp.ACTION_CC) == 0) {
        armController.closeClamp();
      } else if (action.compareTo(Clamp.ACTION_CS) == 0) {
        armController.setClamp(val);
      } else {
        armController.notifyError(
                String.format(
                        armController.getExistingLabel(ERROR_INVALID_COMMAND),
                        command,
                        line+1
                )
        );
        return false;
      }
      if (!allowed) {
        armController.notifyError(
                String.format(
                        armController.getExistingLabel(ERROR_POSSIBLE_COLLISION),
                        command
                )
        );
        return false;
      }
    }
    return true;
  }

  /**
   * Gets a valid command.
   * @param command
   *        The command to be validated.
   * @return A {@code String} array where:
   * <ul>
   * <li>at the COMMAND_ACTION index is located the command;</li>
   * <li>at the COMMAND_AMOUT index is located the amount;</li>
   * <li>at the COMMAND_ERROR index is located the error (if the {@code command}
   * is invalid).</li>
   * </ul>
   */
  public static String[] getValidCommand(String command) {
    String retval[] = new String[3];
    Pattern commandPattern = Pattern.compile("^[A-Za-z]+");
    Pattern valuePattern = Pattern.compile("-?[0-9]+$");
    Matcher matcherCommand = commandPattern.matcher(command);
    Matcher matcherValue = valuePattern.matcher(command);
    int val = 0;
    boolean foundVal = false;

    if (matcherCommand.find()) {
      retval[COMMAND_ACTION] = matcherCommand.group(0);
      PierinhoCommand pc = commands.get(retval[COMMAND_ACTION].toUpperCase());
      if (pc != null) {
        if (matcherValue.find()) {
          val = Integer.parseInt(matcherValue.group(0));
          foundVal = true;
        }
        retval[COMMAND_AMOUNT] = ""+val;
        if (foundVal && !pc.hasParameter()) {
          retval[COMMAND_ERROR] = ERROR_UNEXPECTED_VALUE;
          return retval;
        }
        if (val < pc.getMin() || val > pc.getMax()) {
          retval[COMMAND_ERROR] = ERROR_INVALID_VALUE_FOR_COMMAND;
          return retval;
        }
      } else {
        retval[COMMAND_AMOUNT] = null;
        retval[COMMAND_ERROR] = ERROR_NO_COMMAND_FOUND;
        return retval;
      }
    } else {
      retval[COMMAND_ACTION] = null;
      retval[COMMAND_AMOUNT] = null;
      retval[COMMAND_ERROR] = ERROR_NO_COMMAND_FOUND;
    }
    retval[COMMAND_ERROR] = null;
    return retval;
  }
  
  /**
   * Gets a {@code String} array with all valid commands.
   * @return The {@code String} array with all valid commands.
   */
  public static String[] getCommandList() {
    return commands.keySet().toArray(new String[commands.size()]);
  }

  /**
   * Creates all valid commands for the Pierin-oh! language.
   * @return {@code Map<String,PierinhoCommand>} with the commands.
   */
  private static Map<String,PierinhoCommand> populateCommands() {
    Map<String,PierinhoCommand> cmds = new HashMap<>();
    if (cmds.isEmpty()) {
      Map<String, Joint> joints = SixDOFArmController.getJoints();
      joints.forEach((key, value) -> {
        cmds.putAll(value.getCommands());
      });
/*      Joint joint;

      joint = new Shoulder();
      cmds.putAll(joint.getCommands());
      joint = new Elbow();
      cmds.putAll(joint.getCommands());
      joint = new Wrist();
      cmds.putAll(joint.getCommands());
      joint = new Clamp();
      cmds.putAll(joint.getCommands());*/
/*
      commands.put(ACTION_SHOULDER_MOVE_HORIZONTALLY, 
              new PierinhoCommand(ACTION_SHOULDER_MOVE_HORIZONTALLY, -90, 90)
      );
      commands.put(ACTION_SHOULDER_MOVE_VERTICALLY, 
              new PierinhoCommand(ACTION_SHOULDER_MOVE_VERTICALLY, -90, 90)
      );
      commands.put(ACTION_ELBOW_MOVE_VERTICALLY, 
              new PierinhoCommand(ACTION_ELBOW_MOVE_VERTICALLY, -90, 90)
      );
      commands.put(ACTION_WRIST_ROTATE, 
              new PierinhoCommand(ACTION_WRIST_ROTATE, -90, 90)
      );
      commands.put(ACTION_WRIST_MOVE_VERTICALLY, 
              new PierinhoCommand(ACTION_WRIST_MOVE_VERTICALLY, -90, 90)
      );
      commands.put(ACTION_CLAMP_OPEN, 
              new PierinhoCommand(ACTION_CLAMP_OPEN)
      );
      commands.put(ACTION_CLAMP_CLOSE, 
              new PierinhoCommand(ACTION_CLAMP_CLOSE)
      );
      commands.put(ACTION_CLAMP_SET, 
              new PierinhoCommand(ACTION_CLAMP_SET, 20, 90)
      );*/
    }
    return cmds;
  }
  
  /**
   * Gets the {@code PierinhoCommand} referred to the {@code action}.
   * @param action
   *        The action for which the command is required.
   * @return If found, the {@code PierinhoCommand}, {@code null} otherwise. 
   */
  public static PierinhoCommand getCommand(String action) {
    return commands.get(action);
  }
  
  /**
   * Verifies if the {@code line} is a comment.
   * @param line
   *        The line to be tested.
   * @return {@code true} if a comment, {@code false} otherwise.
   */
  public static boolean isComment(String line) {
    if (line == null) {
      return false;
    }
    return line.matches("^\\s*#.*$");
  }
}
