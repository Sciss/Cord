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

package object impl {
  implicit class NodeImplOps(private val node: Node) extends AnyVal {
    def messageInlet(fun: M => Unit): Inlet =
      new MessageInletImpl(node, fun)

    def messageOutlet: Outlet with (M => Unit) =
      new MessageOutletImpl(node)
  }
}
