package gcli

import org.scalatest._

class UtilitiesSpec extends FlatSpec with Matchers {

  // paddedYear()
  // –––
  " A padded year" should "return a year of this century if the 2 digits are less than the current year" in {
    paddedYear(10) should be (2010)
  }

  it should "return a year of the last century if the 2 digits are greater than the current year" in {
    paddedYear(90) should be (1990)
  }

  it should "0-pad an integer lesser than 10" in {
    paddedYear(5) should be (2005)
  }

  it should "echo back an integer with more than two digits" in {
    paddedYear(123) should be (123)
    paddedYear(1234) should be (1234)
  }


  // validateYearInput()
  // –––
  "A year passed on the CL" should "validate if the integer contains 4 digits" in {
    validateYearInput(2015) should be (true)
  }

  it should "validate if the integer contains 2 digits" in {
    validateYearInput(13) should be (true)
  }

  it should "validate if the integer contains 1 digits" in {
    validateYearInput(9) should be (true)
  }

  it should "return false if integer has not 1, 2 or 4 digits" in {
    validateYearInput(123) should be (false)
    validateYearInput(12345) should be (false)
  }
}
