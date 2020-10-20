/*
 *  PortImpl.scala
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
package impl

import org.scalajs.dom.AudioNode

trait PortImpl extends ModelImpl[Port.Update] {
  _: Port =>

  protected var _cords = List.empty[Cord]

  def cords: List[Cord] = _cords

  def removeCord(cord: Cord): Unit = {
    val i = _cords.indexOf(cord)
    if (i < 0) throw new Exception(s"Trying to remove a cord ($cord) that was not connected to port ($this)")
    _cords = _cords.patch(i, Nil, 1)
    cordRemoved(cord)
    dispatch(Port.CordRemoved(this, cord))
  }

  protected def cordRemoved(cord: Cord): Unit = ()
}

trait InletImpl extends PortImpl with Inlet {
  def addCord(cord: Cord): Unit = {
    if (!accepts(cord.tpe)) throw new Exception(s"Cannot connect a cord of type ${cord.tpe} to this port ($this)")
    if (_cords.contains(cord)) throw new Exception(s"Cannot connect cord ($cord) twice to the same port ($this)")

    _cords ::= cord
    cordAdded(cord)
    dispatch(Port.CordAdded(this, cord))
  }

  protected def cordAdded(cord: Cord): Unit = ()
}

trait OutletImpl extends PortImpl with Outlet {
  def addCord(cord: Cord): Unit = {
    if (this.tpe != cord.tpe) throw new Exception(s"Cannot connect a cord of type ${cord.tpe} to this port ($this)")
    if (_cords.contains(cord)) throw new Exception(s"Cannot connect cord ($cord) twice to the same port ($this)")

    _cords ::= cord
    dispatch(Port.CordAdded(this, cord))
  }
}

class MessageInletImpl(val node: Node, fun: M => Unit)
  extends InletImpl {

  def accepts(tpe: Type): Boolean = tpe == MessageType

  def ! (message: M): Unit = fun(message)
}

class MessageOutletImpl(val node: Node) extends OutletImpl with ((M) => Unit) {
  def tpe: Type = MessageType

  def apply(message: M): Unit = _cords.foreach { cord =>
    cord.sink ! message
  }

  def audio: AudioNode = throw new Exception("Not an audio outlet")
}

trait NoInlets {
  def inlets: List[Inlet] = Nil
}

trait NoOutlets {
  def outlets: List[Outlet] = Nil
}

trait SingleInlet {
  def inlets: List[Inlet] = inlet :: Nil
  def inlet: Inlet
}

trait SingleOutlet {
  def outlets: List[Outlet] = outlet :: Nil
  def outlet: Outlet
}