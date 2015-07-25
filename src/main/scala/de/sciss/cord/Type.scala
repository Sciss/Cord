/*
 *  Type.scala
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

sealed trait Type { def name: String }
case object AudioType   extends Type { def name = "audio"   }
case object MessageType extends Type { def name = "message" }