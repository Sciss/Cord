/*
 *  M.scala
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

import scala.scalajs.js
import scala.scalajs.js.JSConverters.iterableOnceConvertible2JSRichIterableOnce
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object M {
  val Bang = M(de.sciss.cord.Bang)
}
//@JSExportTopLevel("cord.M")
case class M(atoms: Any*) {
  @JSExport("atoms") def toArray: js.Array[Any] = atoms.toJSArray

  override def toString = atoms.mkString(s"$productPrefix(", ", ", ")")
}