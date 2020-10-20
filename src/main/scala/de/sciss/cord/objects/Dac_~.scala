/*
 *  Dac_~.scala
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

import de.sciss.cord.audio.AudioSystem
import de.sciss.cord.impl.{AudioNodeImpl, InletImpl, NoArgs, NoOutlets, ObjNodeImpl, SingleInlet}
import org.scalajs.dom

class Dac_~(val parent: Patcher)
  extends ObjNodeImpl("dac~")
  with AudioNodeImpl
  with SingleInlet with NoOutlets with NoArgs { obj =>

  private var audioNodes = List.empty[dom.AudioNode]

  private def audioTarget = AudioSystem.context.destination

  protected def dspStarted(): Unit = {
    audioNodes = inlet.cords.collect {
      case cord if cord.tpe == AudioType => cord.source.audio
    }
    // println(s"dac~ connecting ${audioNodes.size} nodes.")
    audioNodes.foreach(_.connect(audioTarget))
  }

  protected def dspStopped(): Unit = {
    audioNodes.foreach(_.disconnect(audioTarget))
    audioNodes = Nil
  }

  object inlet extends InletImpl {
    def accepts(tpe: Type) = true

    def node: Node = obj

    /** Tries to send a message into this inlet. Throws an error if `MessageType` is not accepted. */
    def ! (message: M): Unit = message match {
      case M(0)      | M("stop" ) => parent.dsp.active = false
      case M(_: Int) | M("start") => parent.dsp.active = true
    }

    override def cordAdded  (cord: Cord): Unit = if (cord.tpe == AudioType && parent.dsp.active) {
      val audioNode = cord.source.audio
      audioNodes ::= audioNode
      audioNode.connect(audioTarget)
    }

    override def cordRemoved(cord: Cord): Unit = if (cord.tpe == AudioType && parent.dsp.active) {
      val audioNode = cord.source.audio
      audioNodes = audioNodes.diff(audioNode :: Nil)
      audioNode.disconnect(audioTarget)
    }
  }
}
