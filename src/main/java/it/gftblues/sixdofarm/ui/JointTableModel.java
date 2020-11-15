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

import javax.swing.table.DefaultTableModel;

/**
 * Table model for the maths table.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class JointTableModel extends DefaultTableModel {

  private double gravity;
  private double armMass;
  
/*  @SuppressWarnings("rawtypes")
  public JointTableModel(Vector<? extends Vector> data, Vector<?> columnNames) {
    setDataVector(data, columnNames);
  }*/

  public JointTableModel() {
    super(
      new Object [][] {
        {
          SixDOFArmResources.getString("JointTableModelMass"), 
          0.0d, 
          0.0d, 
          SixDOFArmResources.getString("JointTableModelKg")
        },
        {
          SixDOFArmResources.getString("JointTableModelLength"), 
          0.0d, 
          0.0d, 
          SixDOFArmResources.getString("JointTableModelM")},
        {
          SixDOFArmResources.getString("JointTableModelTorque"), 
          0.0d, 
          0.0d, 
          "<html>J = kg×m<sup>2</sup>/s<sup>2</sup> = Nm</html>"
        }
      },
      new String [] {
        SixDOFArmResources.getString("JointTableModelQuantity"), 
        SixDOFArmResources.getString("JointTableModelValue"), 
        SixDOFArmResources.getString("JointTableModelValue"), 
        SixDOFArmResources.getString("JointTableModelUnits")
      }
    );
  }

  /**
   * Returns true if the referred cell is editable.
   *
   * @param   row             the row whose value is to be queried
   * @param   column          the column whose value is to be queried
   * @return                  true
   * @see #setValueAt
   */
  @Override
  public boolean isCellEditable(int row, int column) {
    return ((column == 1 && (row >= 0 && row <= 1)) || (column == 2 && row == 2));
  }

  public void setGravity(double gravity) {
    this.gravity = gravity;
    calculate();
  }

  public void setArmMass(double mass) {
    this.armMass = mass;
    calculate();
  }
  
  public double getMass() {
    return getDoubleValue(getValueAt(0, 1));
  }

  public void setMass(double mass) {
    setValueAt(mass, 0, 1);
    calculate();
  }

  public double getLength() {
    return getDoubleValue(getValueAt(1, 1));
  }

  public void setLength(double length) {
    setValueAt(length, 1, 1);
    setValueAt(length, 1, 2);
    calculate();
  }
  
  public double getTorque() {
    return getDoubleValue(getValueAt(2, 2));
  }

  public void setTorque(double torque) {
    setValueAt(torque, 2, 2);
    calculate();
  }

  public void calculate() {
    double mass = getDoubleValue(getValueAt(0, 1));
    double length = getDoubleValue(getValueAt(1, 1));
    double torque = getDoubleValue(getValueAt(2, 2));
    this.setValueAt(mass*gravity*length, 2, 1);
    this.setValueAt(torque/(gravity*length), 0, 2);
  }
  
  static private double getDoubleValue(Object val) {
    if (val == null) {
      return 0.0d;
    }
    
    return (Double)val;
  }
  
  /**
     *  Returns the object class according to the <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

  @Override
  public void setValueAt(Object aValue, int row, int column) {
    if (aValue != null && aValue instanceof Double) {
      String val = String.format("%.2f", (Double)aValue);
      aValue = Double.valueOf(val);
    }
    super.setValueAt(aValue, row, column);
  }
}
