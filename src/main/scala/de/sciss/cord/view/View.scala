/*
 *  View.scala
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
package view

import org.scalajs.dom

trait View extends Disposable {
  def parentView: PatcherView

  def elem: Elem

  def peer: dom.svg.Element
}
