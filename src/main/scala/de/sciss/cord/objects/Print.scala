/*
 *  Print.scala
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
package objects

import de.sciss.cord.impl.{NoOutlets, ObjNodeImpl, SingleInlet, NodeImplOps}

class Print(val parent: Patcher, val args: List[Any] = Nil)
  extends ObjNodeImpl("print") with SingleInlet with NoOutlets {

  private val prefix = args match {
    case Nil => "print: "
    case "-n" :: Nil => ""
    case _ => args.mkString("", " ", ": ")
  }

  val inlet = this.messageInlet { message =>
    val m = message.atoms.mkString(prefix, " ", "")
    println(m)
  }
}
