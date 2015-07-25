/*
 *  Receive.scala
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

import de.sciss.cord.impl.{NoInlets, NodeImplOps, ObjNodeImpl, SingleOutlet}

class Receive(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("receive") with NoInlets with SingleOutlet {

  private val portName = args match {
    case (n: String) :: Nil => n
    case _ => throw new IllegalArgumentException(s"'receive' requires one string argument, the port name")
  }

  val outlet = this.messageOutlet

  private val inlet = Registry.addReceive(portName, { m: M => outlet(m) })

  override def dispose(): Unit = {
    super.dispose()
    Registry.removeReceive(portName, inlet)
  }
}