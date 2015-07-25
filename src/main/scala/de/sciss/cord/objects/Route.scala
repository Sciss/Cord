/*
 *  Route.scala
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
package objects

import de.sciss.cord.impl.{NodeImplOps, ObjNodeImpl, SingleInlet}

trait RouteLike extends ObjNode with SingleInlet {
  val outlets = List.fill(args.size + 1)(this.messageOutlet)
  private val outRest = outlets.last

  val inlet = this.messageInlet { m =>
    // XXX TODO -- an empty message is illogical; should forbid that case
    val idx = args.indexOf(m.atoms.head)
    if (idx >= 0) {
      val ms = stripMatch(m)
      // println(s"ROUTE AT $idx = $m -> $ms")
      outlets(idx)(ms)
    } else outRest(m)
  }

  protected def stripMatch(in: M): M
}

class Route(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("route") with RouteLike {

  protected def stripMatch(in: M): M = M(in.atoms.tail: _*)
}

class RoutePass(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("routepass") with RouteLike {

  protected def stripMatch(in: M): M = in
}