/*
 *  PatcherView.scala
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

import de.sciss.cord.view.impl.PatcherViewImpl
import org.scalajs.dom

object PatcherView {
  def apply(patcher: Patcher): PatcherView = new PatcherViewImpl(patcher)

  sealed trait Update { def patcher: PatcherView }
  case class ModeChanged(patcher: PatcherView, editing: Boolean) extends Update
  case class Added      (patcher: PatcherView, elems: View*) extends Update
  case class Removed    (patcher: PatcherView, elems: View*) extends Update
}
trait PatcherView extends Model[PatcherView.Update] {
  def patcher: Patcher

  def container: dom.html.Element

  /** Whether the view is in editing mode (`true`) or performance mode (`false`). */
  var editing: Boolean

  def getView(elem: Elem): Option[View]

  def selection: PatcherSelection
}
