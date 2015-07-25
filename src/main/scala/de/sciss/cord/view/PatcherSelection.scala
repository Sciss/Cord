/*
 *  PatcherSelection.scala
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

import de.sciss.cord.view.impl.PatcherSelectionImpl

object PatcherSelection {
  type Listener = Model.Listener[Update]

  sealed trait Update { def patcherView: PatcherView }
  case class Added  (patcherView: PatcherView, views: View*) extends Update
  case class Removed(patcherView: PatcherView, views: View*) extends Update

  def apply(patcherView: PatcherView): PatcherSelection = new PatcherSelectionImpl(patcherView)
}
trait PatcherSelection extends Model[PatcherSelection.Update] {

  def contains(view: View): Boolean

  def clear(): Unit

  def add   (views: View*): Unit
  def remove(views: View*): Unit

  def size: Int

  def isEmpty : Boolean
  def nonEmpty: Boolean

  def get: Set[View]
}
