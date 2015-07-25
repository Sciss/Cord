package de.sciss.cord
package incomplete

import de.sciss.cord.audio.AudioProcessingEvent
import org.scalajs.dom.{AudioContext, AudioNode}

import scala.scalajs.js

class WhiteNoise {
  def node(context: AudioContext): AudioNode = {
    val res = context.createScriptProcessor(4096, 0, 1)
    res.onaudioprocess = { e: AudioProcessingEvent =>
      // e.playbackTime
      val output = e.outputBuffer.getChannelData(0)
      var i = 0; while (i < 4096) {
        output(i) = (js.Math.random() * 2 - 1).toFloat
      i += 1
      }
    }
    res
  }
}
