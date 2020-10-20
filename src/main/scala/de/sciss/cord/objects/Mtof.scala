/*
 *  Mtof.scala
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

import de.sciss.cord.impl.{NoArgs, ObjNodeImpl, SingleInlet, SingleOutlet}

class Mtof(val parent: Patcher)
  extends ObjNodeImpl("mtof") with SingleInlet with SingleOutlet with NoArgs {

  private val /* var */ base = 440.0

  val outlet = this.messageOutlet

  val inlet = this.messageInlet { m =>
    val freq = m.atoms.map {
      case d: Double => cpsmidi(d)
    }
    outlet(M(freq: _*))
  }

  private def cpsmidi(in: Double): Double = base * math.pow(2, (in - 69) / 12)
}