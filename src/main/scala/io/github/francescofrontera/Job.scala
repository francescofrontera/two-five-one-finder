package io.github.francescofrontera


import io.github.francescofrontera.models.Pattern
import io.github.francescofrontera.models.Pattern.IIVI
import io.github.francescofrontera.sources.PatternGenerator
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object Job {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val IIVIPatterDS: DataStream[IIVI] = env.fromCollection(Pattern.IIVIPatterns)
    val bcedPattern: BroadcastStream[IIVI] = IIVIPatterDS.broadcast(Pattern.stateDescriptorForPattern)

    env
      .addSource(PatternGenerator.generator)
      .keyBy(_._1)
      .connect(bcedPattern)
      .process(new FindIIVIProcess())
      .print()

    env.execute("FIND TWO II - V - I Progression.")
  }
}
