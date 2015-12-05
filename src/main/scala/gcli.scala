package gcli

import java.io.File
import java.net.URLEncoder
import java.time.Year

import gcli.QuickOptions.QuickOptions


/**
  * Enum representing the available options for Google's time-filters (last year, month, week, day, hour).
  */
object QuickOptions extends Enumeration {
  type QuickOptions = Value
  val y, m, w, d, h, n = Value
}


object GCli {
  import sys.process._

  def main(args: Array[String]) {

    /**
      * Will return the year of the last century if the two digits given are greater than the current year, i.e.
      * for an input of 71 it will return 1971. 1-digit integers are treated as 0-padded (3 -> 03 -> 2003).
      *
      * @param year the year to be padded
      * @return returns a 2-digit value as a 4-digit integer
      */
    // TODO needs tests
    def paddedYear(year: Int): Int = {
      f"$year%02d".length match {
        case 2 =>
          val prefix = if (year > Year.now().getValue.toString.takeRight(2).toInt) {
            Year.now().getValue.toString.take(2).toInt - 1
          } else {
            Year.now().getValue.toString.take(2).toInt
          }
          f"$prefix$year%02d".toInt
        case _ => year
      }
    }


    /**
      * If an integer less than 10 is given, it will be 0-padded, i.e. an input of 6 is interpreted as 06 and
      * passes validation.
      *
      * @param year the year to be validated
      * @return true if the integer passed has either 2 or 4 digits.
      */
    // TODO needs tests
    def validateYearInput(year: Int): Boolean = f"$year%02d".length == 2 || year.toString.length == 4

    implicit val quickOptionsRead: scopt.Read[QuickOptions.Value] =
      scopt.Read.reads(QuickOptions withName)

    // Config object for arg/option parser
    // –––
    case class Config(query: Seq[File] = Seq(), quick: QuickOptions = QuickOptions.n, noop: Boolean = false,
                      yearFrom: Int = -1, yearTo: Int = -1)


    // Define the CL interface
    // –––
    val parser = new scopt.OptionParser[Config]("gcli") {
      head("gcli", "0.1")
      help("help") text "prints this usage text"

      // the query terms for our search
      arg[File]("<query terms>...") unbounded() required() action { (x, c) =>
        c.copy(query = c.query :+ x)
      } text "search query"

      // using quick options for last year, month, day, week or hour
      opt[QuickOptions]('q', "quick") action { (x, c) =>
        c.copy(quick = x)
      } text "quick options: last y(ear) | m(onth) | d(ay) | w(eek) | h(hour)"

      // specifying year from for year-spanned search
      opt[Int]("yfrom") action { (x, c) =>
        c.copy(yearFrom = paddedYear(x))
      } validate {
        year => if (validateYearInput(year)) success else failure("yfrom is not a valid year")
      } text "search for content more recent than --yfrom (accepts 2-digit and 4-digit numbers)"

      // specifying year until for year-spanned search
      opt[Int]("yto") action { (x, c) =>
        c.copy(yearTo = paddedYear(x))
      } validate {
        year => if (validateYearInput(year)) success else failure("yto is not a valid year")
      } text "search for content older --yto (accepts 2-digit and 4-digit numbers)"

      // noop param for debugging
      opt[Unit]("noop") action { (_, c) =>
        c.copy(noop = true)
      } text "run program but don't open the browser"
    }


    parser.parse(args, Config()) match {
      case Some(config) =>
        def getQuery = config.query.mkString(" ")
        println(s"\nSearching for: $getQuery")

        // QuickOptions relate to Google's standard filters for last y|m|w|d|h.
        // –––
        val qdr = config.quick match {
          case QuickOptions.n => ""
          case _ => s"&tbs=qdr:${config.quick}"
        }

        // Process yearFrom and/or yearTo
        // ---
        val tbs = if (config.yearFrom > -1 || config.yearTo > -1) {
          val yf = if (config.yearFrom == -1) "" else config.yearFrom.toString
          val yt = if (config.yearTo == -1) "" else config.yearTo.toString
          s"&tbs=cdr:1,cd_min:$yf,cd_max:$yt"
        } else ""
        
        // Assemble final time-filter-query
        // ---
        val timeFilter = if (qdr.length > 0 && tbs.length > 0) {
          println("Ambiguous params for --quick and --yearFrom/--yearTo. Filter for --quick will be ignored.")
          tbs
        }
        else if (qdr.length > 0) { qdr }
        else                     { tbs }

        // Build query string and final command
        // –––
        val q: String = URLEncoder encode(getQuery, "utf-8")
        val cmd: String = s"open https://www.google.com/?#q=$q$timeFilter"

        if (!config.noop) cmd ! else println(s"NOOP: would run $cmd")

      case None =>
        // ignore
    }
  }
}
