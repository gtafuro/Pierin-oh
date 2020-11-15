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
 * JUnit Test for {@code PierinhoCommand}
 * @author Gabriele Tafuro
 */
public class PierinhoCommandTest {

  static final String WITH_PARAMETERS = "with parameters";
  static PierinhoCommand command1;
  static PierinhoCommand command2;
  
  public PierinhoCommandTest() {
    command1 = new PierinhoCommand(WITH_PARAMETERS, 0, 100);
    command2 = new PierinhoCommand("with no parameters");
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
   * Test of hasParameter method, of class PierinhoCommand.
   */
  @Test
  public void testHasParameter() {
    System.out.println("hasParameter");
    boolean expResult = true;
    boolean result = command1.hasParameter();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of hasParameter method, of class PierinhoCommand.
   */
  @Test
  public void testHasParameter2() {
    System.out.println("hasParameter2");
    boolean expResult = false;
    boolean result = command2.hasParameter();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getCommand method, of class PierinhoCommand.
   */
  @Test
  public void testGetCommand() {
    System.out.println("getCommand");
    String expResult = WITH_PARAMETERS;
    String result = command1.getCommand();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getMin method, of class PierinhoCommand.
   */
  @Test
  public void testGetMin() {
    System.out.println("getMin");
    int expResult = 0;
    int result = command1.getMin();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getMax method, of class PierinhoCommand.
   */
  @Test
  public void testGetMax() {
    System.out.println("getMax");
    int expResult = 100;
    int result = command1.getMax();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of equals method, of class PierinhoCommand.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Object obj = command2;
    boolean expResult = false;
    boolean result = command1.equals(obj);
    
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of compareTo method, of class PierinhoCommand.
   */
  @Test
  public void testCompareTo() {
    System.out.println("compareTo");
    PierinhoCommand o = command1;
    PierinhoCommand instance = command2;
    int expResult = 0;
    int result = instance.compareTo(o);
    assertNotEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of compareTo method, of class PierinhoCommand.
   */
  @Test
  public void testCompareTo2() {
    System.out.println("compareTo2");
    PierinhoCommand o = command1;
    PierinhoCommand instance = command1;
    int expResult = 0;
    int result = instance.compareTo(o);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }  
}
