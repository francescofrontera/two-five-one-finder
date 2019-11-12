package io.github.francescofrontera.models

object Definitions {

  sealed trait Note

  case object C extends Note

  case object D extends Note

  case object E extends Note

  case object F extends Note

  case object G extends Note

  case object A extends Note

  case object B extends Note

}
