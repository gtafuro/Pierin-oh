package it.gftblues.sixdofarm.ui;

import com.camick.TextLineNumber;
import it.gftblues.sixdofarm.PierinhoLanguage;
import it.gftblues.sixdofarm.SixDOFArmController;
import it.gftblues.sixdofarm.SixDOFArmControllerInterface;
import it.gftblues.utils.FileUtils;
import java.awt.Component;
import java.awt.KeyEventPostProcessor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

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
 *  Programming tab of the {@code SixDOFArmControllerGUI}.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class SixDOFArmProgramTab extends javax.swing.JPanel 
        implements UndoableEditListener, KeyEventPostProcessor {
  
  final private SixDOFArmControllerGUI gui;
  final private SixDOFArmControllerInterface armController;
  
  private final UndoManager undoManager = new UndoManager();
  private final Action undoAction = new UndoAction(undoManager);
  private final Action REdoAction = new RedoAction(undoManager);
  
  private TextLineNumber tln1;

  /**
   * Creates new form SixDOFArmProgramTab
   * @param gui
   *        A reference to the {@code SixDOFArmControllerGUI} for sending 
   *        notifications (for instance informing the parent that the serial 
   *        port has been connected).
   * @param ac
   *        The reference to the object that implements a 
   *        {@code SixDOFArmControllerInterface} interface.
   */
  public SixDOFArmProgramTab(
          SixDOFArmControllerGUI gui,
          SixDOFArmControllerInterface ac
  ) {
    this.gui = gui;
    armController = ac;
    preInistializing();
    initComponents();
    postInitializing();
  }
  
  private void preInistializing() {
  }

  private void postInitializing() {
    tln1 = new TextLineNumber(program);
    programPanel.setRowHeaderView(tln1);
    program.getDocument().addUndoableEditListener(undoManager);
    program.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
          if (e.getKeyCode() == KeyEvent.VK_Z) {
            if (undoManager.canUndo()) {
              undoManager.undo();
            }
          } else if (e.getKeyCode() == KeyEvent.VK_Y) {
            if (undoManager.canRedo()) {
              undoManager.redo();
            }
          }
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });
    armConnected(armController.isArmConnected());
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    innerPanel = new javax.swing.JPanel();
    programLabel = new javax.swing.JLabel();
    programPanel = new javax.swing.JScrollPane();
    program = new javax.swing.JTextArea();
    loadBtn = new javax.swing.JButton();
    saveBtn = new javax.swing.JButton();
    saveAsBtn = new javax.swing.JButton();
    insertCommandBtn = new javax.swing.JButton();
    programPathLabel = new javax.swing.JLabel();
    verifyBtn = new javax.swing.JButton();
    runBtn = new javax.swing.JButton();
    outputPanel = new javax.swing.JPanel();
    outputLabel = new javax.swing.JLabel();
    outputAreaPanel = new javax.swing.JScrollPane();
    outputTextArea = new javax.swing.JTextArea();
    clearBtn = new javax.swing.JButton();

    innerPanel.setMaximumSize(new java.awt.Dimension(535, 462));
    innerPanel.setMinimumSize(new java.awt.Dimension(535, 462));
    innerPanel.setPreferredSize(new java.awt.Dimension(535, 462));

    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("it/gftblues/sixdofarm/SixDOFArm"); // NOI18N
    programLabel.setText(bundle.getString("TAB_PROGRAM_PROGRAM")); // NOI18N

    program.setColumns(20);
    program.setRows(5);
    programPanel.setViewportView(program);

    loadBtn.setText(bundle.getString("COMMON_LOAD_BTN")); // NOI18N
    loadBtn.setActionCommand(bundle.getString("COMMON_LOAD_BTN")); // NOI18N
    loadBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        loadBtnActionPerformed(evt);
      }
    });

    saveBtn.setText(bundle.getString("COMMON_SAVE_BTN")); // NOI18N
    saveBtn.setActionCommand(bundle.getString("COMMON_SAVE_BTN")); // NOI18N
    saveBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveBtnActionPerformed(evt);
      }
    });

    saveAsBtn.setText(bundle.getString("COMMON_SAVE_AS_BTN")); // NOI18N
    saveAsBtn.setActionCommand(bundle.getString("COMMON_SAVE_AS_BTN")); // NOI18N
    saveAsBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsBtnActionPerformed(evt);
      }
    });

    insertCommandBtn.setText(bundle.getString("TAB_PROGRAM_INSERT_COMMAND_BTN")); // NOI18N
    insertCommandBtn.setActionCommand(bundle.getString("TAB_PROGRAM_INSERT_COMMAND_BTN")); // NOI18N
    insertCommandBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        insertCommandBtnActionPerformed(evt);
      }
    });

    programPathLabel.setText("program path");

    verifyBtn.setText(bundle.getString("TAB_PROGRAM_VERIFY_BTN")); // NOI18N
    verifyBtn.setActionCommand(bundle.getString("TAB_PROGRAM_VERIFY_BTN")); // NOI18N
    verifyBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        verifyBtnActionPerformed(evt);
      }
    });

    runBtn.setText(bundle.getString("TAB_PROGRAM_RUN_BTN")); // NOI18N
    runBtn.setActionCommand(bundle.getString("TAB_PROGRAM_RUN_BTN")); // NOI18N
    runBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runBtnActionPerformed(evt);
      }
    });

    outputLabel.setText(bundle.getString("TAB_PROGRAM_OUTPUT")); // NOI18N

    outputTextArea.setEditable(false);
    outputTextArea.setColumns(20);
    outputTextArea.setRows(5);
    outputAreaPanel.setViewportView(outputTextArea);

    javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
    outputPanel.setLayout(outputPanelLayout);
    outputPanelLayout.setHorizontalGroup(
      outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(outputPanelLayout.createSequentialGroup()
        .addComponent(outputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 144, Short.MAX_VALUE))
      .addComponent(outputAreaPanel)
    );
    outputPanelLayout.setVerticalGroup(
      outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(outputPanelLayout.createSequentialGroup()
        .addComponent(outputLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(outputAreaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
    );

    clearBtn.setText(bundle.getString("TABL_PROGRAM_CLEAR_OUTPUT")); // NOI18N
    clearBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clearBtnActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout innerPanelLayout = new javax.swing.GroupLayout(innerPanel);
    innerPanel.setLayout(innerPanelLayout);
    innerPanelLayout.setHorizontalGroup(
      innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(innerPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(innerPanelLayout.createSequentialGroup()
            .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(programPathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(programLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(programPanel))
            .addGap(18, 18, 18)
            .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(saveAsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(loadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(insertCommandBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(verifyBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(runBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap())
    );
    innerPanelLayout.setVerticalGroup(
      innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(innerPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(innerPanelLayout.createSequentialGroup()
            .addComponent(programLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(programPanel))
          .addGroup(innerPanelLayout.createSequentialGroup()
            .addComponent(loadBtn)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(saveBtn)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(saveAsBtn)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(insertCommandBtn)
            .addGap(18, 18, 18)
            .addComponent(verifyBtn)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(runBtn)
            .addGap(0, 0, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addGroup(innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(programPathLabel)
          .addComponent(clearBtn))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(innerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(innerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
  }// </editor-fold>//GEN-END:initComponents

  private void verifyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyBtnActionPerformed
    String[] lines = program.getText().split("\n");
    int len = lines.length;
    boolean verified = true;
//    PierinhoLanguage lang = new PierinhoLanguage();
    String[] commandInfo;
    if (len == 0 || (len == 1 && lines[0].trim().length() == 0)) {
      notifyProgramMessage(
              Level.INFO,
              "MSG_NOTHING_TO_VERIFY"
      );
    } else {
      int errors = 0;
      notifyProgramMessage(
              Level.INFO,
              "MSG_VERIFYING_CODE"
      );
      for (int i = 0; i < len; i++) {
        if (lines[i].trim().length() > 0) {
          if (!PierinhoLanguage.isComment(lines[i])) {
            commandInfo = PierinhoLanguage.getValidCommand(lines[i]);
            if (commandInfo[PierinhoLanguage.COMMAND_ERROR] != null) {
              verified = false;
              errors++;
              if (commandInfo[PierinhoLanguage.COMMAND_ERROR].compareTo(
                      PierinhoLanguage.ERROR_NO_COMMAND_FOUND) == 0) {
                notifyProgramMessage(
                        Level.INFO,
                        "ERROR_INVALID_COMMAND_IN_LIST",
                        commandInfo[PierinhoLanguage.COMMAND_ACTION],
                        i+1
                );
              } else {
                notifyProgramMessage(
                        Level.INFO,
                        "ERROR_INVALID_VALUE_FOR_COMMAND",
                        commandInfo[PierinhoLanguage.COMMAND_AMOUNT],
                        commandInfo[PierinhoLanguage.COMMAND_ACTION],
                        i+1
                );
              }
//              break;
            }
          }
        }
      }
      if (verified) {
        notifyProgramMessage(
                Level.INFO,
                "MSG_VERIFICATION_COMPLETED_SUCCESSFULLY"
        );
      } else {
        notifyProgramMessage(
                Level.INFO,
                "MSG_VERIFICATION_FOUND_ERRORS",
                errors
        );
      }
    }
  }//GEN-LAST:event_verifyBtnActionPerformed

  private void loadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadBtnActionPerformed
    showLoadProgramDlg();
  }//GEN-LAST:event_loadBtnActionPerformed

  private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
    saveProgram();
  }//GEN-LAST:event_saveBtnActionPerformed

  private void saveAsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsBtnActionPerformed
    String filename = selectFileToSave();
    if (filename != null) {
      programPathLabel.setText(filename);
      saveProgram();
    }
  }//GEN-LAST:event_saveAsBtnActionPerformed

  private void insertCommandBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertCommandBtnActionPerformed
    CommandDlg cmdDlg = new CommandDlg(gui);
    cmdDlg.setVisible(true);
    if (cmdDlg.isDone()) {
      program.insert(cmdDlg.getCommand(), program.getCaretPosition());
    }
  }//GEN-LAST:event_insertCommandBtnActionPerformed

  private void runBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runBtnActionPerformed
    armController.runProgram(program.getText());
  }//GEN-LAST:event_runBtnActionPerformed

  private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
    this.outputTextArea.setText("");
  }//GEN-LAST:event_clearBtnActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton clearBtn;
  private javax.swing.JPanel innerPanel;
  private javax.swing.JButton insertCommandBtn;
  private javax.swing.JButton loadBtn;
  private javax.swing.JScrollPane outputAreaPanel;
  private javax.swing.JLabel outputLabel;
  private javax.swing.JPanel outputPanel;
  private javax.swing.JTextArea outputTextArea;
  private javax.swing.JTextArea program;
  private javax.swing.JLabel programLabel;
  private javax.swing.JScrollPane programPanel;
  private javax.swing.JLabel programPathLabel;
  private javax.swing.JButton runBtn;
  private javax.swing.JButton saveAsBtn;
  private javax.swing.JButton saveBtn;
  private javax.swing.JButton verifyBtn;
  // End of variables declaration//GEN-END:variables

  private void showLoadProgramDlg() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
      SixDOFArmResources.getString("PIERINHO_FILE_DESCRIPTION"),
      SixDOFArmResources.getString("PIERINHO_FILE_EXTENSION")
    );
    File file;
    File dir;
    final JFileChooser fc = new JFileChooser();
    fc.setDialogTitle(SixDOFArmResources.getString("PIERINHO_FILE_SELECTION"));
    fc.setFileFilter(filter);
    boolean proceed;
    String filename = programLabel.getText();
    
    if (filename.length() > 0) {
      dir = new File(FileUtils.expandFileName(filename));
      if (dir.exists()) {
        fc.setCurrentDirectory(dir);
      }
    }
    do {
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = fc.getSelectedFile();
        if (file.exists()) {
          proceed = true;
        } else {
          proceed = false;
          gui.showError(String.format(
            SixDOFArmResources.getString("ERROR_FILE_DOES_NOT_EXIST"),
            file.getAbsolutePath()));
      }
      if (proceed) {
        programPathLabel.setText(file.getAbsolutePath());
        loadProgram();
      } else {
      }
    } else {
      break;
    }
    } while(!proceed);
  }

  private void loadProgram() {
    String filename = programPathLabel.getText();
    if (filename.length() > 0) {
      FileReader fr;
      try {
        fr = new FileReader(filename);
      } catch (FileNotFoundException ex) {
        fr = null;
        Logger.getLogger(SixDOFArmController.class.getName()).log(
                Level.SEVERE, null, ex
        );
      }
      if (fr != null) {
        try (BufferedReader reader = new BufferedReader(fr)) {
          String line;
          program.setText("");
          while((line = reader.readLine()) != null) {
            program.append(line+"\n");
          }
        } catch (IOException ex) {
          Logger.getLogger(SixDOFArmController.class.getName()).log(
                  Level.SEVERE, null, ex
          );
        }
      }
    }
  }

  private void saveProgram() {
    String filename = programPathLabel.getText();
    if (filename.length() > 0) {
      FileWriter fr;
      try {
        fr = new FileWriter(filename);
      } catch (IOException ex) {
        fr = null;
        Logger.getLogger(SixDOFArmController.class.getName()).log(
                Level.SEVERE, null, ex
        );
      }
      if (fr != null) {
        try (BufferedWriter writer = new BufferedWriter(fr)) {
          writer.write(program.getText());
          outputTextArea.append(String.format(
                  SixDOFArmResources.getString("INFO_FILE_HAS_BEEN_SAVED"), 
                  filename
          ));
        } catch (IOException ex) {
          Logger.getLogger(SixDOFArmController.class.getName()).log(
                  Level.SEVERE, null, ex
          );
        }
      }
    }
  }
  
  private String selectFileToSave() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
      SixDOFArmResources.getString("PIERINHO_FILE_DESCRIPTION"),
      SixDOFArmResources.getString("PIERINHO_FILE_EXTENSION")
    );
    File file;
    File dir;
    final JFileChooser fc = new JFileChooser();
    fc.setDialogTitle(SixDOFArmResources.getString("TAB_PROGRAM_SAVE_AS"));
    fc.setFileFilter(filter);
    String defaulPathname = null;
    
    if (defaulPathname != null) {
        dir = new File(FileUtils.expandFileName(defaulPathname));
        String directory = dir.getPath();
        dir = new File(directory);

        if (dir.exists()) {
            fc.setCurrentDirectory(dir);
        }
    }
    boolean proceed;
    do {
      int returnVal = fc.showSaveDialog(gui);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = fc.getSelectedFile();
        String ext = FileUtils.getExtension(file);
        if (ext == null) {
          String[] exts = filter.getExtensions();
          if (exts != null && exts.length > 0) {
            File extd = new File(file.getAbsoluteFile()+"."+exts[0]);
            file = extd;
          }
        }
        if (file.exists()) {
          proceed = true;
          Object[] options = {
            SixDOFArmResources.getString("COMMON_YES_BTN"),
            SixDOFArmResources.getString("COMMON_NO_BTN")
          };
          if ( JOptionPane.showOptionDialog(
                  null,
                  String.format(
                          SixDOFArmResources.getString(
                                  "COMMON_OVERWRITE_FILE_MSG"
                          ),
                          file.getName()
                  ),
                  String.format(
                          SixDOFArmResources.getString(
                                  "COMMON_OVERWRITE_FILE_MSG_TITLE"
                          )
                  ),
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE,
                  null, 
                  options,
                  options[1]) != JOptionPane.YES_OPTION) {
            proceed = false;
          }
        } else {
          proceed = true;
        }
        if (proceed) {
          return file.getAbsolutePath();
        }
      } else {
        break;
      }
    } while(!proceed);

    return null;
  }

  @Override
  public void undoableEditHappened(UndoableEditEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void armConnected(boolean connected) {
    if (connected) {
      runBtn.setEnabled(true);
    } else {
      runBtn.setEnabled(false);
    }
  }

  public String getProgramPathname() {
    return programPathLabel.getText();
  }

  @Override
  public boolean postProcessKeyEvent(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_O:
        Component comp = gui.getFocusOwner();
        System.out.println(comp);
        showLoadProgramDlg();
        return true;    //halt further processing
      case KeyEvent.VK_S:
        saveProgram();
        return true;    //halt further processing
      case KeyEvent.VK_R:
        armController.runProgram(program.getText());
        return true;    //halt further processing
      default:
        break;
    }
    return false;
  }

   /**
     * Log a message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     *
     * @param   level   One of the message level identifiers, e.g., SEVERE
     * @param   message     The string message (or a key in the message catalog)
     */
   public void notifyProgramMessage(Level level, String message) {
    outputTextArea.append(SixDOFArmResources.getString(message));
  }

   /**
     * Log a message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     *
     * @param   level   One of the message level identifiers, e.g., SEVERE
     * @param   message     The string message (or a key in the message catalog)
     * @param arguments object(s) to format
     */
  public void notifyProgramMessage(Level level, String message, Object ... arguments) {
    message = MessageFormat.format(SixDOFArmResources.getString(message), arguments);
    if (level == Level.SEVERE) {
      outputTextArea.append("<html color=\"red\">"+message+"</html>");
    } else if (level == Level.SEVERE) {
      outputTextArea.append("<html color=\"red\">"+message+"</html>");
    } else {
      outputTextArea.append(message);
    }
  }
}
