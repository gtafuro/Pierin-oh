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

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serial communicator for governing and receiving data from the robotic arm.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class SerialCommunicator implements SerialPortDataListener {

  /**
   * Ports speeds.
   */
  public static final int[] PORT_SPEEDS = {
    75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200
  };

  /**
   * Number of data bits.
   */
  public static final int[] DATA_BITS = {
    5, 6, 7, 8
  };

  /**
   * Type of parity.
   */
  public static enum Parity {
    Even, Mark, None, Odd, Space
  };
  
  /**
   * Stop bit.
   */
  public static final int[] STOP_BIT = {
    1, 2
  };
  
  /**
   * Available serial ports.
   */
  private Map<String, SerialPort> serialPorts;

  /**
   * Used serial port.
   */
  private SerialPort serialPort = null;
  
  /**
   * Used serial port name.
   */
  private String usedPortName = null;
  
  /**
   * Default timeout in milliseconds.
   */
  private int timeOut = 2000;
  
  /**
   * Default data-rate.
   */
  private int dataRate = 9600;
  
  /**
   * Default parity.
   */
  private Parity parity = Parity.None;

  /**
   * Default number of data bits.
   */
  private int dataBit = 8;

  /**
   * Default stop bit.
   */
  private int stopBit = SerialPort.ONE_STOP_BIT;
  
  /**
   * If {@code true} the serial port is connected, {@code false} otherwise.
   */
  private boolean connected = false;
  
  /**
   * Serial communicator lister list.
   */
  private final Set<SerialCommunicatorListener> listeners = new HashSet<>();

  /**
   * Constructor.
   */
  public SerialCommunicator() {
    getPortList();
  }
  
  /**
   * Adds a {@code SerialCommunicatorListener}.
   * @param listener The {@code SerialCommunicatorListener}.
   */
  public void addListener(SerialCommunicatorListener listener) {
    listeners.add(listener);
  }
  
  /**
   * Removes a {@code SerialCommunicatorListener}.
   * @param listener The {@code SerialCommunicatorListener}.
   */
  public void removeListener(SerialCommunicatorListener listener) {
    listeners.remove(listener);
  }
  
  /**
   * Gets the ports' list.
   * @return 
   */
  public final List<String> getPortList() {
    SerialPort[] sports = SerialPort.getCommPorts();
    if (sports != null) {
      serialPorts = new HashMap<>(sports.length);
      for (SerialPort sport : sports) {
        serialPorts.put(sport.getSystemPortName(), sport);
      }
      if (usedPortName != null) {
        if (!serialPorts.isEmpty() && !serialPorts.containsKey(usedPortName)) {
          usedPortName = getFirstSerial();
        }
      } else {
        usedPortName = getFirstSerial();
      }
    } else {
      serialPorts = new HashMap<>();
    }
    return new ArrayList<>(serialPorts.keySet());
  }

  /**
   * Gets the first port available in the ports' list.
   * @return A {@code String} with the port's name.
   */
  private String getFirstSerial() {
    if (!serialPorts.isEmpty()) {
      Map.Entry<String,SerialPort> entry = 
              serialPorts.entrySet().iterator().next();
      return entry.getKey();
    }
    return null;
  }
  
  /**
   * Writes {@code command} to the connected port.
   * @param command The command to write.
   */
  public void writeToPort(String command) {
    if (connected) {
      byte[] payload = command.getBytes();
      serialPort.writeBytes(payload, payload.length);
    } else {
      if (!listeners.isEmpty()) {
        Iterator<SerialCommunicatorListener> i = listeners.iterator();
        while (i.hasNext()) {
          i.next().notifyError("ERROR_NOT_YET_CONNECTED");
        }
      }
    }
  }
  
  /**
   * Writes {@code command} to the connected port.
   * @param command The command to write.
   */
  public void writeToPort(int command) {
    if (connected) {
      byte[] payload = (""+(char)command).getBytes();
      serialPort.writeBytes(payload, payload.length);
    } else {
      if (!listeners.isEmpty()) {
        Iterator<SerialCommunicatorListener> i = listeners.iterator();
        while (i.hasNext()) {
          i.next().notifyError("ERROR_NOT_YET_CONNECTED");
        }
      }
    }
  }

  /**
   * Connects to the selected serial port.
   * @throws Exception 
   */
  public void connect() throws Exception {
    close();
    serialPort = serialPorts.get(usedPortName);
    serialPort.openPort();
    int db, sb, p;
    switch (dataBit) {
      case 5:
        db = 5;
        break;
      case 6:
        db = 6;
        break;
      case 7:
        db = 7;
        break;
      case 8:
      default:
        db = 8; 
    }
    switch (stopBit) {
      case 1:
        sb = SerialPort.ONE_STOP_BIT;
        break;
      case 3:
        sb = SerialPort.ONE_POINT_FIVE_STOP_BITS;
        break;
      case 2:
      default:
        sb = SerialPort.TWO_STOP_BITS;
        break;
    }

    switch (parity) {
      case Even:
        p = SerialPort.EVEN_PARITY;
        break;
      case Mark:
        p = SerialPort.MARK_PARITY;
        break;
      case Odd:
        p = SerialPort.ODD_PARITY;
        break;
      case Space:
        p = SerialPort.SPACE_PARITY;
        break;
      case None:
      default:
        p = SerialPort.NO_PARITY;
    }

    serialPort.setComPortParameters(dataRate, db, sb, p);
    serialPort.addDataListener(this);

    connected = true;
  }

  /**
   * Closes the active connection.
   */
  public synchronized void close() {
    if (serialPort != null) {
      serialPort.removeDataListener();
      serialPort.closePort();
      serialPort = null;
    }
    connected = false;
  }

  /**
   * Gets the name of the used serial port.
   * @return The name of the used serial port.
   */
  public String getComPortName() {
    return usedPortName;
  }

  /**
   * Sets the name of the used serial port.
   * @param comPortName The name of the used serial port.
   */
  public void setComPortName(String comPortName) {
    this.usedPortName = comPortName;
  }

  /**
   * Gets the timeout (in milliseconds).
   * @return The timeout.
   */
  public int getTimeOut() {
    return timeOut;
  }

  /**
   * Sets the timeout (in milliseconds).
   * @param timeOut the timeout.
   */
  public void setTimeOut(int timeOut) {
    this.timeOut = timeOut;
  }

  /**
   * Gets the data-rate.
   * @return The data-rate.
   */
  public int getDataRate() {
    return dataRate;
  }

  /**
   * Sets the data-rate.
   * @param dataRate The data-rate.
   */
  public void setDataRate(int dataRate) {
    this.dataRate = dataRate;
  }

  /**
   * Gets the parity.
   * @return The parity.
   */
  public Parity getParity() {
    return parity;
  }

  /**
   * Sets the parity.
   * @param parity The parity.
   */
  public void setParity(Parity parity) {
    this.parity = parity;
  }

  /**
   * Gets the number of data bits.
   * @return The number of data bits.
   */
  public int getDataBit() {
    return dataBit;
  }

  /**
   * Sets the number of data bits.
   * @param dataBit The number of data bits.
   */
  public void setDataBit(int dataBit) {
    this.dataBit = dataBit;
  }

  /**
   * Gets the stop bit.
   * @return The stop bit.
   */
  public int getStopBit() {
    return stopBit;
  }

  /**
   * Sets the stop bit.
   * @param stopBit The stop bit.
   */
  public void setStopBit(int stopBit) {
    this.stopBit = stopBit;
  }

  /**
   * Checks whether the serial port is connected.
   * @return {@code true} if connected, {@code false} otherwise.
   */
  public boolean isConnected() {
    return connected;
  }

  /**
   * Gets the number of bytes of data available.
   * @return The number of bytes of data available.
   */
  @Override
  public int getListeningEvents() {
    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
  }

  /**
   * Serial event.
   * @param spe The {@code SerialPortEvent} event.
   */
  @Override
  public void serialEvent(SerialPortEvent spe) {
    if (spe.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
      return;
    int dataSize = serialPort.bytesAvailable();
    byte[] payload = new byte[dataSize];
    serialPort.readBytes(payload, dataSize);
    String data = new String(payload);
//    System.out.println("Read " + numRead + " bytes.");
    if(!listeners.isEmpty()) {
      Iterator<SerialCommunicatorListener> iter = listeners.iterator();
      while(iter.hasNext()) {
        iter.next().notifyMessage(data);
      }
    }
  }
}
