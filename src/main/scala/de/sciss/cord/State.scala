/*
 *  State.scala
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

object State {
  sealed trait Update { def node: Node; def key: String; def newValue: Option[Any] }
  case class Added  (node: Node, key: String, value: Any) extends Update {
    def newValue = Some(value)
  }
  case class Removed(node: Node, key: String, value: Any) extends Update {
    def newValue = None
  }
  case class Changed(node: Node, key: String, before: Any, now: Any) extends Update {
    def newValue = Some(now)
  }

  val Location = "loc"
}
trait State extends Model[State.Update] {
  def put(key: String, value: Any): Unit

  def remove(key: String): Unit

  // def putAll(pairs: (String, Any)*): Unit
  // def removeAll(keys: String*): Unit

  def get(key: String): Option[Any]
}