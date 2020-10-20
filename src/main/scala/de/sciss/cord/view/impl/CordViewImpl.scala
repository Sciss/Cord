/*
 *  CordViewImpl.scala
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
import org.scalajs.dom.raw.SVGAnimatedLength

trait CordViewImpl extends View {
  def elem: Cord

  protected def init(): Unit = {
    val source      = elem.source
    val sink        = elem.sink
    val sourceView  = parentView.getView(source.node)
    val sinkView    = parentView.getView(sink  .node)
    sourceView.foreach(setSourceLoc)
    sinkView  .foreach(setSinkLoc  )
  }

  private def mkPoint(n: NodeView, port: Port): DoublePoint2D = {
    val loc     = n.portLocation(port)
    val eLoc    = n.elem.location
    // val bounds  = n.peer.getBoundingClientRect()
    // val pBounds = n.parentView.container.getBoundingClientRect()
    // println(s"SOURCE BOUNDS (${bounds.left}, ${bounds.top}, ${bounds.width}, ${bounds.height})")
    val x       = eLoc.x + loc.x - 0.5
    val y       = eLoc.y + loc.y - 0.5
    DoublePoint2D(x, y)
  }

  protected def setStartPoint(pt: DoublePoint2D): Unit
  protected def setEndPoint  (pt: DoublePoint2D): Unit

  private def setSourceLoc(view: View): Unit = view match {
    case n: NodeView => setStartPoint(mkPoint(n, elem.source))
    case _ => Console.err.println(s"Cord $elem is not connected to a node?")
  }

  private def setSinkLoc(view: View): Unit = view match {
    case n: NodeView => setEndPoint(mkPoint(n, elem.sink))
    case _ => Console.err.println(s"Cord $elem is not connected to a node?")
  }

  def dispose(): Unit = ()
}

class MessageCordViewImpl(val parentView: PatcherView, val elem: Cord) extends CordViewImpl {
  val peer: dom.svg.Line = {
    import scalatags.JsDom.all.{height => _, width => _, tpe => _, _}
    import scalatags.JsDom.svgTags._
    line(cls := "cord-cord cord-message-cord").render
  }

  init()

  protected def setStartPoint(pt: DoublePoint2D): Unit = { peer.x1.baseVal.value = pt.x; peer.y1.baseVal.value = pt.y; }
  protected def setEndPoint  (pt: DoublePoint2D): Unit = { peer.x2.baseVal.value = pt.x; peer.y2.baseVal.value = pt.y; }
}

class AudioCordViewImpl(val parentView: PatcherView, val elem: Cord) extends CordViewImpl {
  import scalatags.JsDom.all.{height => _, width => _, tpe => _, _}
  import scalatags.JsDom.svgTags._

  private val line1 = line(cls := "cord-cord cord-audio-cord1").render
  private val line2 = line(cls := "cord-cord cord-audio-cord2").render

  val peer: dom.svg.Element = g(cls := "cord-node", line1, line2).render

  init()

  private def setPoint(xa: SVGAnimatedLength, ya: SVGAnimatedLength, xb: SVGAnimatedLength, yb: SVGAnimatedLength,
                       pt: DoublePoint2D): Unit = {
    xa.baseVal.value = pt.x
    ya.baseVal.value = pt.y
    xb.baseVal.value = pt.x
    yb.baseVal.value = pt.y
  }

  protected def setStartPoint(pt: DoublePoint2D): Unit = setPoint(line1.x1, line1.y1, line2.x1, line2.y1, pt)
  protected def setEndPoint  (pt: DoublePoint2D): Unit = setPoint(line1.x2, line1.y2, line2.x2, line2.y2, pt)
}
