/*
 *  NodeViewImpl.scala
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
package impl

import org.scalajs.dom

import scala.collection.mutable
import scalatags.JsDom.all.{height => _, width => _, _}
import scalatags.JsDom.svgAttrs._
import scalatags.JsDom.svgTags._

trait NodeViewImpl extends ViewImpl with NodeView {
  protected def boxWidth : Int
  protected def boxHeight: Int = 20

  private def mkPort(port: Port, isInlet: Boolean, idx: Int, num: Int): dom.svg.RectElement = {
    val py  = if (isInlet) 0 else boxHeight - 1
    val idx = port.index
    val px  =
      if      (idx == 0)       0
      else if (idx == num - 1) boxWidth - 7
      else                     (idx.toDouble / (num - 1) * (boxWidth - 7)).toInt

    rect(cls := "cord-port", x := px, y := py, width := 8, height := 2).render
  }

  private def mkPorts(b: mutable.Builder[(Port, dom.svg.RectElement), _], coll: List[Port], isInlet: Boolean): Unit = {
    val num = coll.size
    var idx = 0
    var rem = coll
    while (idx < num) {
      val head :: tail = rem
      val r = mkPort(head, isInlet = isInlet, idx = idx, num = num)
      b += head -> r
      idx += 1
      rem = tail
    }
  }

  protected val ports: Map[Port, dom.svg.RectElement] = {
    val b = Map.newBuilder[Port, dom.svg.RectElement]
    mkPorts(b, elem.inlets , isInlet = true )
    mkPorts(b, elem.outlets, isInlet = false)
    b.result()
  }

  def portLocation(port: Port): DoublePoint2D = {
    val r = ports(port)
    DoublePoint2D(r.x.baseVal.value + 4, r.y.baseVal.value + 1)
  }

  //  override protected def init(): Unit = {
  //    super.init()
  //    // XXX TODO --- create portlet
  //  }
}
