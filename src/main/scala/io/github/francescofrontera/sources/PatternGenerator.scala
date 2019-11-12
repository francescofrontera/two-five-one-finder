package io.github.francescofrontera.sources

import io.github.francescofrontera.models.Chord.ChordRep
import io.github.francescofrontera.models.Pattern
import org.apache.flink.streaming.api.functions.source.SourceFunction

import scala.util.Random

object PatternGenerator {
  final def generator: SourceFunction[(String, ChordRep)] =
    new SourceFunction[(String, ChordRep)] {
      override def run(ctx: SourceFunction.SourceContext[(String, ChordRep)]): Unit = {
        while (true) {
          val p = Pattern.IIVIPatterns.map(_.progression)
          val name = Seq("songOne", "songTwo")(Random.nextInt(2))

          Random.shuffle(p).foreach { progression ⇒
            progression.foreach { chord ⇒
              ctx.getCheckpointLock.synchronized {
                ctx.collect((name, chord))
              }
            }
          }

          Thread.sleep(800)
        }
      }

      override def cancel(): Unit = {}
    }
}
