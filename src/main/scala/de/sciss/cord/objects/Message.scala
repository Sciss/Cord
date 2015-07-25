/*
 *  Message.scala
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
package objects

import de.sciss.cord.impl.{ModelImpl, NodeImpl, NodeImplOps}
import de.sciss.cord.view.{MessageNodeView, PatcherView, View}

import scala.scalajs.js

class Message(val parent: Patcher, var args: List[Any])
  extends NodeImpl
  with de.sciss.cord.Message
  with ModelImpl[String] {

  private val _vars = js.Array[Any]()
  private var _message: M = null

  private def clearVars(): Unit = {
    var idx = 0
    while (idx < 9) {
      _vars(idx) = 0
      idx += 1
    }
  }

  // _vars.length = 9
  clearVars()

  private def argsUpdated(): Unit = {
    _message  = null // invalidate
    clearVars()
    dispatch(contents)
  }

  private def updateMessage(): Unit = {
    val argsOut = args.map {
      case arg: String if arg.length == 2 && arg.charAt(0) == '$' =>
        val c = arg.charAt(1)
        if (c >= '1' && c <= '9') _vars(c - '1') else arg
      case arg => arg
    }
    _message = M(argsOut: _*)
  }

  def message: M = {
    if (_message == null) updateMessage()
    // println(s"MESSAGE = ${_message}")
    _message
  }

  def contents: String = args.mkString(" ")

  val outlet = this.messageOutlet

  val inlet = this.messageInlet {
    case M.Bang =>
      outlet(message)
    case M("set", rest @ _*) =>
      args = rest.toList
      // println(s"SET ARGS = $args")
      argsUpdated()
    case M("append", rest @ _*) =>
      args ++= rest
      argsUpdated()
    case M("prepend", rest @ _*) =>
      args :::= rest.toList
      argsUpdated()
    case other =>
      val it  = other.atoms.iterator
      var idx = 0
      while (it.hasNext && idx < 9) {
        _vars(idx) = it.next()
        idx += 1
      }
      updateMessage()
      outlet(message)
  }

  def view(parentView: PatcherView): View = MessageNodeView(parentView, this)
}
