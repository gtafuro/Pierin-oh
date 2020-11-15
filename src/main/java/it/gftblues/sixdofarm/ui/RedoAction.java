package it.gftblues.sixdofarm.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.undo.UndoManager;

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
 * Redo action for undo manager.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public class RedoAction extends AbstractAction {
  private final UndoManager manager;

  public RedoAction(UndoManager manager) {
    this.manager = manager;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    manager.redo();
  }
}
