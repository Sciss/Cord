/*
 *  Sfplay_~.scala
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
import de.sciss.cord.impl.{AudioNodeImpl, ObjNodeImpl, OutletImpl, NodeImplOps}
import org.scalajs.dom
import org.scalajs.dom.{AudioNode, MediaElementAudioSourceNode}

import scala.scalajs.js
import scalatags.JsDom
import scalatags.JsDom.all._

class Sfplay_~(val parent: Patcher, val args: List[Any] = Nil)
  extends ObjNodeImpl("sfplay~") with AudioNodeImpl { obj =>

  val outlet2 = this.messageOutlet

  // tracks media updates and reports them to right outlet
  private val funMedia: js.Function1[dom.Event, Unit] = { e: dom.Event =>
    val msg = e.`type` match {
      case "timeupdate"     => M("time"     , mediaElem.currentTime)
      case "durationchange" => M("duration" , mediaElem.duration)

      case "progress" =>
        val ranges  = mediaElem.buffered
        val num     = ranges.length
        var b       = List.empty[Double]
        var i = num - 1
        val rangesJ = ranges.asInstanceOf[js.Dynamic]
        while (i >= 0) {
          // cf. https://github.com/scala-js/scala-js-dom/issues/137
          // b ::= ranges.end  (i)
          // b ::= ranges.start(i)
          b ::= rangesJ.end  (i) .asInstanceOf[Double]
          b ::= rangesJ.start(i) .asInstanceOf[Double]
          i -= 1
        }
        M("buffered" :: b: _*)

      case other => M(other)
    }
    outlet2(msg)
  }

  // In these we are interested
  private val eventTypes = js.Array[String]("canplay", "durationchange", "emptied", "ended", "error",
    /* "loadedmetadata", */ "loadstart", "progress", "timeupdate")

  private val mediaElem = {
    val res = JsDom.tags.audio(cls := "pat-sf").render
    // Needed for Firefox, default in Chromium. cf. http://stackoverflow.com/questions/31590108
    res.preload = "auto"

    // For testing purposes, uncommenting the following block
    // will make the <audio> element visible in the browser

    /*
      res.controls = true
      org.scalajs.dom.document.body.appendChild(res)
     */

    eventTypes.foreach(res.addEventListener(_, funMedia))
    res
  }

  override def dispose(): Unit = {
    super.dispose()

    mediaElem.removeEventListener("canplay", funMedia)
    eventTypes.foreach(mediaElem.removeEventListener(_, funMedia))
    // cf. http://stackoverflow.com/questions/3258587
    mediaElem.src = ""
    mediaElem.load()
  }

  private var mediaSource = Option.empty[MediaElementAudioSourceNode]

  private var _playing = false
  def playing: Boolean = _playing
  def playing_=(value: Boolean): Unit = if (_playing != value) {
    _playing = value
    if (value) {
      // mediaElem.initialTime = 0.0
    }
    if (mediaSource.isDefined) {
      if (value) {
        mediaElem.play()
      } else {
        mediaElem.pause()
      }
    }
    if (!value) {
      // mediaElem.initialTime = 0.0
    }
  }

  private var _pausing = false
  def pausing: Boolean = _pausing
  def pausing_=(value: Boolean): Unit = if (_pausing != value) {
    _pausing = value
    if (mediaSource.isDefined) {
      if (value) {
        mediaElem.play()
      } else {
        mediaElem.pause()
      }
    }
  }

  private var _speed = 1.0
  def speed: Double = _speed
  def speed_=(value: Double): Unit = if (_speed != value) {
    _speed = value
    mediaElem.playbackRate = value
  }

  private var _url = ""
  def url: String = _url
  def url_=(value: String): Unit = if (_url != value) {
    _url = value
    mediaElem.src = value
    mediaElem.load()
  }

  // XXX TODO --- cannot create media source element twice from same media elem (says Chrome)
  private lazy val lazyM = AudioSystem.context.createMediaElementSource(mediaElem)

  protected def dspStarted(): Unit = if (mediaSource.isEmpty) {
    val m = lazyM // AudioSystem.context.createMediaElementSource(mediaElem)
    if (_playing && !_pausing) mediaElem.play()
    mediaSource = Some(m)
  }

  protected def dspStopped(): Unit = mediaSource.foreach { m =>
    mediaSource = None
    mediaElem.pause()
  }

  def inlets : List[Inlet ] = inlet1  :: inlet2  :: Nil
  def outlets: List[Outlet] = outlet1 :: outlet2 :: Nil

  // http://dev.w3.org/html5/spec-preview/media-elements.html
  private def debug(): Unit = {
    println(s"paused              = ${mediaElem.paused}")
    println(s"ended               = ${mediaElem.ended}")
//    println(s"defaultPlaybackRate = ${mediaElem.ended}")
//    println(s"playbackRate        = ${mediaElem.ended}")
    println(s"played              = ${mediaElem.played}")
    println(s"seeking             = ${mediaElem.seeking}")
    println(s"seekable            = ${mediaElem.seekable}")
//    println(s"volume              = ${mediaElem.volume}")
//    println(s"muted               = ${mediaElem.muted}")
    println(s"readyState          = ${mediaElem.readyState}")
    println(s"buffered            = ${mediaElem.buffered}")
    println(s"preload             = ${mediaElem.preload}")
    println(s"duration            = ${mediaElem.duration}")
    println(s"error               = ${mediaElem.error}")
    println(s"networkState        = ${mediaElem.networkState}")
  }

  val inlet1 = this.messageInlet {
    case M(i: Int) => playing = i != 0
    case M("open", u: String) => url = u
    case M("pause" ) => pausing = true
    case M("resume") => pausing = false
    case M("debug") => debug()
  }

  val inlet2 = this.messageInlet {
    case M(d: Double) => speed = d
  }

  object outlet1 extends OutletImpl {
    def tpe   = AudioType
    def node  = obj

    def audio: AudioNode = mediaSource.getOrElse {
      if (parent.dsp.active) dspStarted()
      mediaSource.getOrElse(throw new Exception("DSP is not active"))
    }
  }
}