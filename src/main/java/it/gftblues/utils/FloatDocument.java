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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Document for handling float values.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 * 
 */
public class FloatDocument extends PlainDocument {
  public static final String FLOAT = "0123456789.";

  protected String acceptedChars = null;

  protected boolean negativeAccepted = false;

  public FloatDocument() {
    this(FLOAT);
  }

  public FloatDocument(String acceptedchars) {
    acceptedChars = acceptedchars;
  }

  public void setNegativeAccepted(boolean negativeaccepted) {
    if (acceptedChars.equals(FLOAT)) {
      negativeAccepted = negativeaccepted;
      acceptedChars += "-";
    }
  }

  @Override
  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
    if (str == null)
      return;

    for (int i = 0; i < str.length(); i++) {
      if (!acceptedChars.contains(str.valueOf(str.charAt(i))))
        return;
    }

    if (acceptedChars.equals(FLOAT) || (acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
      if (str.contains(".")) {
        if (getText(0, getLength()).contains(".")) {
          return;
        }
      }
    }

    if (negativeAccepted && str.contains("-")) {
      if (str.indexOf("-") != 0 || offset != 0) {
        return;
      }
    }

    super.insertString(offset, str, attr);
  }
}
