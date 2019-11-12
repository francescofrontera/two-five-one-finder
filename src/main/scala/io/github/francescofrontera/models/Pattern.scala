package io.github.francescofrontera.models

import io.github.francescofrontera.models.Chord.ChordRep
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.api.common.typeinfo.{TypeInformation, Types}

object Pattern {
  final val typeInformation: TypeInformation[IIVI] = TypeInformation.of(classOf[IIVI])
  final val stateDescriptorForPattern: MapStateDescriptor[String, IIVI] =
    new MapStateDescriptor("IIVI", Types.STRING, Pattern.typeInformation)

  sealed trait IIVI {
    def II: ChordRep

    def V: ChordRep

    def I: ChordRep

    final def progression: Seq[ChordRep] = Seq(II, V, I)

  }

  def inC: IIVI = new IIVI {
    val II: ChordRep = ChordRep(Definitions.D, Chord.MinSeven)
    val V: ChordRep = ChordRep(Definitions.G, Chord.Dominant)
    val I: ChordRep = ChordRep(Definitions.C, Chord.MajorSeven)
  }

  def inF: IIVI = new IIVI {
    val II: ChordRep = ChordRep(Definitions.G, Chord.MinSeven)
    val V: ChordRep = ChordRep(Definitions.C, Chord.Dominant)
    val I: ChordRep = ChordRep(Definitions.F, Chord.MajorSeven)
  }

  //so one...
  final val IIVIPatterns: Seq[IIVI] = Seq(Pattern.inC, Pattern.inF)
}
