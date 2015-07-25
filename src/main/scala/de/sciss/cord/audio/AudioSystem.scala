/*
 *  AudioSystem.scala
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

package de.sciss.cord.audio

import org.scalajs.dom.raw.AudioContext

object AudioSystem {
  lazy val instance: AudioSystem = new AudioSystem

  def context: AudioContext = instance.context
}
class AudioSystem {
  val context = new AudioContext
}
