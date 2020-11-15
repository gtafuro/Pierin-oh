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

import java.util.ResourceBundle;

/**
 * Textual resources for Pierin-oh!.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class SixDOFArmResources {

  /**
   * Resources.
   */
  private static final ResourceBundle resources =
          java.util.ResourceBundle.getBundle(
                  "it/gftblues/sixdofarm/SixDOFArm"
          );

  /**
    * Gets a string for the given key from this resource bundle or one of its 
    * parents.
    * 
    * @param key The key for the desired string.
    * @return  If found, the resource bundle for the given base name and the 
    *          default locale, {@code null} otherwise.
    */
  public static final String getString(String key) {
    if (resources.containsKey(key)) {
      return resources.getString(key);
    }
    return key;
  }
}
