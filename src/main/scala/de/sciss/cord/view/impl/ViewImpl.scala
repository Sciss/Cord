/*
 *  ViewImpl.scala
 *  (Cord)
 *
 *  Copyright (c) 2015-2020 Hanns Holger Rutz.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package de.sciss.cord
package view
package impl

trait ViewImpl extends View {
  protected def init(): Unit = {
    // handle selection
    peer.mousePressed { e =>
      if (e.button == 0 && !e.defaultPrevented && parentView.editing && !isMenu(e)) {
        val sel = parentView.selection
        if (!sel.contains(this)) {
          if (!e.shiftKey) sel.clear()
          sel.add(this)
        }
        // new DragBox(this, e)
        // e.stopPropagation()
        e.preventDefault()
      }
    }
  }
}
