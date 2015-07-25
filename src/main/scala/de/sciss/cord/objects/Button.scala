/*
 *  Button.scala
 *  (Cord)
 *
 *  Copyright (c) 2015 Hanns Holger Rutz.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package de.sciss.cord
package objects

import de.sciss.cord.impl.{ModelImpl, NoArgs, ObjNodeImpl, SingleInlet, SingleOutlet}
import de.sciss.cord.view.{ButtonView, NodeView, PatcherView}

class Button(val parent: Patcher)
  extends ObjNodeImpl("button")
  with ModelImpl[Unit]
  with SingleInlet with SingleOutlet with NoArgs {

  override def view(parentView: PatcherView): NodeView = ButtonView(parentView, this)

  val outlet = this.messageOutlet

  val inlet = this.messageInlet { _ =>
    dispatch(())
    outlet(M.Bang)
  }
}