package gcli

import org.scalatest._

class GoogleQueryCmdSpec extends FlatSpec with Matchers {
  "A command" should "have correct field properties for search without time filters" in {
    val args: Array[String] = "--noconf hello world".split(" ")
    val (cmd: GoogleQueryCmd, hasArgsError: Boolean) = GCli.processArgs(args)
    cmd.q should be ("hello+world")
    cmd.startCommand should be ("open")
    cmd.tld should be ("com")
    cmd.noop should be (false)
    cmd.timeFilter should be ("")
    cmd.limitToSite should be ("")
  }


  // Tests for --quick option
  // –––

  it should "have a correct timeFilter field for last year" in {
    val args: Array[String] = "--noconf -q:y hello world".split(" ")
    val (cmd: GoogleQueryCmd, hasArgsError: Boolean) = GCli.processArgs(args)
    cmd.timeFilter should be ("&tbs=qdr:y")
  }

  it should "have a correct timeFilter field for last month" in {
    val args: Array[String] = "--noconf -q:m hello world".split(" ")
    val (cmd: GoogleQueryCmd, hasArgsError: Boolean) = GCli.processArgs(args)
    cmd.timeFilter should be ("&tbs=qdr:m")
  }

  it should "have a correct timeFilter field for last week" in {
    val args: Array[String] = "--noconf -q:w hello world".split(" ")
    val (cmd: GoogleQueryCmd, hasArgsError: Boolean) = GCli.processArgs(args)
    cmd.timeFilter should be ("&tbs=qdr:w")
  }

  it should "have a correct timeFilter field for last day" in {
    val args: Array[String] = "--noconf -q:d hello world".split(" ")
    val (cmd: GoogleQueryCmd, hasArgsError: Boolean) = GCli.processArgs(args)
    cmd.timeFilter should be ("&tbs=qdr:d")
  }

  it should "have a correct timeFilter field for last hour" in {
    val args: Array[String] = "--noconf -q:h hello world".split(" ")
    val (cmd: GoogleQueryCmd, hasArgsError: Boolean) = GCli.processArgs(args)
    cmd.timeFilter should be ("&tbs=qdr:h")
  }


  // Tests for year-range-options (--yfrom, --yto)
  // –––

  // TODO implement
}
