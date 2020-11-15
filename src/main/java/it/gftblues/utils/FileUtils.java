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

/**
 * FileUtils
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for file.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 * 
 */
public class FileUtils {
  
  /**
   * Gets the extension of the {@code file}.
   * @param file The file from which extract the function.
   * @return The file extension.
   */
  static public String getExtension(File file) {
      String ext = null;
      if (file != null) {
          String filename = file.getName();
          int i = filename.lastIndexOf('.');
          if (i > 0) {
              ext = filename.substring(i+1);
          }
      }
      return ext;
  }

  /**
   * Gets all the system environment.
   * @return A Map with all system environment variables.
   */
  static private Map<String, String> getEnv() {
      Map<String, String> fromEnv = System.getenv();
      Map<String, String> toReturn = new HashMap<>();
      for (String key : fromEnv.keySet()) {
          toReturn.put(key.toLowerCase(), fromEnv.get(key));
      }
      return toReturn;
  }

  /**
   * Expands all environment variable found in a pathname.
   * @param filename The pathname.
   * @return An expanded pathname.
   */
  static public String expandFileName(String filename) {
      if (filename == null) {
          return null;
      }
      Pattern p = Pattern.compile("%([^%]*)%");
      Matcher m = p.matcher(filename);
      Map<String, String> env = getEnv();
      while (m.find()) {
          String var = m.group(1);
          String value = env.get(var.toLowerCase());
          if (value != null) {
              filename = filename.replaceAll("%" + var + "%",
                      Matcher.quoteReplacement(value));
          } else {
              return filename;
          }
          m = p.matcher(filename);
      }
      return filename;
  }

  /**
   * Loads a jar DLL
   * @param name the pathname of the library.
   * @throws IOException 
   */
  public static void loadJarDll(String name) throws IOException {
    File temp;
      try (InputStream in = FileUtils.class.getResourceAsStream(name)) {
        byte[] buffer = new byte[1024];
        int read;
        temp = File.createTempFile(name, "");
//        temp = new File(new File(System.getProperty("java.io.tmpdir")), name);
        try (FileOutputStream fos = new FileOutputStream(temp)) {
          while((read = in.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
          }
        }
      }
    System.load(temp.getAbsolutePath());
  }
}
