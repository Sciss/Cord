/*
 *  Point2D.scala
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

package de.sciss.cord.view

import scala.language.implicitConversions

object IntPoint2D {
  implicit def fromTuple(tup: (Int, Int)): IntPoint2D = IntPoint2D(tup._1, tup._2)
}
case class IntPoint2D(x: Int, y: Int) {
  def + (that: IntPoint2D): IntPoint2D = IntPoint2D(this.x + that.x, this.y + that.y)
}

case class IntSize2D(width: Int, height: Int)

case class DoublePoint2D(x: Double, y: Double)