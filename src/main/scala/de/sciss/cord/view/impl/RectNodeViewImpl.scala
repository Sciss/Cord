/*
 *  RectNodeViewImpl.scala
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

package de.sciss.cord.view.impl

import org.scalajs.dom

import scalatags.JsDom.all.{width => _, height => _, _}
import scalatags.JsDom.svgAttrs._
import scalatags.JsDom.svgTags._

trait RectNodeViewImpl extends NodeViewImpl {
  val peer: dom.svg.G = {
    val loc       = elem.location
    val rectTree  = rect(cls := "cord-node", x := 0.5, y := 0.5, width := boxWidth, height := boxHeight)
    val res       = g(cls := "cord-node", rectTree, transform := s"translate(${loc.x},${loc.y})").render
    ports.foreach { case (_, port) =>
      res.appendChild(port)
    }
    res
  }
}
