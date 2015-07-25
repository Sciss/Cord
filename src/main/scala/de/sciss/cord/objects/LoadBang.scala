/*
 *  LoadBang.scala
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

class LoadBang(val parent: Patcher)
  extends ObjNodeImpl("loadbang")
  with ModelImpl[Unit]
  with SingleInlet with SingleOutlet with NoArgs {

  val outlet = this.messageOutlet

  private val bangL = parent.addListener {
    case Patcher.Loaded(_) => outlet(M.Bang)
  }

  val inlet = this.messageInlet {
    case M.Bang => outlet(M.Bang)
  }

  override def dispose(): Unit = {
    super.dispose()
    parent.removeListener(bangL)
  }
}