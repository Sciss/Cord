/*
 *  Send.scala
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

import de.sciss.cord.impl.{NoOutlets, ObjNodeImpl, SingleInlet, NodeImplOps}

class Send(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("send") with SingleInlet with NoOutlets {

  private val portName = args match {
    case (n: String) :: Nil => n
    case _ => throw new IllegalArgumentException(s"'send' requires one string argument, the port name")
  }

  private val outlet = Registry.addSend(portName)

  val inlet = this.messageInlet(outlet(_))

  override def dispose(): Unit = {
    super.dispose()
    Registry.removeSend(portName, outlet)
  }
}