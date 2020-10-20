/*
 *  NodeImpl.scala
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

import scala.collection.mutable

trait NodeImpl {
  node: Node =>

  object state extends State with ModelImpl[State.Update] {
    private val map = mutable.Map.empty[String, Any]

    def put(key: String, value: Any): Unit =
      map.put(key, value).fold {
        dispatch(State.Added(node, key = key, value = value))
      } { oldValue =>
        if (value != oldValue) dispatch(State.Changed(node, key = key, before = oldValue, now = value))
      }

    def get(key: String): Option[Any] = map.get(key)

    def remove(key: String): Unit =
      map.remove(key).foreach { oldValue =>
        dispatch(State.Removed(node, key = key, value = oldValue))
      }
  }

  def dispose(): Unit = ()
}
