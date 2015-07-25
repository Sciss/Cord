/*
 *  Port.scala
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

import org.scalajs.dom

object Port {
  sealed trait Update { def port: Port }
  case class CordAdded  (port: Port, cord: Cord) extends Update
  case class CordRemoved(port: Port, cord: Cord) extends Update
}
sealed trait Port extends Model[Port.Update] {
  def node: Node

  def cords: List[Cord]

  def addCord   (cord: Cord): Unit
  def removeCord(cord: Cord): Unit
}

trait Inlet extends Port {
  def accepts(tpe: Type): Boolean

  /** Tries to send a message into this inlet. Throws an error if `MessageType` is not accepted. */
  def ! (message: M): Unit
}

trait Outlet extends Port {
  def tpe: Type

  /** Tries to retrieve the audio node whose output can be connected. Throws an error if `tpe` is not `AudioType`. */
  def audio: dom.AudioNode
}
