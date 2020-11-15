package it.gftblues.sixdofarm.ui;

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

import it.gftblues.sixdofarm.Dimension;
import it.gftblues.sixdofarm.controllers.SerialCommunicator;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
import it.gftblues.sixdofarm.SixDOFArmControllerInterface;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Level;
import javax.swing.event.ChangeEvent;

/**
 * Main graphical user interface di Pierin-oh!.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class SixDOFArmControllerGUI
        extends javax.swing.JFrame
        implements SixDOFArmControllerUI,
                   WindowListener {

  private static final String GFTBLUES_WEBSITE = "https://www.gftblues.it/";
  private static final String GFTBLUES_VISIT = "Visit www.gftblues.it";

  final private SixDOFArmControllerInterface armController;
  
  private SixDOFArmControlTab controlTab;
  private SixDOFArmSerialTab serialTab;
  private SixDOFArmGameControllerTab gameControllerTab;
  private SixDOFArmFaceRecognitionTab videoTab;
  private SixDOFArmMathsTab mathsTab;
  private SixDOFArmProgramTab programTab;
  
  /**
   * Creates new form ROT6UGUI
   * @param pc
   *        The reference to the object that implements a 
   *        {@code SixDOFArmControllerInterface} interface.
   */
  public SixDOFArmControllerGUI(SixDOFArmControllerInterface pc) {
    armController = pc;
    URL iconLocation = SixDOFArmControllerGUI.class.getResource(
            "/it/gftblues/sixdofarm/pointer.png"
    );
    if (iconLocation != null) {
      setIconImage(new ImageIcon(iconLocation).getImage());
    }
    preInitializing();
    initComponents();
    postInitializing();
  }

  private void preInitializing() {
    controlTab = new SixDOFArmControlTab(armController);
    serialTab = new SixDOFArmSerialTab(this, armController);
    gameControllerTab = new SixDOFArmGameControllerTab(armController);
    videoTab = new SixDOFArmFaceRecognitionTab(this, armController);
    mathsTab = new SixDOFArmMathsTab(armController);
    programTab = new SixDOFArmProgramTab(this, armController);

    armController.setUI(this);
    serialTab.activate();
  }
  
  private void postInitializing() {
    controlTab.setLayout(new GridLayout(1, 1));
    serialTab.setLayout(new GridLayout(1, 1));
    gameControllerTab.setLayout(new GridLayout(1, 1));
    videoTab.setLayout(new GridLayout(1, 1));
    mathsTab.setLayout(new GridLayout(1, 1));
    programTab.setLayout(new GridLayout(1, 1));
//    controlTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    
    controlTab.setSliders();
/*    clampHeight.setDocument(new FloatDocument());
    elbowHeight.setDocument(new FloatDocument());
    shoulderHeight.setDocument(new FloatDocument());
    baseHeight.setDocument(new FloatDocument());*/
    armController.notifyUIReady();
    DefaultCaret caret;
    caret = (DefaultCaret)feedback.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    enablePad();

/*    String portN = armController.getComPortName();
    if (portN != null) {
      comPortName.setSelectedItem(portN);
    }
    datarateModel.setSelectedItem(""+armController.getSerialDataRate());*/
/*    dataBitsModel.setSelectedItem(""+armController.getSerialDataBit());
    parityComboModel.setSelectedItem(""+armController.getSerialParity());
    stopBitModel.setSelectedItem(""+armController.getSerialStopBit());
    comTimeout.setText(""+armController.getSerialTimeout());*/
    addWindowListener(this);
    setAboutTab();
    tabbedPane.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (tabbedPane.getSelectedComponent() == programTab) {
          KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
          manager.addKeyEventPostProcessor(programTab);
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.removeKeyEventPostProcessor(programTab);
      }
    });
    tabbedPane.addChangeListener((ChangeEvent e) -> {
      if (tabbedPane.getSelectedComponent() == programTab) {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(programTab);
      } else {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.removeKeyEventPostProcessor(programTab);
      }
    });
  }

  private void setAboutTab() {
    aboutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    Component parent = this;
    aboutLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (
                JOptionPane.showConfirmDialog(
                        parent,
                        "Do you want to visit "+GFTBLUES_WEBSITE+")",
                        "About to open a web page",
                        JOptionPane.YES_NO_OPTION
                ) == 0
        ) {
          try {
            Desktop.getDesktop().browse(new URI(GFTBLUES_WEBSITE));
          } catch (URISyntaxException | IOException ex) {
            //It looks like there's a problem
          }
        }
      }
    });
  }

  public void enablePad() {
    programTab.armConnected(armController.isArmConnected());
    if (armController.isArmConnected()) {
      enablePad(true);
    } else {
      enablePad(false);
    }
  }
  
  public void enablePad(boolean enable) {
    commandLabel.setEnabled(enable);
    command.setEnabled(enable);
    feedback.setEnabled(enable);
    feedbackLabel.setEnabled(enable);
    feedback.setEnabled(enable);
    if (enable) {
/*      target.addMouseListener(new MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
          targetMouseClicked(evt);
        }
      });*/
    } else {
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    mainPanel = new javax.swing.JPanel();
    tabbedPane = new javax.swing.JTabbedPane();
    javax.swing.JPanel controlTab = this.controlTab;
    javax.swing.JPanel serialTab = this.serialTab;
    javax.swing.JPanel gameControllerPanel = this.gameControllerTab;
    javax.swing.JPanel videoTab = this.videoTab;
    javax.swing.JPanel mathsTab = this.mathsTab;
    javax.swing.JPanel programTab = this.programTab;
    aboutTab = new javax.swing.JPanel();
    aboutLabel = new javax.swing.JLabel();
    feedbackPane = new javax.swing.JPanel();
    commandLabel = new javax.swing.JLabel();
    command = new javax.swing.JTextField();
    feedbackLabel = new javax.swing.JLabel();
    feedbackScroll = new javax.swing.JScrollPane();
    feedback = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("6 DOF Arm Controller");
    setMinimumSize(new java.awt.Dimension(730, 555));
    setResizable(false);
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    mainPanel.setMaximumSize(new java.awt.Dimension(715, 548));
    mainPanel.setMinimumSize(new java.awt.Dimension(715, 548));
    mainPanel.setPreferredSize(new java.awt.Dimension(715, 548));
    mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    tabbedPane.setToolTipText("");
    tabbedPane.setMaximumSize(new java.awt.Dimension(540, 508));
    tabbedPane.setMinimumSize(new java.awt.Dimension(540, 508));
    tabbedPane.setPreferredSize(new java.awt.Dimension(540, 508));
    tabbedPane.setRequestFocusEnabled(false);

    controlTab.setToolTipText("Controls");
    controlTab.setMaximumSize(new java.awt.Dimension(525, 460));
    controlTab.setMinimumSize(new java.awt.Dimension(525, 460));
    controlTab.setPreferredSize(new java.awt.Dimension(525, 460));

    javax.swing.GroupLayout controlTabLayout = new javax.swing.GroupLayout(controlTab);
    controlTab.setLayout(controlTabLayout);
    controlTabLayout.setHorizontalGroup(
      controlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 535, Short.MAX_VALUE)
    );
    controlTabLayout.setVerticalGroup(
      controlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/target.png")), controlTab, "Controls"); // NOI18N

    serialTab.setToolTipText("Serial Ports");
    serialTab.setPreferredSize(new java.awt.Dimension(525, 460));

    javax.swing.GroupLayout serialTabLayout = new javax.swing.GroupLayout(serialTab);
    serialTab.setLayout(serialTabLayout);
    serialTabLayout.setHorizontalGroup(
      serialTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 535, Short.MAX_VALUE)
    );
    serialTabLayout.setVerticalGroup(
      serialTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/serial.png")), serialTab, "Serial Ports"); // NOI18N

    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("it/gftblues/sixdofarm/SixDOFArm"); // NOI18N
    gameControllerPanel.setToolTipText(bundle.getString("TAB_GAME_CONTROLLERS")); // NOI18N

    javax.swing.GroupLayout gameControllerPanelLayout = new javax.swing.GroupLayout(gameControllerPanel);
    gameControllerPanel.setLayout(gameControllerPanelLayout);
    gameControllerPanelLayout.setHorizontalGroup(
      gameControllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 535, Short.MAX_VALUE)
    );
    gameControllerPanelLayout.setVerticalGroup(
      gameControllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/game-controller.png")), gameControllerPanel, "Game Controllers"); // NOI18N

    videoTab.setToolTipText(bundle.getString("TAB_VIDEO")); // NOI18N

    javax.swing.GroupLayout videoTabLayout = new javax.swing.GroupLayout(videoTab);
    videoTab.setLayout(videoTabLayout);
    videoTabLayout.setHorizontalGroup(
      videoTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 535, Short.MAX_VALUE)
    );
    videoTabLayout.setVerticalGroup(
      videoTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/video.png")), videoTab, bundle.getString("TAB_VIDEO")); // NOI18N

    mathsTab.setToolTipText(bundle.getString("TAB_MATHS")); // NOI18N

    javax.swing.GroupLayout mathsTabLayout = new javax.swing.GroupLayout(mathsTab);
    mathsTab.setLayout(mathsTabLayout);
    mathsTabLayout.setHorizontalGroup(
      mathsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 535, Short.MAX_VALUE)
    );
    mathsTabLayout.setVerticalGroup(
      mathsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/maths.png")), mathsTab, "Maths"); // NOI18N

    programTab.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        programTabFocusGained(evt);
      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        programTabFocusLost(evt);
      }
    });

    javax.swing.GroupLayout programTabLayout = new javax.swing.GroupLayout(programTab);
    programTab.setLayout(programTabLayout);
    programTabLayout.setHorizontalGroup(
      programTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 535, Short.MAX_VALUE)
    );
    programTabLayout.setVerticalGroup(
      programTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/program.png")), programTab, bundle.getString("TAB_PROGRAM")); // NOI18N

    aboutTab.setToolTipText("");
    aboutTab.setMaximumSize(null);
    aboutTab.setPreferredSize(new java.awt.Dimension(500, 400));

    aboutLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    aboutLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/about-photo.jpg"))); // NOI18N
    aboutLabel.setText("<html><span style=\"font-family:sans-serif;font-size:16pt;font-weight:bold;\">Pierinho! v 1.0</span> - GFT Blues</html>");
    aboutLabel.setToolTipText(GFTBLUES_VISIT);
    aboutLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    aboutLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    aboutLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

    javax.swing.GroupLayout aboutTabLayout = new javax.swing.GroupLayout(aboutTab);
    aboutTab.setLayout(aboutTabLayout);
    aboutTabLayout.setHorizontalGroup(
      aboutTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(aboutLabel)
    );
    aboutTabLayout.setVerticalGroup(
      aboutTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(aboutLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
    );

    tabbedPane.addTab("", new javax.swing.ImageIcon(getClass().getResource("/it/gftblues/sixdofarm/about.png")), aboutTab, "About"); // NOI18N

    mainPanel.add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));

    feedbackPane.setMaximumSize(new java.awt.Dimension(165, 508));
    feedbackPane.setMinimumSize(new java.awt.Dimension(165, 508));
    feedbackPane.setPreferredSize(new java.awt.Dimension(165, 508));
    feedbackPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    commandLabel.setText("Command:");
    commandLabel.setMaximumSize(new java.awt.Dimension(160, 14));
    commandLabel.setMinimumSize(new java.awt.Dimension(160, 14));
    commandLabel.setPreferredSize(new java.awt.Dimension(160, 14));
    feedbackPane.add(commandLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    command.setMaximumSize(new java.awt.Dimension(160, 20));
    command.setMinimumSize(new java.awt.Dimension(160, 20));
    command.setPreferredSize(new java.awt.Dimension(160, 20));
    command.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        commandActionPerformed(evt);
      }
    });
    feedbackPane.add(command, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, -1, -1));

    feedbackLabel.setText("Feedback:");
    feedbackLabel.setMaximumSize(new java.awt.Dimension(160, 14));
    feedbackLabel.setMinimumSize(new java.awt.Dimension(160, 14));
    feedbackLabel.setPreferredSize(new java.awt.Dimension(160, 14));
    feedbackPane.add(feedbackLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 58, -1, -1));

    feedbackScroll.setMaximumSize(new java.awt.Dimension(160, 430));
    feedbackScroll.setMinimumSize(new java.awt.Dimension(160, 430));
    feedbackScroll.setPreferredSize(new java.awt.Dimension(160, 430));

    feedback.setColumns(17);
    feedback.setRows(24);
    feedback.setTabSize(2);
    feedback.setMaximumSize(new java.awt.Dimension(156, 436));
    feedback.setMinimumSize(new java.awt.Dimension(156, 436));
    feedbackScroll.setViewportView(feedback);

    feedbackPane.add(feedbackScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 78, -1, -1));

    mainPanel.add(feedbackPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 5, -1, -1));

    getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void commandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commandActionPerformed
//    String action = evt.getActionCommand();
    armController.sendCommand(command.getText());
  }//GEN-LAST:event_commandActionPerformed

  private void programTabFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_programTabFocusGained
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventPostProcessor(programTab);
  }//GEN-LAST:event_programTabFocusGained

  private void programTabFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_programTabFocusLost
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.removeKeyEventPostProcessor(programTab);
  }//GEN-LAST:event_programTabFocusLost

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel aboutLabel;
  private javax.swing.JPanel aboutTab;
  private javax.swing.JTextField command;
  private javax.swing.JLabel commandLabel;
  private javax.swing.JTextArea feedback;
  private javax.swing.JLabel feedbackLabel;
  private javax.swing.JPanel feedbackPane;
  private javax.swing.JScrollPane feedbackScroll;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JTabbedPane tabbedPane;
  // End of variables declaration//GEN-END:variables

  @Override
  public Dimension getTargetDimension() {
    return new Dimension(0, 0);
  }

  @Override
  public void sendFeedback(String message) {
    feedback.append(message);
  }

  @Override
  public void showError(String error) {
    JOptionPane.showMessageDialog(this,
              SixDOFArmResources.getString(error),
              "Error",
              JOptionPane.ERROR_MESSAGE
    );
  }
  
  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this,
              SixDOFArmResources.getString(message),
              "Information",
              JOptionPane.INFORMATION_MESSAGE
    );
  }
  
  @Override
  public void showWarningMessage(String xostrMessage) {
    JOptionPane.showMessageDialog( 
            null,
            SixDOFArmResources.getString(xostrMessage),
            SixDOFArmResources.getString("WARNING_CAPTION"),
            JOptionPane.WARNING_MESSAGE
    );
  }

  @Override
  public String getSerialPortName() {
    return serialTab.getSerialPortName();
  }

  @Override
  public void setSerialPortName(String val) {
    serialTab.setSerialPortName(val);
  }

  @Override
  public int getSerialDataRate() {
    return serialTab.getSerialDataRate();
  }

  @Override
  public void setSerialDataRate(int val) {
    serialTab.setSerialDataRate(val);
  }

  @Override
  public int getSerialDataBit() {
    return serialTab.getSerialDataBit();
  }

  @Override
  public void setSerialDataBit(int val) {
    serialTab.setSerialDataBit(val);
  }

  @Override
  public SerialCommunicator.Parity getSerialParity() {
    return serialTab.getSerialParity();
  }

  @Override
  public void setSerialParity(SerialCommunicator.Parity val) {
    serialTab.setSerialParity(val);
  }

  @Override
  public int getSerialStopBit() {
    return serialTab.getSerialStopBit();
  }

  @Override
  public void setSerialStopBit(int val) {
    serialTab.setSerialStopBit(val);
  }

  @Override
  public int getSerialTimeout() {
    return serialTab.getSerialTimeout();
  }

  @Override
  public void setSerialTimeout(int val) {
    serialTab.setSerialTimeout(val);
  }

  @Override
  public void setSerialPortNames(String[] names) {
    serialTab.setSerialPortNames(names);
  }

  @Override
  public void setSerialDataRates(String[] names) {
    serialTab.setSerialDataRates(names);
  }

  @Override
  public void setSerialDataBits(String[] names) {
    serialTab.setSerialDataBits(names);
  }

  @Override
  public void setSerialParities(String[] names) {
    serialTab.setSerialParities(names);
  }

  @Override
  public void setSerialStopBits(String[] names) {
    serialTab.setSerialStopBits(names);
  }

  @Override
  public void setVideoFrame(BufferedImage frame) {
    videoTab.setVideoFrame(frame);
  }

  @Override
  public void videoHasStopped() {
    videoTab.videoHasStopped();
  }

  @Override
  public boolean useFaceRecognition() {
    return videoTab.useFaceRecognition();
  }

  @Override
  public void setUseFaceRecognition(boolean use) {
    videoTab.setUseFaceRecognition(use);
  }

  @Override
  public String getClassifierFilename() {
    return videoTab.getClassifierFilename();
  }

  @Override
  public void setClassifierFilename(String classifierFilename) {
    videoTab.setClassifierFilename(classifierFilename);
  }

  @Override
  public boolean isGameControllerEnabled() {
    return gameControllerTab.isGameControllerEnabled();
  }

  @Override
  public void setGameCOntrollerEnabled(boolean enable) {
    gameControllerTab.setGameCOntrollerEnabled(enable);
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    armController.shutdown();
    System.exit(0);
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }
  
  @Override
  public void setShoulderMaths(double mass, double length, double torque) {
    mathsTab.setShoulderMaths(mass, length, torque);
  }
  
  @Override
  public void setElbowMaths(double mass, double length, double torque) {
    mathsTab.setElbowMaths(mass, length, torque);
  }
  
  @Override
  public void setWristMaths(double mass, double length, double torque) {
    mathsTab.setWristMaths(mass, length, torque);
  }
  
  @Override
  public void setGravity(double gravity) {
    mathsTab.setGravity(gravity);
  }

  public static Double getDouble(double value) {
    return Double.valueOf(String.format("%.3f", value));
  }

  @Override
  public void setShoulderVerticalPosition(int deg) {
    controlTab.setShoulderVerticalPosition(deg);
  }

  @Override
  public void setElbowVerticalPosition(int deg) {
    controlTab.setElbowVerticalPosition(deg);
  }

  @Override
  public void setWristVerticalPosition(int deg) {
    controlTab.setWristVerticalPosition(deg);
  }

  @Override
  public void setWristRotationalPosition(int deg) {
    controlTab.setWristRotationalPosition(deg);
  }

  @Override
  public void setShoulderHorizontalPosition(int deg) {
    controlTab.setShoulderHorizontalPosition(deg);
  }

  @Override
  public void setClampAperturelPosition(int deg) {
    controlTab.setClampAperturelPosition(deg);
  }

  @Override
  public void setClampLength(double len) {
    controlTab.setClampLength(len);
  }

  @Override
  public void setElbowLength(double len) {
    controlTab.setElbowLength(len);
  }

  @Override
  public void setShoulderLength(double len) {
    controlTab.setShoulderLength(len);
  }

  @Override
  public void setBaseHeight(double len) {
    controlTab.setBaseHeight(len);
  }

  /**
   * Sets the time between commands.
   * @param time Time in milliseconds.
   */
  @Override
  public void setTimeBetweenCommands(int time) {
    serialTab.setTimeBetweenCommands(time);
  }
  
  /**
   * Sets the full pathname of the log file.
   * @param pathname
   *        The full pathname of the log file.
   */
  @Override
  public void setLogFilePathname(String pathname) {
    serialTab.setLogFilePathname(pathname);
  }
  
  @Override
  public String getProgramPathname() {
    return programTab.getProgramPathname();
  }
  
  @Override
  public void notifyProgramMessage(Level level, String message) {
    programTab.notifyProgramMessage(level, message);
  }

  @Override
  public void notifyProgramMessage(Level level, String message, Object param) {
    programTab.notifyProgramMessage(level, message, param);
  }
}
