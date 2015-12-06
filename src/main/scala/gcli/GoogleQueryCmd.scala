package gcli

/**
  * @author Stephan Herzog
  * @since 06.12.15
  */
class GoogleQueryCmd {
  var startCommand: String = _
  var tld: String = _
  var limitToSite: String = _
  var q: String = _
  var timeFilter: String = _
  var noop: Boolean = false
  var hasArgsError = false

  def buildCommand: String = s"$startCommand https://www.google.$tld/?#q=$limitToSite$q$timeFilter"

  def isValid: Boolean = {
    // TODO implement
    true
  }
}
