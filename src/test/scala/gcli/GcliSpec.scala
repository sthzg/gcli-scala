package gcli

import org.scalatest._

class GcliSpec extends FlatSpec with Matchers {

  // paddedYear()
  // –––
  " A padded year" should "return a year of this century if the 2 digits are less than the current year" in {
    GCli.paddedYear(10) should be (2010)
  }

  it should "return a year of the last century if the 2 digits are greater than the current year" in {
    GCli.paddedYear(90) should be (1990)
  }

  it should "0-pad an integer lesser than 10" in {
    GCli.paddedYear(5) should be (2005)
  }

  it should "echo back an integer with more than two digits" in {
    GCli.paddedYear(123) should be (123)
    GCli.paddedYear(1234) should be (1234)
  }


  // validateYearInput()
  // –––
  "A year passed on the CL" should "validate if the integer contains 4 digits" in {
    GCli.validateYearInput(2015) should be (true)
  }

  it should "validate if the integer contains 2 digits" in {
    GCli.validateYearInput(13) should be (true)
  }

  it should "validate if the integer contains 1 digits" in {
    GCli.validateYearInput(9) should be (true)
  }

  it should "return false if integer has not 1, 2 or 4 digits" in {
    GCli.validateYearInput(123) should be (false)
    GCli.validateYearInput(12345) should be (false)
  }
}
