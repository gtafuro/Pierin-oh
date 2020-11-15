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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Table for the maths calculation.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class JointTable extends JTable {
  
  public final static int COLUMN_QUANTITY = 0;
  public final static int COLUMN_VALUE1 = 1;
  public final static int COLUMN_VALUE2 = 2;
  public final static int COLUMN_UNITS = 3;
  
  public final static int COLUMN_QUANTITY_WIDTH = 70; //  96
  public final static int COLUMN_VALUE1_WIDTH = 70; //  96
  public final static int COLUMN_VALUE2_WIDTH = 70; //  96
  public final static int COLUMN_UNITS_WIDTH = 121; //  95

  private final static Color SELECTED_CELL_BACKGROUND_COLOR = new Color(0xff, 0x66, 0x00);
  private final static Color SELECTED_CELL_FOREGROUND_COLOR = new Color(0xf9, 0xf9, 0xf9);

  public JointTable() {
    addPropertyChangeListener((PropertyChangeEvent evt) -> {
      if ("tableCellEditor".equals(evt.getPropertyName())) {
        if (isEditing()) {
          recaluclate();
        } else {
          recaluclate();
        }
      }
    });
    TableCellRenderer tcr = new JointTableCellRenderer();
    setDefaultRenderer(Double.class, tcr);
  }
  
  @Override
  public Color getSelectionForeground() {
    return SELECTED_CELL_FOREGROUND_COLOR;
  }

  @Override
  public Color getSelectionBackground() {
    return SELECTED_CELL_BACKGROUND_COLOR;
  }

  @Override
  final public void addPropertyChangeListener(PropertyChangeListener listener) {
    super.addPropertyChangeListener(listener);
  }
  
  /**
   * Returns an appropriate editor for the cell specified by
   * <code>row</code> and <code>column</code>. If the
   * <code>TableColumn</code> for this column has a non-null editor,
   * returns that.  If not, finds the class of the data in this
   * column (using <code>getColumnClass</code>)
   * and returns the default editor for this type of data.
   * <p>
   * <b>Note:</b>
   * Throughout the table package, the internal implementations always
   * use this method to provide editors so that this default behavior
   * can be safely overridden by a subclass.
   *
   * @param row       the row of the cell to edit, where 0 is the first row
   * @param column    the column of the cell to edit,
   *                  where 0 is the first column
   * @return          the editor for this cell;
   *                  if <code>null</code> return the default editor for
   *                  this type of cell
   * @see DefaultCellEditor
   */
  @Override
  public TableCellEditor getCellEditor(int row, int column) {
      TableColumn tableColumn = getColumnModel().getColumn(column);
      TableCellEditor editor = tableColumn.getCellEditor();
      if (editor == null) {
          editor = getDefaultEditor(getColumnClass(column));
      }
      return editor;
  }

  public void setWidths() {
    int width = this.getPreferredSize().width;
    TableColumnModel tcm = this.getColumnModel();
    tcm.getColumn(COLUMN_QUANTITY).setPreferredWidth(COLUMN_QUANTITY_WIDTH);
    tcm.getColumn(COLUMN_VALUE1).setPreferredWidth(COLUMN_VALUE1_WIDTH);
    tcm.getColumn(COLUMN_VALUE2).setPreferredWidth(COLUMN_VALUE2_WIDTH);
    tcm.getColumn(COLUMN_UNITS).setPreferredWidth(
            width-COLUMN_QUANTITY_WIDTH-COLUMN_VALUE1_WIDTH-COLUMN_VALUE2_WIDTH
    );
  }
  
  void printWidths() {
    System.out.println("0 - "+getColumnModel().getColumn(0).getPreferredWidth());
    System.out.println("1 - "+getColumnModel().getColumn(1).getPreferredWidth());
    System.out.println("2 - "+getColumnModel().getColumn(2).getPreferredWidth());
    System.out.println("3 - "+getColumnModel().getColumn(3).getPreferredWidth());
  }

  private void recaluclate() {
    TableModel tm = getModel();
    if (tm != null && tm instanceof JointTableModel) {
      JointTableModel jtm = (JointTableModel)tm;
      jtm.calculate();
    }
  }
  
  
}
