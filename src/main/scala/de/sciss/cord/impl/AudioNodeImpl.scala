/*
 *  AudioNodeImpl.scala
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
package impl

trait AudioNodeImpl extends NodeImpl {
  node: Node =>

  private val dspL = parent.dsp.addListener {
    case true  => dspStarted()
    case false => dspStopped()
  }

  protected def dspStarted(): Unit
  protected def dspStopped(): Unit

  /** Should be called by sub-classes after they finished initialization. */
  protected def initDSP(): Unit = if (parent.dsp.active) dspStarted()

  override def dispose(): Unit = {
    if (parent.dsp.active) dspStopped()
    super.dispose()
    parent.dsp.removeListener(dspL)
  }
}
