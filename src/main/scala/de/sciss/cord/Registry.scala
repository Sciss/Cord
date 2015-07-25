/*
 *  Registry.scala
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

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport("cord.Registry")
object Registry {
  type Fun = js.Function1[M, Unit]
  
  private val senders   = js.Dictionary.empty[js.Array[Fun]]
  private val receivers = js.Dictionary.empty[js.Array[Fun]]

  private def fire(name: String, m: M): Unit = {
    val arr = receivers.getOrElse(name,
      throw new IllegalStateException(s"Sender for '$name' is not registered any longer"))
    arr.foreach { receiver =>
      try {
        receiver(m)
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }
  
  private def add(name: String, fun: Fun, map: js.Dictionary[js.Array[Fun]]): Unit = {
    val xs = map.getOrElseUpdate(name, js.Array())
    xs.push(fun)
  }

  private def remove(name: String, fun: Fun, map: js.Dictionary[js.Array[Fun]]): Unit = {
    val xs  = map.get(name).getOrElse(throw new NoSuchElementException(name))
    val idx = xs.indexOf(fun)
    if (idx < 0) throw new NoSuchElementException((name, fun).toString())
    xs.remove(idx)
    if (xs.isEmpty) {
      map.delete(name)
    }
  }

  def addSend(name: String): Fun = {
    val fun: Fun = { m: M =>
      fire(name, m)
    }
    add(name, fun, senders)
    fun
  }

  @JSExport
  def addReceive(name: String, fun: Fun): fun.type = {
    add(name, fun, receivers)
    fun
  }

  def removeSend   (name: String, fun: Fun): Unit = remove(name, fun, senders)
  def removeReceive(name: String, fun: Fun): Unit = remove(name, fun, senders)

}
