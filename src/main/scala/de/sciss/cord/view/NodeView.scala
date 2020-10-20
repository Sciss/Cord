/*
 *  NodeView.scala
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
package view

import de.sciss.cord.view.impl.ObjNodeViewImpl
import org.scalajs.dom

object NodeView {
  /** Standard view for object nodes. */
  def apply(parentView: PatcherView, obj: ObjNode): NodeView =
    new ObjNodeViewImpl(parentView, obj, elemText = obj.contents)
}
trait NodeView extends View {
  override def elem: Node

  override def peer: dom.svg.G

  def portLocation(port: Port): DoublePoint2D
}
