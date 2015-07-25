/*
 *  PatcherImpl.scala
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
package impl

import de.sciss.cord.view.PatcherView

class PatcherImpl extends Patcher with ModelImpl[Patcher.Update] {

  private var _elems = Vector.empty[Elem] // XXX TODO -- perhaps a Queue (fast append) would suffice

  def dispose(): Unit = remove(_elems: _*)

  object dsp extends DSPStatus with ModelImpl[Boolean] {
    private var _active = false

    def active: Boolean = _active
    def active_=(value: Boolean): Unit = if (_active != value) {
      // important to store the new state before dispatching,
      // because connecting nodes might reach out for those
      // later in the listener sequence which in turn might
      // query the status.
      _active = value
      dispatch(value)
    }
  }

  def loadBang(): Unit = dispatch(Patcher.Loaded(this))

  def view(): PatcherView = PatcherView(this)

  def elems: Seq[Elem] = _elems

  def add(elems: Elem*): Unit = {
    _elems ++= elems
    dispatch(Patcher.Added(this, elems: _*))
  }

  def remove(elems: Elem*): Unit = {
    _elems = _elems.diff(elems)
    dispatch(Patcher.Removed(this, elems: _*))
  }
}