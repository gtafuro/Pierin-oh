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

import it.gftblues.sixdofarm.controllers.SerialCommunicator;
import it.gftblues.sixdofarm.controllers.SerialCommunicatorListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dispatcher lies between the arm controller and the hardware.
 * 
 * Every command sent to the actual arm has to be sent to the dispatcher that
 * creates a unique id for every single command sent to the arm.
 * On completing the command the arm has to send, as a response, the related id.
 * 
 * On receiving the id the dispatcher will verify that no command has been 
 * ignored (all commands have been executed) and, in case of missing return id,
 * It will:
 *
 *     1.  write it to the log file;
 *     2.  signal it to the listeners.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class Dispatcher implements SerialCommunicatorListener {

  private static final String INFO_IGNORED_DATA_FROM_ARM =
          "INFO_IGNORED_DATA_FROM_ARM";
  private static final String LOG_COMMAND_EXECUTED = "LOG_COMMAND_EXECUTED";
  private static final String LOG_COMMAND_NOT_EXECUTED = 
          "LOG_COMMAND_NOT_EXECUTED";
  private static final String LOG_COMMAND_NOT_FOUND = "LOG_COMMAND_NOT_FOUND";
  private static final String LOG_COMMAND_SENT = "LOG_COMMAND_SENT";
  private static final String LOG_WRONG_DATA = "LOG_WRONG_DATA";
  private static final String LOG_CORRUPTED_DATA = "LOG_CORRUPTED_DATA";
  private static final String SYS_SIMULATING_COMMAND = "SYS_SIMULATING_COMMAND";

  /**
   * The initial {@code inFlow} capacity.
   */
  private static final int INITIAL_IN_FLOW_CAPACITY = 256;
  
  /**
   * Opening bracket character.
   */
  private static final char ID_OPENING_CHAR = '[';
  
  /**
   * Closing bracket character.
   */
  private static final char ID_CLOSING_CHAR = ']';
  
  /**
   * Line feed character.
   */
  private static final char LF = '\n';

  /**
   * Carriage Return character.
   */
  private static final char CR = '\r';

  /**
   * The arm controller;
   */
  private final SixDOFArmController armController;
  
  /**
   * The minimum time that has to pass between two commands to avoid to overrun 
   * the micro-controller speed in executing them.
   */
  private int timeBetweenCommands;

  /**
   * The id counter that will be appended to the next id.  The ids are computed
   * using the system milliseconds but, to make sure that all ids are unique,
   * the {@code nextIdCounter} is appended.
   */
  private static long nextIdCounter = 1L;

  /**
   * The FIFO list used to store all the commands in the issuing order.
   */
  private final LinkedList<ArmCommand> queue;
  
  /**
   * The serial communicator connected to the robotic arm.
   */
  private SerialCommunicator comm;
  
  /**
   * The flow of data coming from the robotic arm.
   */
  private final StringBuilder inFlow;
  
  /**
   * The data coming from the {@code inFlow} which is ignored.
   */
  private final StringBuilder ignored;
  
  /**
   * The current id coming from the robotic arm.
   */
  private StringBuilder currentId;
  
  /**
   * Set to {@code true} when reading an id, {@code false} otherwise.
   */
  private boolean composingId = false;
  
  /**
   * The current pos in the {@code inFlow}.
   */
  private int pos = 0;
  
  /**
   * If set to {@code true}, no actual data is sent to the serial communicator, 
   * {@code false} otherwise.
   */
  private boolean simulation = false;

  /**
   * When the robotic arm completes a command, it returns an acknowledge message
   * with the id of the executed command.  As the data flows in in chunks, the 
   * {@code inFlow} has to be analysed one character at the time and {@code ack}
   * serves as a 'sentinel' and to mark every step toward an acknowledge message 
   * completion.
   * 
   * The acknowledge message format is:<br>
   * ^ACK\s\[[0-9]+\]$
   * 
   * <p>When the character in {@code inFlow} pointed from {@code pos} is a 
   * line-feed {@code ack} becomes 1.</p>
   * <p>When the character in {@code inFlow} pointed from {@code pos} is a 
   * 'A' and {@code ack} is 1 then {@code ack} becomes 2.</p>
   * <p>When the character in {@code inFlow} pointed from {@code pos} is a 
   * 'C' and {@code ack} is 2 then {@code ack} becomes 3.</p>
   * <p>When the character in {@code inFlow} pointed from {@code pos} is a 
   * 'K' and {@code ack} is 3 then {@code ack} becomes 4.</p>
    * <p>When the character in {@code inFlow} pointed from {@code pos} is a 
   * space and {@code ack} is 4 then {@code ack} becomes 5.</p>
   * 
   * If, in any point of the chain the sequence is not respected, the ack
   * returns to 0.
   * 
   */
  private int ack = 0;

  /**
   * Constructor.
   * @param armController
   *        The arm controller.
   */
  public Dispatcher(SixDOFArmController armController) {
    this.armController = armController;
    comm = null;
    queue = new LinkedList<>();
    inFlow = new StringBuilder(INITIAL_IN_FLOW_CAPACITY);
    ignored = new StringBuilder(INITIAL_IN_FLOW_CAPACITY);
  }

  /**
   * Gets the {@code SerialCommunicator} used.
   * @return The {@code SerialCommunicator} used.
   */
  public SerialCommunicator getComm() {
    return comm;
  }

  /**
   * Sets the {@code SerialCommunicator} to be used.
   * @param comm
   *        The {code @SerialCommunicator} to be used.
   */
  public void setComm(SerialCommunicator comm) {
    if (this.comm != null) {
      this.comm.removeListener(this);
    }
    this.comm = comm;
    if (comm != null) {
      comm.addListener(this);
    }
  }

  /**
   * Checks whether is simulating the transmission.
   * @return {@code true} if is a simulation, {@code false} otherwise.
   */
  public boolean isSimulation() {
    return simulation;
  }

  /**
   * Sets whether it is a simulation.
   * @param simulation 
   *        {@code true} if is a simulation, {@code false} otherwise.
   */
  public void setSimulation(boolean simulation) {
    this.simulation = simulation;
  }

  /**
   * Gets the minimum time that has to pass between commands sent to the robotic
   * arm.
   * @return The time in milliseconds.
   */
  public int getTimeBetweenCommands() {
    return timeBetweenCommands;
  }

  /**
   * Sets the minimum time that has to pass between commands sent to the robotic
   * @param timeBetweenCommands
   *        The time in milliseconds.
   */
  public void setTimeBetweenCommands(int timeBetweenCommands) {
    this.timeBetweenCommands = timeBetweenCommands;
  }

  /**
   * Adds a new command to be sent to the robotic arm.
   * @param command
   *        The command to be sent.
   * @return The newly generated id for the passed command.
   */
  public String addCommand(String command) {
    long millis = System.currentTimeMillis();
    String id = String.format("%d%d", millis, nextIdCounter++);
    ArmCommand ac = new ArmCommand(command, id);
    queue.add(ac);
//    System.out.println("Sending command: "+ac.getMessage());
    if (!simulation && comm != null && comm.isConnected()) {
      try {
        TimeUnit.MILLISECONDS.sleep(timeBetweenCommands);
      } catch (InterruptedException ex) {
        Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
      }
      comm.writeToPort(ac.getMessage()+LF);
      armController.log(Level.INFO, LOG_COMMAND_SENT, ac.getMessage());
    } else {
      System.out.println(
              String.format(armController.getExistingLabel(
                      SYS_SIMULATING_COMMAND
              ), 
              ac.getMessage())
      );
    }
    return id;
  }

  /**
   * Receives the notification of an error from the {@code SerialCommunicator}.
   * @param error
   *        The error message.
   */
  @Override
  public void notifyError(String error) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Receives the notification of a message from the {@code SerialCommunicator}.
   * @param message
   *        The message.
   */
  @Override
  public void notifyMessage(String message) {
//    System.out.println("notifyMessage() >"+message+"<");
    inFlow.append(message);
    processIncomingFlow();
  }

  /**
   * Processes the data present in {@code inFlow}.
   * 
   * As the data flows in in chunks and the operation could require more than 
   * one call of this method to complete, the seek for acknowledge messages is 
   * done one character at the time
   */
  private void processIncomingFlow() {
    int start = pos;
    char curr;

    for (; pos < inFlow.length(); pos++) {
      curr = inFlow.charAt(pos);
      if (curr == LF) {
        if (ignored.length() > 0) {
          armController.log(
                  Level.INFO, 
                  INFO_IGNORED_DATA_FROM_ARM, 
                  ignored.toString());
          ignored.delete(0, ignored.length());
        }
        ack = 1;
      } else if (ack == 1 && curr == 'A') {
        ack = 2;
      } else if (ack == 2 && curr == 'C') {
        ack = 3;
      } else if (ack == 3 && curr == 'K') {
        ack = 4;
      } else if (ack == 4 && curr == 0x20) {
        ack = 5;
      } else if (ack == 5 && curr == ID_OPENING_CHAR) {
        start = pos;
        if (composingId) {
          ack = 0;
          System.out.println(
                  armController.getExistingLabel(LOG_CORRUPTED_DATA)
          );
        } else {
          composingId = true;
          currentId = new StringBuilder();
        }
      } else if (ack == 5 && curr == ID_CLOSING_CHAR) {
        ack = 0;
        if (composingId) {
          composingId = false;
          removeId(currentId.toString());
//          String prefix = inFlow.substring(0, start);
//          armController.log(Level.SEVERE, prefix);
//          inFlow.replace(0, start+pos+1, "");
//          pos = 0;
//          System.out.println(String.format("command %s", prefix));
//          System.out.println(String.format("id: %s", currentId.toString()));
        } else {
          String wrongData = inFlow.substring(0, pos+1);
//          inFlow.replace(0, pos+1, "");
//          pos = 0;
          armController.log(Level.SEVERE, LOG_WRONG_DATA, wrongData);
        }        
      } else if (composingId) {
        if (curr >= '0' && curr <= '9') {
          currentId.append(curr);
        } else {
          ack = 0;
          armController.log(Level.SEVERE, LOG_CORRUPTED_DATA);
        }
      } else if (curr != CR) {
        ignored.append(curr);
      }
    }
    inFlow.delete(0, pos);
    pos = 0;
  }

 /**
   * Removes the id from the FIFO queue.
   * @param id 
   *        The id of the command to be removed.
   */
  private void removeId(String id) {
    ListIterator<ArmCommand> iter = queue.listIterator();
    ArmCommand item = null;
    String itemId;
    boolean first = true;
    boolean found = false;
    List skipped = new LinkedList<ArmCommand>();
    while(iter.hasNext()) {
      item = iter.next();
      itemId = item.getId();
      if (itemId.compareTo(id) == 0) {
        found = true;
        break;
      }
      first = false;
      skipped.add(item);
    }
    
    if (found) {
      armController.log(Level.INFO, LOG_COMMAND_EXECUTED, item);
      queue.remove(item);
      /*
       * If it wasn't the first in the list, it means that we did not receive a
       * number of acknowledges and hence we suppose that those commands have 
       * not been executed. 
       */
      if (!first) {
        armController.log(
                Level.SEVERE, armController.getExistingLabel(LOG_CORRUPTED_DATA)
        );
        /*
         * Proceed removing from the FIFO queue all skipped items.
         */
        iter = skipped.listIterator();
        while(iter.hasNext()) {
          item = iter.next();
          armController.log(
                  Level.SEVERE,
                  LOG_COMMAND_NOT_EXECUTED, 
                  item
          );
          queue.remove(item);
        }
      } else {
      }
    } else {
      armController.log(Level.INFO, LOG_COMMAND_NOT_FOUND, id);
    }
  }
}
