package gcli

import java.io._
import java.nio.file.{Paths, Files}

import scala.io.StdIn

case class Config private(startCommand: String = "open",
                          tld: String = "com")

object Config{

  import play.api.libs.json._

  implicit val configFormats = Json.format[gcli.Config]

  /**
    * @param config  config instance to be serialized
    * @return instance of config serialized to Json
    */
  def writeConfig(config: Config): JsValue = {
    Json.toJson(config)
  }

  /**
    * @param jsonConfig content of config.json
    * @return deserialized instance of config object
    */
  def readConfig(jsonConfig: JsValue) = {
    jsonConfig.as[Config]
  }

  /**
    * @param startCommand the command that will be invoked in the shell to open up the browser
    * @param tld the top-level-domain to search for results
    * @return an instance of the populated [[Config]]
    */
  def get(startCommand: String = "open", tld: String = "com") = {
    Config(startCommand, tld)
  }

  /**
    * Saves configuration serialized to Json in configPath.
    *
    * @param config instance of Config object that will be persisted
    * @param configPath absolute file path that config file will be saved at
    */
  def save(config: Config, configPath: String): Unit = {
    val file = new File(configPath)
    file.getParentFile.mkdirs()
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))
    try writer.write(Config.writeConfig(config).toString()) finally writer.close()
  }

  /**
    * Loads and deserializes configuration file from disk.
    *
    * If the configuration file doesn't exist, it prompts the user for approval to create the file on their disk.
    *
    * @param location absolut path to look for the .gcli config directory
    * @return instance of `Config` object
    */
  def load(location:String = System.getProperty("user.home")): Config = {
    val configPath = s"$location/.gcli/gcli.config"

    if (!Files.exists(Paths.get(configPath))) {
      val yesno = promptForYesNo(s"$configPath does not exist. Do you want to create it? [y|N]")
      if (yesno) save(get(), configPath)
      // TODO handle no
    }

    val source = scala.io.Source.fromFile(configPath)
    val configJson = try source.mkString finally source.close()

    Config.readConfig(Json.parse(configJson))
  }

  /**
    * Displays a yesno question on stdin. Any input except y or Y returns false.
    *
    * @param question the question to be displayed at stdin
    * @return true for an input of y or Y, otherwise false
    */
  def promptForYesNo(question: String): Boolean = {
    val yesno = StdIn.readLine(s"$question ")
    yesno.toLowerCase match {
      case "y" => true
      case _   => false
    }
  }

}
