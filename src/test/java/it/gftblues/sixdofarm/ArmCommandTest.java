/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gftblues.sixdofarm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit Test for {@code ArmCommand}
 * @author Gabriele Tafuro
 */
public class ArmCommandTest {
  
  public ArmCommandTest() {
  }
  
  @BeforeAll
  public static void setUpClass() {
  }
  
  @AfterAll
  public static void tearDownClass() {
  }
  
  @BeforeEach
  public void setUp() {
  }
  
  @AfterEach
  public void tearDown() {
  }

  /**
   * Test of equals method, of class ArmCommand.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Object obj = new ArmCommand("command1", "id1");
    ArmCommand instance = new ArmCommand("command2", "id2");
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of equals method, of class ArmCommand.
   */
  @Test
  public void testEquals1() {
    System.out.println("equals1");
    Object obj = new ArmCommand("command1", "id1");
    ArmCommand instance = new ArmCommand("command1", "id1");
    boolean expResult = true;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getCommand method, of class ArmCommand.
   */
  @Test
  public void testGetCommand() {
    System.out.println("getCommand");
    String command = "command";
    String id = "id";
    ArmCommand instance = new ArmCommand(command, id);
    String expResult = command;
    String result = instance.getCommand();
    assertEquals(expResult, result);
  }

  /**
   * Test of getId method, of class ArmCommand.
   */
  @Test
  public void testGetId() {
    System.out.println("getId");
    String command = "command";
    String id = "id";
    ArmCommand instance = new ArmCommand(command, id);
    String expResult = id;
    String result = instance.getId();
    assertEquals(expResult, result);
  }

  /**
   * Test of compareTo method, of class ArmCommand.
   */
  @Test
  public void testCompareTo() {
    System.out.println("compareTo");
    String command = "command";
    String id = "id";
    ArmCommand instance = new ArmCommand(command, id);
    ArmCommand o = new ArmCommand("command1", "id1");
    int expResult = 0;
    int result = instance.compareTo(o);
    assertNotEquals(expResult, result);
  }

  /**
   * Test of compareTo method, of class ArmCommand.
   */
  @Test
  public void testCompareTo1() {
    System.out.println("compareTo1");
    String command = "command";
    String id = "id";
    ArmCommand instance = new ArmCommand(command, id);
    ArmCommand o = new ArmCommand("command1", id);
    int expResult = 0;
    int result = instance.compareTo(o);
    assertEquals(expResult, result);
  }

  /**
   * Test of getMessage method, of class ArmCommand.
   */
  @Test
  public void testGetMessage() {
    System.out.println("getMessage");
    String command = "command";
    String id = "id";
    ArmCommand instance = new ArmCommand(command, id);
    String expResult = String.format("%s [%s]", command, id);
    String result = instance.getMessage();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class ArmCommand.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    String command = "command";
    String id = "id";
    ArmCommand instance = new ArmCommand(command, id);
    String expResult =String.format("Command:%s id:%s", command, id);
    String result = instance.toString();
    assertEquals(expResult, result);
  }
  
}
