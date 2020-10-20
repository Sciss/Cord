/*
 *  BangViewImpl.scala
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

import de.sciss.cord.objects.Button
import org.scalajs.dom

import scala.scalajs.js

class ButtonViewImpl(val parentView: PatcherView, val elem: Button) extends RectNodeViewImpl {
  import scalatags.JsDom.all.{height => _, width => _, _}
  import scalatags.JsDom.svgAttrs._
  import scalatags.JsDom.svgTags._

  protected def boxWidth = 18

  private val elemL = elem.addListener { case _ => flash() }

  init()

  private var flashHandle = Int.MinValue

  private val stopFlash: js.Function0[Any] = () => {
    flashHandle = Int.MinValue
    peer.classList.remove("cord-flash")
  }

  private def flash(): Unit = {
    cancelFlash()
    peer.classList.add("cord-flash")
    flashHandle = dom.window.setTimeout(stopFlash, 150)
  }

  private def cancelFlash(): Unit = if (flashHandle != Int.MinValue) {
    dom.window.clearTimeout(flashHandle)
    flashHandle = Int.MinValue
  }

  def dispose(): Unit = {
    elem.removeListener(elemL)
    cancelFlash()
  }

  override protected def init(): Unit = {
    super.init()
    val circleElem = circle(cls := "cord-button", cx := 9.5, cy := 10.5, r := 6.5 /* 8.5 */).render
    peer.appendChild(circleElem)

    peer.mousePressed { e =>
      if (this.isClickAction(e)) {  // cause a bang
        elem.inlet ! M.Bang
        e.preventDefault()
      }
    }
  }
}