/*
 * Copyright (c) 2020 The National Archives
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.gov.nationalarchives.oci

import munit.FunSuite
import java.io.IOException



class CTDb25CoderSuite extends FunSuite {

  test("encode CTDb25 negative") {
    intercept[IllegalArgumentException] {
      encodeToCTDb25Str(-1)
    }
    intercept[IllegalArgumentException] {
      encodeToCTDb25Str(-2)
    }
  }

  test("encode CTDb25 small") {
    assertEquals(encodeToCTDb25Str(0), "2")
    assertEquals(encodeToCTDb25Str(1), "3")
    assertEquals(encodeToCTDb25Str(2), "4")
    assertEquals(encodeToCTDb25Str(3), "5")
    assertEquals(encodeToCTDb25Str(4), "6")
    assertEquals(encodeToCTDb25Str(5), "7")
    assertEquals(encodeToCTDb25Str(6), "8")
    assertEquals(encodeToCTDb25Str(7), "9")
    assertEquals(encodeToCTDb25Str(8), "B")
    assertEquals(encodeToCTDb25Str(9), "C")
    assertEquals(encodeToCTDb25Str(10), "D")
    assertEquals(encodeToCTDb25Str(11), "F")
    assertEquals(encodeToCTDb25Str(12), "G")
    assertEquals(encodeToCTDb25Str(13), "H")
    assertEquals(encodeToCTDb25Str(14), "J")
    assertEquals(encodeToCTDb25Str(15), "K")
    assertEquals(encodeToCTDb25Str(16), "L")
    assertEquals(encodeToCTDb25Str(17), "M")
    assertEquals(encodeToCTDb25Str(18), "N")
    assertEquals(encodeToCTDb25Str(19), "P")
    assertEquals(encodeToCTDb25Str(20), "R")
    assertEquals(encodeToCTDb25Str(21), "S")
    assertEquals(encodeToCTDb25Str(22), "T")
    assertEquals(encodeToCTDb25Str(23), "V")
    assertEquals(encodeToCTDb25Str(24), "W")
  }

  test("encode CTDb25 large") {
    assertEquals(encodeToCTDb25Str(123), "6V")
    assertEquals(encodeToCTDb25Str(255), "D7")
    assertEquals(encodeToCTDb25Str(1234), "3WC")
    assertEquals(encodeToCTDb25Str(4095), "8HR")
    assertEquals(encodeToCTDb25Str(12345), "PNR")
    assertEquals(encodeToCTDb25Str(65535), "66SD")
    assertEquals(encodeToCTDb25Str(123456), "9TH8")
    assertEquals(encodeToCTDb25Str(1048575), "4M4N2")
    assertEquals(encodeToCTDb25Str(1234567), "5629M")
    assertEquals(encodeToCTDb25Str(16777215), "3MVNHK")
    assertEquals(encodeToCTDb25Str(12345678), "38K545")
    assertEquals(encodeToCTDb25Str(268435455), "34G6SN7")
    assertEquals(encodeToCTDb25Str(123456789), "GL37SJ")
  }

  test("decode CTDb25 empty-string") {
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("")
    }
  }

  test("decode CTDb25 invalid char") {
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("21")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("20")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("O")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("A")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("Q")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("E")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("I")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("U")
    }
  }

  test("decode CTDb25 small") {
    assertEquals(decodeFromCTDb25Str("2").toInt, 0)
    assertEquals(decodeFromCTDb25Str("3").toInt, 1)
    assertEquals(decodeFromCTDb25Str("4").toInt, 2)
    assertEquals(decodeFromCTDb25Str("5").toInt, 3)
    assertEquals(decodeFromCTDb25Str("6").toInt, 4)
    assertEquals(decodeFromCTDb25Str("7").toInt, 5)
    assertEquals(decodeFromCTDb25Str("8").toInt, 6)
    assertEquals(decodeFromCTDb25Str("9").toInt, 7)
    assertEquals(decodeFromCTDb25Str("B").toInt, 8)
    assertEquals(decodeFromCTDb25Str("C").toInt, 9)
    assertEquals(decodeFromCTDb25Str("D").toInt, 10)
    assertEquals(decodeFromCTDb25Str("F").toInt, 11)
    assertEquals(decodeFromCTDb25Str("G").toInt, 12)
    assertEquals(decodeFromCTDb25Str("H").toInt, 13)
    assertEquals(decodeFromCTDb25Str("J").toInt, 14)
    assertEquals(decodeFromCTDb25Str("K").toInt, 15)
    assertEquals(decodeFromCTDb25Str("L").toInt, 16)
    assertEquals(decodeFromCTDb25Str("M").toInt, 17)
    assertEquals(decodeFromCTDb25Str("N").toInt, 18)
    assertEquals(decodeFromCTDb25Str("P").toInt, 19)
    assertEquals(decodeFromCTDb25Str("R").toInt, 20)
    assertEquals(decodeFromCTDb25Str("S").toInt, 21)
    assertEquals(decodeFromCTDb25Str("T").toInt, 22)
    assertEquals(decodeFromCTDb25Str("V").toInt, 23)
    assertEquals(decodeFromCTDb25Str("W").toInt, 24)
  }

  test("decode CTDb25 large") {
    assertEquals(decodeFromCTDb25Str("5B").toInt, 83)
    assertEquals(decodeFromCTDb25Str("F6").toInt, 279)
    assertEquals(decodeFromCTDb25Str("2WC").toInt, 609)
    assertEquals(decodeFromCTDb25Str("7JT").toInt, 3497)
    assertEquals(decodeFromCTDb25Str("SRT").toInt, 13647)
    assertEquals(decodeFromCTDb25Str("55VF").toInt, 49336)
    assertEquals(decodeFromCTDb25Str("8WJ7").toInt, 109105)
    assertEquals(decodeFromCTDb25Str("3K3R2").toInt, 626125)
    assertEquals(decodeFromCTDb25Str("4528L").toInt, 828291)
    assertEquals(decodeFromCTDb25Str("2P3RJL").toInt, 7450366)
    assertEquals(decodeFromCTDb25Str("27L434").toInt, 2204402)
    assertEquals(decodeFromCTDb25Str("23H5VR6").toInt, 14905504)
  }

  @throws[IOException]
  private def encodeToCTDb25Str(value: BigInt) : String = {
    val encoded = BaseCoder.encode(value, 25)
    Alphabet.loadAlphabet(Right(IncludedAlphabet.CTDb25)) match {
      case Right(alphabet) =>
        Alphabet.toString(alphabet, encoded)
      case Left(ioe) => throw ioe
    }
  }

  @throws[IOException]
  private def decodeFromCTDb25Str(str: String) : BigInt = {
    Alphabet.loadAlphabet(Right(IncludedAlphabet.CTDb25)) match {
      case Right(alphabet) =>
        BaseCoder.decode(str, 25, Alphabet.valueOf(alphabet, _))
      case Left(ioe) => throw ioe
    }
  }
}
