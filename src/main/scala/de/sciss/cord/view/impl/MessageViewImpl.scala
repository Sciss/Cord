/*
 *  MessageViewImpl.scala
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

import scala.scalajs.js
import scalatags.JsDom.all.{width => _, height => _, _}
import scalatags.JsDom.svgAttrs._
import scalatags.JsDom.svgTags._

class MessageViewImpl(val parentView: PatcherView, val elem: Message,
                          private var contents: String)
  extends NodeViewImpl {

  private val polyElem = {
    val h   = boxHeight
    val h1  = h + 0.5
    polygon(cls := "cord-msg",
      points := s"0.5,0.5 0,0.5 0,5.5 0,${h - 4.5} 0,$h1 0.5,$h1").render
  }

  private val textTree = text(cls := "cord-node-name", x := 4, y := 15, contents).render

  private val elemL = elem.addListener { case newContents =>
    contents = newContents
    updatePoly()
    textTree.textContent = newContents
  }

  val peer: dom.svg.G = {
    val loc       = elem.location
    val res       = g(cls := "cord-node", polyElem, textTree, transform := s"translate(${loc.x},${loc.y})").render
    ports.foreach { case (_, port) =>
      res.appendChild(port)
    }
    res
  }

  private var flashHandle = Int.MinValue

  private val stopFlash: js.Function0[Any] = () => {
    flashHandle = Int.MinValue
    peer.classList.remove("cord-flash")
  }

  init()

  private def flash(): Unit = {
    cancelFlash()
    peer.classList.add("cord-flash")
    flashHandle = dom.window.setTimeout(stopFlash, 150)
  }

  private def cancelFlash(): Unit = if (flashHandle != Int.MinValue) {
    dom.window.clearTimeout(flashHandle)
    flashHandle = Int.MinValue
  }

  protected def boxWidth: Int = js.Math.max(30, contents.length * 7 + 13) // + 8

  private def updatePoly(): Unit = {
    val pt  = polyElem.points
    val w   = boxWidth + 0.5
    val w1  = w - 5
    pt.getItem(1).x = w
    pt.getItem(2).x = w1
    pt.getItem(3).x = w1
    pt.getItem(4).x = w
  }

  override protected def init(): Unit = {
    super.init()
    updatePoly()

    peer.mousePressed { e =>
      if (this.isClickAction(e)) {  // cause a bang
        elem.inlet ! M.Bang
        flash()
        e.preventDefault()
      }
    }
  }

  def dispose(): Unit = {
    elem.removeListener(elemL)
    cancelFlash()
  }
}
