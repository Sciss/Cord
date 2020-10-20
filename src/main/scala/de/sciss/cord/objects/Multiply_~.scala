/*
 *  Multiply_~.scala
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
import de.sciss.cord.impl.{AudioNodeImpl, InletImpl, ObjNodeImpl, OutletImpl, SingleOutlet, NodeImplOps}
import org.scalajs.dom

class Multiply_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("*~")
  with AudioNodeImpl
  with SingleOutlet { obj =>

  private var audioNodes = List.empty[dom.AudioNode]

  private var gainElem = Option.empty[dom.GainNode]

  private var _mul: Double = args match {
    case (d: Double) :: Nil => d
    case Nil => 0.0
    case _ => throw new Exception(s"Illegal initial multiplication parameter $args")
  }

  object outlet extends OutletImpl {
    def node        = obj
    def tpe         = AudioType

    def audio: dom.AudioNode = gainElem.getOrElse {
      if (parent.dsp.active) dspStarted()
      gainElem.getOrElse(throw new Exception("DSP is not active"))
    }
  }

  def mul: Double = _mul
  def mul_=(value: Double): Unit = if (_mul != value) {
    _mul = value
    gainElem.foreach { g =>
      val time = g.context.currentTime // AudioSystem.context.currentTime
      // g.gain.cancelScheduledValues(time)
      g.gain.setValueAtTime(value, time)
      // g.gain.value = value
      // println(s"SETTING MUL TO $value AT $time @${g.hashCode().toHexString}")
      // osc.frequency.value = value
    }
  }

  protected def dspStarted(): Unit = if (gainElem.isEmpty) {
    val g = AudioSystem.context.createGain()
    g.gain.value = _mul
    audioNodes = inlet1.cords.map(_.source.audio)
    audioNodes.foreach { n =>
      // println(s"[A] CONNECTING TO ${g.hashCode.toHexString}")
      n.connect(g)
    }
    // println("SETTING GAIN")
    gainElem = Some(g)
  }

  protected def dspStopped(): Unit = gainElem.foreach { g =>
    audioNodes.foreach { n =>
      // println(s"[A] DISCONNECTING FROM ${g.hashCode.toHexString}")
      n.disconnect(g)
    }
    audioNodes = Nil
    gainElem = None
  }

  object inlet1 extends InletImpl {
    def accepts(tpe: Type) = tpe == AudioType

    def node: Node = obj

    def ! (message: M): Unit = throw new Exception("Does not accept messages")

    override def cordAdded  (cord: Cord): Unit = gainElem.foreach { g =>
      val audioNode = cord.source.audio
      audioNodes ::= audioNode
      // println(s"[B] CONNECTING TO ${g.hashCode.toHexString}")
      audioNode.connect(g)
    }

    override def cordRemoved(cord: Cord): Unit = gainElem.foreach { g =>
      val audioNode = cord.source.audio
      audioNodes = audioNodes.diff(audioNode :: Nil)
      // println(s"[B] DISCONNECTING FROM ${g.hashCode.toHexString}")
      audioNode.disconnect(g)
    }
  }

  val inlet2 = this.messageInlet {
    case M(d: Double) => mul = d
  }

  def inlets: List[Inlet] = inlet1 :: inlet2 :: Nil
}