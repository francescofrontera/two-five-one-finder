package io.github.francescofrontera

import io.github.francescofrontera.models.Chord.ChordRep
import io.github.francescofrontera.models.Pattern.IIVI
import io.github.francescofrontera.models.{Chord, Pattern}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.util.Collector

import scala.collection.JavaConverters._

final class FindIIVIProcess
    extends KeyedBroadcastProcessFunction[String, (String, ChordRep), IIVI, (String, Seq[ChordRep], Boolean)] {
  private[this] var accumulateChord: ListState[ChordRep] = _

  override def open(parameters: Configuration): Unit = {
    accumulateChord =
      getRuntimeContext.getListState(new ListStateDescriptor[ChordRep]("CHORD_ACC", classOf[Chord.ChordRep]))
  }

  override def processElement(
    value: (String, ChordRep),
    ctx: KeyedBroadcastProcessFunction[String, (String, ChordRep), IIVI, (String, Seq[ChordRep], Boolean)]#ReadOnlyContext,
    out: Collector[(String, Seq[ChordRep], Boolean)]
  ): Unit = {
    val bState   = ctx.getBroadcastState(Pattern.stateDescriptorForPattern).immutableEntries()
    val patterns = bState.asScala.map(_.getValue)

    val accChord = accumulateChord.get().asScala.toList
    accumulateChord.update((value._2 :: accChord).asJava)

    if (patterns.nonEmpty && accChord.length == 3) {
      val reversed = accChord.reverse
      val isIIVIProgression: Boolean = patterns.exists { p ⇒
        val two +: five +: one +: Nil = p.progression

        reversed match {
          case `two` :: `five` :: `one` :: Nil ⇒ true
          case _                               ⇒ false
        }
      }

      accumulateChord.clear()
      out.collect((ctx.getCurrentKey, reversed, isIIVIProgression))
    }
  }

  override def processBroadcastElement(
    value: IIVI,
    ctx: KeyedBroadcastProcessFunction[String, (String, ChordRep), IIVI, (String, Seq[ChordRep], Boolean)]#Context,
    out: Collector[(String, Seq[ChordRep], Boolean)]
  ): Unit = ctx.getBroadcastState(Pattern.stateDescriptorForPattern).put(value.I.note.toString, value)
}
