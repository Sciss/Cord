/*
 *  PatcherSelectionImpl.scala
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
package view
package impl

import de.sciss.cord.impl.ModelImpl

import scala.collection.mutable

class PatcherSelectionImpl(patcherView: PatcherView) extends PatcherSelection with ModelImpl[PatcherSelection.Update] {
  import PatcherSelection.{Added, Removed, Update}

  private val coll = mutable.Set.empty[View]

  def contains(view: View): Boolean = coll.contains(view)

  def clear(): Unit = if (coll.nonEmpty) {
    val removed = coll.toList
    coll.clear()
    updateAndDispatch(Removed(patcherView, removed: _*))
  }

  def add(views: View*): Unit = {
    val added = views.filter(coll.add)
    if (added.nonEmpty) updateAndDispatch(Added(patcherView, added: _*))
  }

  def remove(views: View*): Unit = {
    val removed = views.filter(coll.remove)
    if (removed.nonEmpty) updateAndDispatch(Removed(patcherView, removed: _*))
  }
  
  private def updateAndDispatch(u: Update): Unit = {
    u match {
      case Removed(_, views @ _*) => views.foreach(_.peer.classList.remove("selected"))
      case Added  (_, views @ _*) => views.foreach(_.peer.classList.add   ("selected"))
    }
    dispatch(u)
  }

  def size    : Int     = coll.size
  def isEmpty : Boolean = coll.isEmpty
  def nonEmpty: Boolean = coll.nonEmpty

  def get: Set[View] = coll.toSet
}