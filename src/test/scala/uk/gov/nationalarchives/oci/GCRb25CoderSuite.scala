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

import java.io.IOException

import munit.FunSuite

/**
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
class GCRb25CoderSuite extends FunSuite {

  test("encode GCRb25 negative") {
    intercept[IllegalArgumentException] {
      encodeToGCRb25Str(-1)
    }
    intercept[IllegalArgumentException] {
      encodeToGCRb25Str(-2)
    }
  }

  test("encode GCRb25 short") {
    assertEquals(encodeToGCRb25Str(0), "B")
    assertEquals(encodeToGCRb25Str(1), "C")
    assertEquals(encodeToGCRb25Str(2), "D")
    assertEquals(encodeToGCRb25Str(3), "F")
    assertEquals(encodeToGCRb25Str(4), "G")
    assertEquals(encodeToGCRb25Str(5), "H")
    assertEquals(encodeToGCRb25Str(6), "J")
    assertEquals(encodeToGCRb25Str(7), "K")
    assertEquals(encodeToGCRb25Str(8), "L")
    assertEquals(encodeToGCRb25Str(9), "M")
    assertEquals(encodeToGCRb25Str(10), "N")
    assertEquals(encodeToGCRb25Str(11), "P")
    assertEquals(encodeToGCRb25Str(12), "Q")
    assertEquals(encodeToGCRb25Str(13), "R")
    assertEquals(encodeToGCRb25Str(14), "S")
    assertEquals(encodeToGCRb25Str(15), "T")
    assertEquals(encodeToGCRb25Str(16), "V")
    assertEquals(encodeToGCRb25Str(17), "W")
    assertEquals(encodeToGCRb25Str(18), "X")
    assertEquals(encodeToGCRb25Str(19), "2")
    assertEquals(encodeToGCRb25Str(20), "3")
    assertEquals(encodeToGCRb25Str(21), "4")
    assertEquals(encodeToGCRb25Str(22), "5")
    assertEquals(encodeToGCRb25Str(23), "6")
    assertEquals(encodeToGCRb25Str(24), "7")
    assertEquals(encodeToGCRb25Str(25), "CB")
    assertEquals(encodeToGCRb25Str(26), "CC")
    assertEquals(encodeToGCRb25Str(27), "CD")
    assertEquals(encodeToGCRb25Str(28), "CF")
    assertEquals(encodeToGCRb25Str(29), "CG")
    assertEquals(encodeToGCRb25Str(30), "CH")
    assertEquals(encodeToGCRb25Str(31), "CJ")
  }

  test("encode GCRb25 long") {
    assertEquals(encodeToGCRb25Str(123), "G6")
    assertEquals(encodeToGCRb25Str(255), "NH")
    assertEquals(encodeToGCRb25Str(1234), "C7M")
    assertEquals(encodeToGCRb25Str(4095), "JR3")
    assertEquals(encodeToGCRb25Str(12345), "2X3")
    assertEquals(encodeToGCRb25Str(65535), "GG4N")
    assertEquals(encodeToGCRb25Str(123456), "K5RJ")
    assertEquals(encodeToGCRb25Str(1048575), "DWDXB")
    assertEquals(encodeToGCRb25Str(1234567), "FGBKW")
    assertEquals(encodeToGCRb25Str(16777215), "CW6XRT")
    assertEquals(encodeToGCRb25Str(12345678), "CJTFDF")
    assertEquals(encodeToGCRb25Str(268435455), "CDQG4XH")
    assertEquals(encodeToGCRb25Str(123456789), "QVCH4S")
  }

  test("decode GCRb25 empty-string") {
    intercept[IllegalArgumentException] {
      decodeFromGCRb25Str("")
    }
  }

  test("decode GCRb25 invalid char") {
    intercept[IllegalArgumentException] {
      decodeFromGCRb25Str("BA")
    }
    intercept[IllegalArgumentException] {
      decodeFromGCRb25Str("MON")
    }
  }

  test("decode GCRb25 short") {
    assertEquals(decodeFromGCRb25Str("B"), 0)
    assertEquals(decodeFromGCRb25Str("C"), 1)
    assertEquals(decodeFromGCRb25Str("D"), 2)
    assertEquals(decodeFromGCRb25Str("F"), 3)
    assertEquals(decodeFromGCRb25Str("G"), 4)
    assertEquals(decodeFromGCRb25Str("H"), 5)
    assertEquals(decodeFromGCRb25Str("J"), 6)
    assertEquals(decodeFromGCRb25Str("K"), 7)
    assertEquals(decodeFromGCRb25Str("L"), 8)
    assertEquals(decodeFromGCRb25Str("M"), 9)
    assertEquals(decodeFromGCRb25Str("N"), 10)
    assertEquals(decodeFromGCRb25Str("P"), 11)
    assertEquals(decodeFromGCRb25Str("Q"), 12)
    assertEquals(decodeFromGCRb25Str("R"), 13)
    assertEquals(decodeFromGCRb25Str("S"), 14)
    assertEquals(decodeFromGCRb25Str("T"), 15)
    assertEquals(decodeFromGCRb25Str("V"), 16)
    assertEquals(decodeFromGCRb25Str("W"), 17)
    assertEquals(decodeFromGCRb25Str("X"), 18)
    assertEquals(decodeFromGCRb25Str("2"), 19)
    assertEquals(decodeFromGCRb25Str("3"), 20)
    assertEquals(decodeFromGCRb25Str("4"), 21)
    assertEquals(decodeFromGCRb25Str("5"), 22)
    assertEquals(decodeFromGCRb25Str("6"), 23)
    assertEquals(decodeFromGCRb25Str("7"), 24)
    assertEquals(decodeFromGCRb25Str("CB"), 25)
    assertEquals(decodeFromGCRb25Str("CC"), 26)
    assertEquals(decodeFromGCRb25Str("CD"), 27)
    assertEquals(decodeFromGCRb25Str("CF"), 28)
    assertEquals(decodeFromGCRb25Str("CG"), 29)
    assertEquals(decodeFromGCRb25Str("CH"), 30)
    assertEquals(decodeFromGCRb25Str("CJ"), 31)
  }

  test("decode GCRb25 long") {
    assertEquals(decodeFromGCRb25Str("G6"), 123)
    assertEquals(decodeFromGCRb25Str("NH"), 255)
    assertEquals(decodeFromGCRb25Str("C7M"), 1234)
    assertEquals(decodeFromGCRb25Str("JR3"), 4095)
    assertEquals(decodeFromGCRb25Str("2X3"), 12345)
    assertEquals(decodeFromGCRb25Str("GG4N"), 65535)
    assertEquals(decodeFromGCRb25Str("K5RJ"), 123456)
    assertEquals(decodeFromGCRb25Str("DWDXB"), 1048575)
    assertEquals(decodeFromGCRb25Str("FGBKW"), 1234567)
    assertEquals(decodeFromGCRb25Str("CW6XRT"), 16777215)
    assertEquals(decodeFromGCRb25Str("CJTFDF"), 12345678)
    assertEquals(decodeFromGCRb25Str("CDQG4XH"), 268435455)
    assertEquals(decodeFromGCRb25Str("QVCH4S"), 123456789)
  }

  @throws[IOException]
  private def encodeToGCRb25Str(value: Int) : String = {
    val encoded = BaseCoder.encode(value, 25)
    Alphabet.loadAlphabet(Right(IncludedAlphabet.GCRb25)) match {
      case Right(alphabet) =>
        Alphabet.toString(alphabet, encoded)
      case Left(ioe) => throw ioe
    }
  }

  @throws[IOException]
  private def decodeFromGCRb25Str(str: String) : Int = {
    Alphabet.loadAlphabet(Right(IncludedAlphabet.GCRb25)) match {
      case Right(alphabet) =>
        BaseCoder.decode(str, 25, Alphabet.valueOf(alphabet, _))
      case Left(ioe) => throw ioe
    }
  }
}
