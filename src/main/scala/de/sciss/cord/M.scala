/*
 *  M.scala
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

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object M {
  val Bang = M(de.sciss.cord.Bang)
}
@JSExport("cord.M")
case class M(atoms: Any*) {
  @JSExport("atoms") def toArray: js.Array[Any] = atoms.to[js.Array]

  override def toString = atoms.mkString(s"$productPrefix(", ", ", ")")
}