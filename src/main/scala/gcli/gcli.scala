package gcli

import java.net.URLEncoder


object GCli {
  import sys.process._

  def main(args: Array[String]) {
    val (queryCmd: GoogleQueryCmd, hasArgsError: Boolean) = processArgs(args)
    if (hasArgsError) sys.exit(0)
    if (!queryCmd.noop) executeCmd(queryCmd.buildCommand) else println(s"NOOP: would run ${queryCmd.buildCommand}")
  }

  /** Executes command on shell. */
  def executeCmd(cmd: String): Unit = cmd !

  /**
    * Processes and validates command line arguments for the gcli command.
    *
    * @param args args array from command invokation
    * @return populated instance of [[GoogleQueryCmd]] and a bool flag to signal whether parsing args was successful
    */
  def processArgs(args: Array[String]): (GoogleQueryCmd, Boolean) = {

    val queryCmd = new GoogleQueryCmd()
    var hasArgsError: Boolean = false

    getCliParser.parse(args, CliConfig()) match {
      case Some(config) =>

        val cfg = if(!config.noconf) gcli.Config.load() else gcli.Config.get()

        def getQuery = config.query.mkString(" ")

        // QuickOptions relate to Google's standard filters for last y|m|w|d|h.
        // –––
        val qdr = config.quick match {
          case QuickOptions.n => ""
          case _ => s"&tbs=qdr:${config.quick}"
        }

        // Process yearFrom and/or yearTo
        // –––
        val tbs = if (config.yearFrom > -1 || config.yearTo > -1) {
          val yf = if (config.yearFrom == -1) "" else config.yearFrom.toString
          val yt = if (config.yearTo == -1) "" else config.yearTo.toString
          s"&tbs=cdr:1,cd_min:$yf,cd_max:$yt"
        } else ""

        // Assemble final time-filter-query
        // –––
        val timeFilter = if (!qdr.isEmpty && !tbs.isEmpty) {
          println("Ambiguous params for --quick and --yearFrom/--yearTo. Filter for --quick will be ignored.")
          tbs
        }
        else if (qdr.length > 0) { qdr }
        else                     { tbs }

        // Use other Google search types like images, news or videos
        // –––
        def googleSearchType(): String = {
          if (config.images) return "&tbm=isch"
          if (config.videos) return "&tbm=vid"
          if (config.news) return "&tbm=nws"
          ""
        }

        // Limit search results to one domain using Google's site:xyz.com option
        // –––
        def limitToSite(): String = {
          if (!config.site.isEmpty && config.limitToStackoverflow)
            println("Ambiguous params for --site and --so. --so flag will be ignored.")

          if (!config.site.isEmpty) return s"site:${URLEncoder.encode(config.site, "utf-8")}+"
          if (config.limitToStackoverflow) return "site:stackoverflow.com+"
          ""
        }

        // Build query string and final command
        // –––
        val q: String = URLEncoder encode(getQuery, "utf-8")

        // Populate the query cmd
        queryCmd.startCommand = cfg.startCommand
        queryCmd.tld = cfg.tld
        queryCmd.q = q
        queryCmd.timeFilter = timeFilter
        queryCmd.limitToSite = limitToSite()
        queryCmd.googleSearchType = googleSearchType()
        if (config.noop) queryCmd.noop = true

      case None =>
        hasArgsError = true
    }

    (queryCmd, hasArgsError)
  }
}
