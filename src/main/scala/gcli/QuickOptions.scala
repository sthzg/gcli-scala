package gcli

/**
  * Enum representing the available options for Google's time-filters (last year, month, week, day, hour).
  */
object QuickOptions extends Enumeration {
  type QuickOptions = Value
  val y, m, w, d, h, n = Value
}
