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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * Used in the {@code SixDOFArmControllerTab} for showing the robotic arm in the
 * background.
 * 
 * https://stackoverflow.com/questions/9816403/how-to-set-jframe-or-jpanel-background-image-in-eclipse-helios
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class JPanelWithBackground extends JPanel { 
  Image imageOrg = null; 
  Image image = null; 

  { 
    addComponentListener(new ComponentAdapter() { 
        @Override
        public void componentResized(ComponentEvent e) { 
          int w = JPanelWithBackground.this.getWidth(); 
          int h = JPanelWithBackground.this.getHeight(); 
    /*        image = w>0&&h>0?imageOrg.getScaledInstance(w,h,  
                    java.awt.Image.SCALE_SMOOTH):imageOrg; 
            JPanelWithBackground.this.repaint();*/
          BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
          Graphics2D bGr = bimage.createGraphics();
          bGr.drawImage(image, 0, 0, null);
          bGr.dispose();
          try {
            image = resizeImage(bimage, w, h);
          } catch (IOException ex) {
            Logger.getLogger(JPanelWithBackground.class.getName()).log(Level.SEVERE, null, ex);
          }
        } 
    }); 
  }

  private BufferedImage resizeImage(
          BufferedImage originalImage, 
          int targetWidth, 
          int targetHeight) throws IOException {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
  }

  public JPanelWithBackground(Image i) { 
    imageOrg=i; 
    image=i; 
    setOpaque(false); 
  }

  @Override
  public void paint(Graphics g) { 
    if (image!=null) {
      g.drawImage(image, 0, 0, null);
    }
    super.paint(g); 
  }
}
