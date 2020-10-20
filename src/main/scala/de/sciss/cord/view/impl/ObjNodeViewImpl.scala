/*
 *  ObjNodeViewImpl.scala
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

class ObjNodeViewImpl(val parentView: PatcherView, val elem: ObjNode, elemText: String)
  extends RectNodeViewImpl {

  init()

  protected def boxWidth: Int = elemText.length * 7 + 8

  override def toString = s"View of $elem"

  override protected def init(): Unit = {
    super.init()

    import scalatags.JsDom.all._
    import scalatags.JsDom.svgAttrs._
    import scalatags.JsDom.svgTags._
    val textTree = text(cls := "cord-node-name", x := 4, y := 15, elemText).render
    peer.appendChild(textTree)
  }

  def dispose(): Unit = ()
}
