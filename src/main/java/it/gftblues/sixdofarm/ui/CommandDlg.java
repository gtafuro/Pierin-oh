package it.gftblues.sixdofarm.ui;

import it.gftblues.sixdofarm.PierinhoCommand;
import it.gftblues.sixdofarm.PierinhoLanguage;
import it.gftblues.sixdofarm.joints.Clamp;
import it.gftblues.sixdofarm.joints.Elbow;
import it.gftblues.sixdofarm.joints.Shoulder;
import it.gftblues.sixdofarm.joints.Wrist;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

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
 * User interface for creating Pierin-oh! commands.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class CommandDlg extends javax.swing.JDialog {

  /**
   * When the dialog box is closed, and the user hits the Insert button, 
   * {@code done} is set to {@code true}, {@code false} when the Cancel button
   * is clicked.
   *
   */
  private boolean done = false;

  /**
   * Creates new form CommandDlg
   * 
   * @param parent
   *        The parent {@code Frame}.
   */
  public CommandDlg(SixDOFArmControllerGUI parent) {
    super(parent, true);
    initComponents();
    postInitializing();
  }

  private void postInitializing() {
    setMovementsForShoulder();
    showCommand();
    Document amountDoc = amount.getDocument();
    amountDoc.addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        showCommand();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        showCommand();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        showCommand();
      }
    });
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jointsGroup = new javax.swing.ButtonGroup();
    movementsGroup = new javax.swing.ButtonGroup();
    jointPanel = new javax.swing.JPanel();
    shoulderRadioBtn = new javax.swing.JRadioButton();
    elbowRadioBtn = new javax.swing.JRadioButton();
    wristRadioBtn = new javax.swing.JRadioButton();
    clampRadioBtn = new javax.swing.JRadioButton();
    movementPanel = new javax.swing.JPanel();
    move1Btn = new javax.swing.JRadioButton();
    move2Btn = new javax.swing.JRadioButton();
    move3Btn = new javax.swing.JRadioButton();
    amountPanel = new javax.swing.JPanel();
    amountLabel = new javax.swing.JLabel();
    amount = new javax.swing.JFormattedTextField();
    amountRange = new javax.swing.JLabel();
    commandLabel = new javax.swing.JLabel();
    errorLabel = new javax.swing.JLabel();
    okBtn = new javax.swing.JButton();
    cancelBtn = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("it/gftblues/sixdofarm/SixDOFArm"); // NOI18N
    setTitle(bundle.getString("COMMAND_DLG")); // NOI18N

    jointPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("CommandDlgJoints"))); // NOI18N

    jointsGroup.add(shoulderRadioBtn);
    shoulderRadioBtn.setSelected(true);
    shoulderRadioBtn.setText(bundle.getString("CommandDlgJointsShoulder")); // NOI18N
    shoulderRadioBtn.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        shoulderRadioBtnFocusGained(evt);
      }
    });
    shoulderRadioBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        shoulderRadioBtnActionPerformed(evt);
      }
    });

    jointsGroup.add(elbowRadioBtn);
    elbowRadioBtn.setText(bundle.getString("CommandDlgJointsElbow")); // NOI18N
    elbowRadioBtn.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        elbowRadioBtnFocusGained(evt);
      }
    });
    elbowRadioBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        elbowRadioBtnActionPerformed(evt);
      }
    });

    jointsGroup.add(wristRadioBtn);
    wristRadioBtn.setText(bundle.getString("CommandDlgJointsWrist")); // NOI18N
    wristRadioBtn.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        wristRadioBtnFocusGained(evt);
      }
    });
    wristRadioBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        wristRadioBtnActionPerformed(evt);
      }
    });

    jointsGroup.add(clampRadioBtn);
    clampRadioBtn.setText(bundle.getString("CommandDlgJointsClamp")); // NOI18N
    clampRadioBtn.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        clampRadioBtnFocusGained(evt);
      }
    });
    clampRadioBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clampRadioBtnActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jointPanelLayout = new javax.swing.GroupLayout(jointPanel);
    jointPanel.setLayout(jointPanelLayout);
    jointPanelLayout.setHorizontalGroup(
      jointPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(shoulderRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
      .addComponent(elbowRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(wristRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(clampRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jointPanelLayout.setVerticalGroup(
      jointPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jointPanelLayout.createSequentialGroup()
        .addComponent(shoulderRadioBtn)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(elbowRadioBtn)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(wristRadioBtn)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(clampRadioBtn)
        .addContainerGap())
    );

    movementPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("CommandDlgMovements"))); // NOI18N

    movementsGroup.add(move1Btn);
    move1Btn.setSelected(true);
    move1Btn.setText(bundle.getString("CommandDlgMovementsClose")); // NOI18N
    move1Btn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        move1BtnActionPerformed(evt);
      }
    });

    movementsGroup.add(move2Btn);
    move2Btn.setText(bundle.getString("CommandDlgMovementsHorizontal")); // NOI18N
    move2Btn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        move2BtnActionPerformed(evt);
      }
    });

    movementsGroup.add(move3Btn);
    move3Btn.setText(bundle.getString("CommandDlgMovementsOpen")); // NOI18N
    move3Btn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        move3BtnActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout movementPanelLayout = new javax.swing.GroupLayout(movementPanel);
    movementPanel.setLayout(movementPanelLayout);
    movementPanelLayout.setHorizontalGroup(
      movementPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(move1Btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(move2Btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(move3Btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    movementPanelLayout.setVerticalGroup(
      movementPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(movementPanelLayout.createSequentialGroup()
        .addComponent(move1Btn)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(move2Btn)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(move3Btn)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    amountLabel.setText(bundle.getString("CommandDlgDegrees")); // NOI18N

    amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
    amount.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
    amount.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        amountActionPerformed(evt);
      }
    });

    amountRange.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    amountRange.setText(" ");

    javax.swing.GroupLayout amountPanelLayout = new javax.swing.GroupLayout(amountPanel);
    amountPanel.setLayout(amountPanelLayout);
    amountPanelLayout.setHorizontalGroup(
      amountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(amountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(amount)
      .addComponent(amountRange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    amountPanelLayout.setVerticalGroup(
      amountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(amountPanelLayout.createSequentialGroup()
        .addComponent(amountLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(amountRange))
    );

    commandLabel.setText(" ");

    errorLabel.setForeground(new java.awt.Color(255, 0, 0));
    errorLabel.setText(" ");

    okBtn.setText(bundle.getString("COMMON_INSERT_BTN")); // NOI18N
    okBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okBtnActionPerformed(evt);
      }
    });

    cancelBtn.setText(bundle.getString("COMMON_CANCEL_BTN")); // NOI18N
    cancelBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelBtnActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jointPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(movementPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(amountPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(okBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(0, 0, Short.MAX_VALUE))
          .addComponent(commandLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(okBtn)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cancelBtn))
          .addComponent(jointPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(movementPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(amountPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(commandLabel)
        .addGap(18, 18, 18)
        .addComponent(errorLabel)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void elbowRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elbowRadioBtnActionPerformed
    setMovementsForElbow();
  }//GEN-LAST:event_elbowRadioBtnActionPerformed

  private void wristRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wristRadioBtnActionPerformed
    setMovementsForWrist();
  }//GEN-LAST:event_wristRadioBtnActionPerformed

  private void clampRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clampRadioBtnActionPerformed
    setMovementsForClamp();
  }//GEN-LAST:event_clampRadioBtnActionPerformed

  private void move1BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move1BtnActionPerformed
    showCommand();
  }//GEN-LAST:event_move1BtnActionPerformed

  private void move2BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move2BtnActionPerformed
    showCommand();
  }//GEN-LAST:event_move2BtnActionPerformed

  private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
    done = true;
    setVisible(false);
  }//GEN-LAST:event_okBtnActionPerformed

  private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
    done = false;
    setVisible(false);
  }//GEN-LAST:event_cancelBtnActionPerformed

  private void shoulderRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shoulderRadioBtnActionPerformed
    setMovementsForShoulder();
  }//GEN-LAST:event_shoulderRadioBtnActionPerformed

  private void shoulderRadioBtnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_shoulderRadioBtnFocusGained
    setMovementsForShoulder();
  }//GEN-LAST:event_shoulderRadioBtnFocusGained

  private void elbowRadioBtnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_elbowRadioBtnFocusGained
    setMovementsForElbow();
  }//GEN-LAST:event_elbowRadioBtnFocusGained

  private void wristRadioBtnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_wristRadioBtnFocusGained
    setMovementsForWrist();
  }//GEN-LAST:event_wristRadioBtnFocusGained

  private void clampRadioBtnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clampRadioBtnFocusGained
    setMovementsForClamp();
  }//GEN-LAST:event_clampRadioBtnFocusGained

  private void amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_amountActionPerformed

  private void move3BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_move3BtnActionPerformed
    showCommand();
  }//GEN-LAST:event_move3BtnActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JFormattedTextField amount;
  private javax.swing.JLabel amountLabel;
  private javax.swing.JPanel amountPanel;
  private javax.swing.JLabel amountRange;
  private javax.swing.JButton cancelBtn;
  private javax.swing.JRadioButton clampRadioBtn;
  private javax.swing.JLabel commandLabel;
  private javax.swing.JRadioButton elbowRadioBtn;
  private javax.swing.JLabel errorLabel;
  private javax.swing.JPanel jointPanel;
  private javax.swing.ButtonGroup jointsGroup;
  private javax.swing.JRadioButton move1Btn;
  private javax.swing.JRadioButton move2Btn;
  private javax.swing.JRadioButton move3Btn;
  private javax.swing.JPanel movementPanel;
  private javax.swing.ButtonGroup movementsGroup;
  private javax.swing.JButton okBtn;
  private javax.swing.JRadioButton shoulderRadioBtn;
  private javax.swing.JRadioButton wristRadioBtn;
  // End of variables declaration//GEN-END:variables

  private void setMovementsForClamp() {
    move1Btn.setText(SixDOFArmResources.getString("CommandDlgMovementsOpen"));
    move1Btn.setSelected(true);
    move2Btn.setText(SixDOFArmResources.getString("CommandDlgMovementsClose"));
    move2Btn.setVisible(true);
    move3Btn.setText(SixDOFArmResources.getString("CommandDlgMovementsSetAmount"));
    move3Btn.setVisible(true);
    showCommand();
  }

  private void setMovementsForWrist() {
    move1Btn.setText(
            SixDOFArmResources.getString("CommandDlgMovementsVertical")
    );
    move1Btn.setSelected(true);
    move2Btn.setText(SixDOFArmResources.getString("CommandDlgMovementsRotate"));
    move2Btn.setVisible(true);
    move3Btn.setVisible(false);
    showCommand();
  }
  
  private void setMovementsForElbow() {
    move1Btn.setText(
            SixDOFArmResources.getString("CommandDlgMovementsVertical")
    );
    move1Btn.setSelected(true);
    move2Btn.setVisible(false);
    move3Btn.setVisible(false);
    showCommand();
  }

  private void setMovementsForShoulder() {
    move1Btn.setText(SixDOFArmResources.getString("CommandDlgMovementsVertical"));
    move1Btn.setSelected(true);
    move2Btn.setText(SixDOFArmResources.getString("CommandDlgMovementsHorizontal"));
    move2Btn.setVisible(true);
    move3Btn.setVisible(false);
    showCommand();
  }

  private String getSelectedAction() {
    if (shoulderRadioBtn.isSelected()) {
      //  COMMAND_DLG_MOVEMENTS_VERTICAL
      if (move1Btn.isSelected()) {
        return Shoulder.ACTION_SV;
      } else if (move2Btn.isSelected()) {
        return Shoulder.ACTION_SH;
      }
    } else if(elbowRadioBtn.isSelected()) {
      return Elbow.ACTION_EV;
    } else if (wristRadioBtn.isSelected()) {
      if (move1Btn.isSelected()) {
        return Wrist.ACTION_WV;
      } else if (move2Btn.isSelected()) {
        return Wrist.ACTION_WR;
      }
    } else if (clampRadioBtn.isSelected()) {
      if (move1Btn.isSelected()) {
        return Clamp.ACTION_CO;
      } else if (move2Btn.isSelected()) {
        return Clamp.ACTION_CC;
      } else {
        return Clamp.ACTION_CS;
      }
    }
    return null;
  }
  
  private void showCommand() {
    String action = getSelectedAction();
    String[] cmd = PierinhoLanguage.getValidCommand(action+amount.getText());
    PierinhoCommand pc = PierinhoLanguage.getCommand(
            cmd[PierinhoLanguage.COMMAND_ACTION]
    );
    if (pc != null && pc.hasParameter()) {
      amountRange.setText(String.format("[%d, %d]", pc.getMin(), pc.getMax()));
    } else {
      amountRange.setText(" ");
    }
    if (cmd[PierinhoLanguage.COMMAND_ERROR] == null) {
/*      if ((cmd[PierinhoLanguage.COMMAND_ACTION].compareTo(PierinhoLanguage.ACTION_CLAMP_SET)) == 0) {
        System.out.println("Bingo!");
      }*/
      okBtn.setEnabled(true);
      commandLabel.setText(
              cmd[PierinhoLanguage.COMMAND_ACTION]+
              cmd[PierinhoLanguage.COMMAND_AMOUNT]
      );
      errorLabel.setText(" ");
    } else {
      okBtn.setEnabled(false);
      commandLabel.setText(" ");
      errorLabel.setText(
              String.format(
                      SixDOFArmResources.getString(
                              cmd[PierinhoLanguage.COMMAND_ERROR]
                      ),
                      Integer.parseInt(cmd[PierinhoLanguage.COMMAND_AMOUNT]),
                      cmd[PierinhoLanguage.COMMAND_ACTION]
              )
      );
    }
  }

  public String getCommand() {
    return commandLabel.getText();
  }

  public boolean isDone() {
    return done;
  }
}
