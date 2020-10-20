/*
 *  Patcher.scala
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

import de.sciss.cord.impl.PatcherImpl
import de.sciss.cord.view.PatcherView

object Patcher {
  sealed trait Update { def patcher: Patcher }
  case class Added  (patcher: Patcher, elems: Elem*) extends Update
  case class Removed(patcher: Patcher, elems: Elem*) extends Update
  case class Loaded (patcher: Patcher) extends Update

  def apply(): Patcher = new PatcherImpl
}
trait Patcher extends Model[Patcher.Update] {
  def dispose(): Unit
  def view(): PatcherView

  def add   (elems: Elem*): Unit
  def remove(elems: Elem*): Unit

  def elems: Seq[Elem]

  def dsp: DSPStatus

  def loadBang(): Unit
}

trait DSPStatus extends Model[Boolean] {
  var active: Boolean
}