import java.time.Year

import gcli.QuickOptions.QuickOptions

import scala.collection.mutable.ArrayBuffer


package object gcli {

  /**
    * Will return the year of the last century if the two digits given are greater than the current year, i.e.
    * for an input of 71 it will return 1971. 1-digit integers are treated as 0-padded (3 -> 03 -> 2003).
    *
    * @param year the year to be padded
    * @return returns a 2-digit value as a 4-digit integer
    */
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
  def validateYearInput(year: Int): Boolean = f"$year%02d".length == 2 || year.toString.length == 4

  // CliConfig object for arg/option parser
  // –––
  case class CliConfig(query: Seq[String] = Seq(),
                       quick: QuickOptions = QuickOptions.n,
                       yearFrom: Int = -1,
                       yearTo: Int = -1,
                       site: String = "",
                       limitToStackoverflow: Boolean = false,
                       images: Boolean = false,
                       news: Boolean = false,
                       videos: Boolean = false,
                       noconf: Boolean = false,
                       noop: Boolean = false)


  def getCliParser = {
    implicit val quickOptionsRead: scopt.Read[QuickOptions.Value] =
      scopt.Read.reads(QuickOptions withName)

    // Define the CL interface
    // –––
    val parser = new scopt.OptionParser[CliConfig]("gcli") {
      head("gcli", "0.0.1")
      help("help") text "prints this usage text"

      // the query terms for our search
      arg[String]("<query terms>...") unbounded() required() action { (x, c) =>
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

      // specifying a domain to limit search results
      opt[String]("site") action { (x, c) =>
        c.copy(site = x)
      } text "specify a domain that this search should be limited to, e.g. docs.djangoproject.com"

      // limit search results to stackoverflow.com
      opt[Unit]("so") action { (_, c) =>
        c.copy(limitToStackoverflow = true)
      } text "limit search results to stackoverflow.com"

      // use Google's image search
      opt[Unit]("images") action { (_, c) =>
        c.copy(images = true)
      } text "use Google's image search"

      // use Google's news search
      opt[Unit]("news") action { (_, c) =>
        c.copy(news = true)
      } text "use Google's news search"

      // use Google's video search
      opt[Unit]("videos") action { (_, c) =>
        c.copy(videos = true)
      } text "use Google's video search"

      // skip config-check and use defaults
      opt[Unit]("noconf") action { (_, c) =>
        c.copy(noconf = true)
      } text "don't look for configuration files on disk and don't prompt to create them"

      // noop param for debugging
      opt[Unit]("noop") action { (_, c) =>
        c.copy(noop = true)
      } text "run program but don't open the browser"

      // Combined validation for validated config object
      // –––
      checkConfig { c =>
        var errors = ArrayBuffer[String]()

        if (List(c.images, c.videos, c.news).count(x => x) > 1)
          errors += s"Only one of the flags --videos, --images and --news may be set"

        if (errors.isEmpty) success else failure(errors.mkString("\n"))
      }
    }
    parser
  }
}
