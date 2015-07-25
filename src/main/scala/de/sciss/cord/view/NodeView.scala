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
