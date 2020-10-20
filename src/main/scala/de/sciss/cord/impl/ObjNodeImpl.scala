/*
 *  ObjNodeImpl.scala
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
package impl

import de.sciss.cord.view.{NodeView, PatcherView}

abstract class ObjNodeImpl(val name: String) extends NodeImpl with ObjNode {
  /** The default implementation calls `View(this)` */
  def view(parentView: PatcherView): NodeView = NodeView(parentView, this)

  override def toString = s"$name@${hashCode.toHexString}"

  def contents: String = if (args.isEmpty) name else (name :: args).mkString(" ")
}