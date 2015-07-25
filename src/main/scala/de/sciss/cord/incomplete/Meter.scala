package de.sciss.cord
package incomplete

import de.sciss.cord.audio.AudioProcessingEvent
import org.scalajs.dom._
import org.scalajs.dom.html.Canvas

import scala.scalajs.js
import scalatags.JsDom.all._

class Meter { meter =>
  private var lastPeak  = 0.0
  private var lastRMS   = 0.0
  private var sqrSum    = 0.0
  private var sqrMax    = 0.0
  private var count     = 0

  lazy val render: Canvas = {
    val elem = canvas(cls := "meter").render
    elem
  }

  def peak: Double  = {
    if (count > 0) lastPeak = js.Math.sqrt(sqrMax)
    lastPeak
  }

  def rms: Double = {
    if (count > 0) lastRMS = js.Math.sqrt(sqrSum / count)
    lastRMS
  }

  def reset(): Unit = if (count > 0) {
    sqrSum  = 0.0
    sqrMax  = 0.0
    count   = 0
  }

  def node(context: AudioContext): AudioNode = {
    // println("INIT METER NODE")
    // val inNode  = context.createGain()
    // val squared = context.createGain()

    // val isFirefox = navigator.userAgent.toLowerCase.indexOf("firefox") > -1
    val blockSize = 512 // 0 // if (isFirefox) 512 else 0  // Chrome doesn't accept any blockSize by default (4096)
    val analyze = context.createScriptProcessor(blockSize, 1, 1)
    analyze.onaudioprocess = { e: AudioProcessingEvent =>
      // if (paintCount == 0) println("IN AUDIO LOOP")
      val input = e.inputBuffer.getChannelData(0)
      val len   = input.length
      var i = 0; while (i < len) {
        val x0 = input(i)
        val x = x0 * x0
        sqrSum += x
        if (x > sqrMax) sqrMax = x
        i += 1
      }
      count += len
    }

    // inNode  connect squared
    // inNode  connect squared.gain
    // squared connect analyze

    // THIS IS NEEDED FOR CHROME
    // XXX TODO --- look into Tone.js, they
    // call some GC-prevention function that might serve the same purpose.
    val dummy = context.createGain()
    dummy.gain.value = 0.0
    analyze connect dummy
    dummy connect context.destination

    // inNode
    analyze
  }

  private var animHandle = Int.MinValue

  private def startAnimation(): Unit = {
    stopAnimation()
    animHandle = setInterval(animStep _, 33.3)
  }

  private val ampdbFactor = 20 / js.Math.log(10)

  private def ampdb(in: Double): Double =
    js.Math.log(in) * ampdbFactor

  private var lastPeakPx = 0.0
  private var lastRMSPx  = 0.0
  // private var paintCount = 0

  private def animStep(): Unit = {
    val peakDB    = ampdb(peak)
    val floorDB   = -48
    val peakNorm  = peakDB / -floorDB + 1
    val rmsDB     = ampdb(rms)
    val rmsNorm   = rmsDB / -floorDB + 1
    val elem  = render

    // paintCount = (paintCount + 1) % 20
    // if (paintCount == 0) println(f"peak $peakDB%1.1f rms $rmsDB%1.1f sqrSum $sqrSum%1.2f count $count")

    reset()
    val w     = elem.width
    val h     = elem.height
    val px0   = js.Math.max(0, js.Math.min(w, peakNorm * w))
    val rx0   = js.Math.max(0, js.Math.min(w, rmsNorm * w))
    val px    = js.Math.max(lastPeakPx - 4, px0)
    val rx    = js.Math.max(lastRMSPx  - 4, rx0)
    if (lastPeakPx != px || lastRMSPx != rx) {
      lastPeakPx  = px
      lastRMSPx   = rx
      val ctx     = elem.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
      ctx.fillStyle = "#000000"
      ctx.fillRect(0, 0, w , h)
      ctx.fillStyle = "#FF0000"
      ctx.fillRect(0, 0, px, h)
      ctx.fillStyle = "#0000FF"
      ctx.fillRect(0, 0, rx, h)
    }
  }

  private def stopAnimation(): Unit = {
    if (animHandle != Int.MinValue) {
      clearInterval(animHandle)
      animHandle = Int.MinValue
    }
  }
}
