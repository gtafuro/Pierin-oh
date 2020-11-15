package it.gftblues.utils;

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

import java.awt.Color;
import java.io.File;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


/**
 * Text Field for inserting a valid filename.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 * 
 */
public class ValidFilename extends JTextField {

  public ValidFilename() {
    getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        setForeground();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        setForeground();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        setForeground();
      }
    });
  }

  @Override
  public final Document getDocument() {
    return super.getDocument();
  }

  private void setForeground() {
    if (exists(getText())) {
      setForeground(Color.black);
      setToolTipText(null);
    } else {
      setForeground(Color.red);
      setToolTipText("File does not exist");
    }
  }

  public boolean exists() {
    return exists(getText());
  }

  private static boolean exists(String t) {
    if (t == null) {
      return false;
    }
    File f = new File(t);
    return f.exists();
  }
}
