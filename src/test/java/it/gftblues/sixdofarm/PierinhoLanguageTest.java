/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gftblues.sixdofarm;

import it.gftblues.sixdofarm.joints.Clamp;
import it.gftblues.sixdofarm.joints.Elbow;
import it.gftblues.sixdofarm.joints.Shoulder;
import it.gftblues.sixdofarm.joints.Wrist;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit Test for {@code PierinhoLanguage}
 * @author Gabriele Tafuro
 */
public class PierinhoLanguageTest {

  private static final String ACTION_1 = "SV";
  private static final String AMOUNT_1 = "23";
  private static final String COMMAND_1 = ACTION_1+AMOUNT_1;
  
  public PierinhoLanguageTest() {
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
   * Test of run method, of class PierinhoLanguage.
   */
  @Test
  public void testRun() {
    System.out.println("run");
    String filename = "";
    PierinhoLanguage instance = new PierinhoLanguage();
    instance.run(filename);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of executeCommand method, of class PierinhoLanguage.
   */
  @Test
  public void testExecuteCommand() {
    System.out.println("executeCommand");
    String command = COMMAND_1;
    int line = 0;
    PierinhoLanguage instance = new PierinhoLanguage();
    boolean expResult = false;
    boolean result = instance.executeCommand(command, line);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of getValidCommand method, of class PierinhoLanguage.
   */
  @Test
  public void testGetValidCommand() {
    System.out.println("getValidCommand");
    String command = COMMAND_1;
    String[] expResult = {ACTION_1, AMOUNT_1, null};
    String[] result = PierinhoLanguage.getValidCommand(command);
    assertArrayEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of getCommandList method, of class PierinhoLanguage.
   */
  @Test
  public void testGetCommandList() {
    System.out.println("getCommandList");
    Map<String, PierinhoCommand> commands = new HashMap<>();
    addCommands(commands, new Shoulder().getCommands());
    addCommands(commands, new Elbow().getCommands());
    addCommands(commands, new Wrist().getCommands());
    addCommands(commands, new Clamp().getCommands());

    boolean complete = true;
        
    String[] result = PierinhoLanguage.getCommandList();
    
    for (int i = 0; i < result.length; i++) {
      if (!commands.containsKey(result[i])) {
        complete = false;
        break;
      }
    }
    
    List<String> keys = Arrays.asList(result);
    if (complete) {
      Set<String> cmdKeys = commands.keySet();
      Iterator iter = cmdKeys.iterator();
      String elem;
      while(iter.hasNext()) {
        elem = (String)iter.next();
        if (!keys.contains(elem)) {
          complete = false;
          break;
        }
      }
    }

    boolean expResult = true;
    assertEquals(expResult, complete);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }
  
  void addCommands(
          Map<String, PierinhoCommand> to, 
          Map<String, PierinhoCommand> from
  ) {
    from.forEach((key, value) -> {
      to.put(key, value);
    });
  }

  /**
   * Test of getCommand method, of class PierinhoLanguage.
   */
  @Test
  public void testGetCommand() {
    System.out.println("getCommand");
    String action = ACTION_1;
    PierinhoCommand expResult = new PierinhoCommand(ACTION_1, -90, 90);
    PierinhoCommand result = PierinhoLanguage.getCommand(action);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isComment method, of class PierinhoLanguage.
   */
  @Test
  public void testIsComment() {
    System.out.println("isComment");
    String command = "#";
    boolean expResult = true;
    boolean result = PierinhoLanguage.isComment(command);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }
}
