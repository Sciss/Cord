/*
 *  package.scala
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

import de.sciss.cord.view.impl.{AudioCordViewImpl, ButtonViewImpl, MessageCordViewImpl, MessageViewImpl, ToggleViewImpl}
import org.scalajs.dom
import org.scalajs.dom.ModifierKeyEvent

package object view {
  def ButtonView(parentView: PatcherView, button: objects.Button): NodeView = new ButtonViewImpl(parentView, button)
  def ToggleView(parentView: PatcherView, toggle: objects.Toggle): NodeView = new ToggleViewImpl(parentView, toggle)

  def CordView(parentView: PatcherView, cord: Cord): View =
    if (cord.tpe == MessageType) new MessageCordViewImpl(parentView, cord)
    else                         new AudioCordViewImpl  (parentView, cord)

  def MessageNodeView(parentView: PatcherView, view: Message): NodeView =
    new MessageViewImpl(parentView, view, view.contents)

  def isMenu(e: ModifierKeyEvent): Boolean = if (isMac) e.metaKey else e.ctrlKey

  implicit class NodeViewOps(private val view: NodeView) extends AnyVal {
    /** Is a non-consumed left button click (in editing mode: while holding meta) */
    def isClickAction(e: dom.MouseEvent): Boolean =
      e.button == 0 && !e.defaultPrevented && (!view.parentView.editing || isMenu(e))
  }
}
