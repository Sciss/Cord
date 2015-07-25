/*
 *  CordImpl.scala
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
package impl

import de.sciss.cord.view.{CordView, PatcherView, View}

class CordImpl(val source: Outlet, val sink: Inlet) extends Cord {
  init()

  def tpe: Type = source.tpe

  def parent: Patcher = source.node.parent

  def view(parentView: PatcherView): View = CordView(parentView, this)

  def dispose(): Unit = {
    sink  .removeCord(this)
    source.removeCord(this)
  }

  private def init(): Unit = {
    if (source.node.parent != sink.node.parent)
      throw new Exception(s"Source ($source) and sink ($sink) do not live in the same patcher")

    source.addCord(this)
    sink  .addCord(this)
  }
}
