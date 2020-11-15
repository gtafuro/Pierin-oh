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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Table cell renderer for the Maths table.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class JointTableCellRenderer extends DefaultTableCellRenderer {

  private final static Color EDITABLE_CELL_COLOR = new Color(0xff, 0xf6, 0xd5);
  private final static Color SELECTED_EDITABLE_CELL_COLOR = new Color(0xff, 0xae, 0x6a);

  @Override
  public Component getTableCellRendererComponent(
          JTable table, 
          Object value,
          boolean isSelected, 
          boolean hasFocus, 
          int row, 
          int column
  ) {
      TableModel tm = table.getModel();
      boolean cellEditable;
      if (tm != null) {
        cellEditable = tm.isCellEditable(row, column);
      } else {
        cellEditable = false;
      }
      if (isSelected) {
        if (cellEditable) {
          setBackground(SELECTED_EDITABLE_CELL_COLOR);
        } else {
          setBackground(table.getSelectionBackground());
        }
        setForeground(table.getSelectionForeground());
      } else {
        setForeground(table.getForeground());
        if (cellEditable) {
          setBackground(EDITABLE_CELL_COLOR);
        } else {
          setBackground(table.getBackground());
        }
      }

    setValue(value);

    this.setHorizontalAlignment(RIGHT);
    return this;
  }
}
