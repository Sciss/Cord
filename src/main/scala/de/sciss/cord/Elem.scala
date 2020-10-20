/*
 *  Elem.scala
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

import de.sciss.cord
import de.sciss.cord.impl.{CordImpl, SingleInlet, SingleOutlet}
import de.sciss.cord.view.{PatcherView, View}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

sealed trait Elem extends Disposable {
  def parent: Patcher
  def view(parentView: PatcherView): View
}

sealed trait Node extends Elem {
  def inlets  : List[Inlet ]
  def outlets : List[Outlet]
  def state   : State
  def args    : List[Any]
  def contents: String
}

trait Message extends Node with Model[String] with SingleInlet with SingleOutlet {
  def message: M
}

trait ObjNode extends Node {
  def name: String
}

@JSExportTopLevel("cord")
object Cord {
  @JSExport val M         = cord.M
  @JSExport val Demo      = cord.Demo
  @JSExport val Registry  = cord.Registry

  def apply(source: Outlet, sink: Inlet): Cord = new CordImpl(source, sink)
}
trait Cord extends Elem {
  def source: Outlet
  def sink  : Inlet
  def tpe   : Type
}