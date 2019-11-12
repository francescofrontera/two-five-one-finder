package io.github.francescofrontera.models

import io.github.francescofrontera.models.Definitions.Note


object Chord {

  sealed trait ChordType

  case object MinSeven extends ChordType

  case object MajorSeven extends ChordType

  case object Diminished extends ChordType

  case object Dominant extends ChordType

  final case class ChordRep(note: Note, chordType: ChordType)

}
