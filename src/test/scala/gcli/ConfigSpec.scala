package gcli

import org.scalatest._
import play.api.libs.json._

class ConfigSpec extends FlatSpec with Matchers {


  "Config" should "be converted to json correctly" in {
    val config = Config.get("open", "com")
    val configJson = Config.writeConfig(config)
    (configJson \ "startCommand").get should be (JsString("open"))
    (configJson \ "tld").get should be (JsString("com"))
  }
}
