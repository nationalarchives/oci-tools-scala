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
class OCIb25CoderSuite extends FunSuite {

  test("encode OCIb25 negative") {
    intercept[IllegalArgumentException] {
      encodeToOCIb25Str(-1)
    }
    intercept[IllegalArgumentException] {
      encodeToOCIb25Str(-2)
    }
  }

  test("encode OCIb25 small") {
    assertEquals(encodeToOCIb25Str(0), "1")
    assertEquals(encodeToOCIb25Str(1), "2")
    assertEquals(encodeToOCIb25Str(2), "3")
    assertEquals(encodeToOCIb25Str(3), "4")
    assertEquals(encodeToOCIb25Str(4), "5")
    assertEquals(encodeToOCIb25Str(5), "6")
    assertEquals(encodeToOCIb25Str(6), "7")
    assertEquals(encodeToOCIb25Str(7), "8")
    assertEquals(encodeToOCIb25Str(8), "9")
    assertEquals(encodeToOCIb25Str(9), "C")
    assertEquals(encodeToOCIb25Str(10), "F")
    assertEquals(encodeToOCIb25Str(11), "G")
    assertEquals(encodeToOCIb25Str(12), "H")
    assertEquals(encodeToOCIb25Str(13), "J")
    assertEquals(encodeToOCIb25Str(14), "K")
    assertEquals(encodeToOCIb25Str(15), "L")
    assertEquals(encodeToOCIb25Str(16), "N")
    assertEquals(encodeToOCIb25Str(17), "Q")
    assertEquals(encodeToOCIb25Str(18), "R")
    assertEquals(encodeToOCIb25Str(19), "S")
    assertEquals(encodeToOCIb25Str(20), "T")
    assertEquals(encodeToOCIb25Str(21), "V")
    assertEquals(encodeToOCIb25Str(22), "W")
    assertEquals(encodeToOCIb25Str(23), "X")
    assertEquals(encodeToOCIb25Str(24), "Y")
    assertEquals(encodeToOCIb25Str(25), "21")
    assertEquals(encodeToOCIb25Str(26), "22")
    assertEquals(encodeToOCIb25Str(27), "23")
  }

  test("encode OCIb25 large") {
    assertEquals(encodeToOCIb25Str(123), "5X")
    assertEquals(encodeToOCIb25Str(255), "F6")
    assertEquals(encodeToOCIb25Str(1234), "2YC")
    assertEquals(encodeToOCIb25Str(4095), "7JT")
    assertEquals(encodeToOCIb25Str(12345), "SRT")
    assertEquals(encodeToOCIb25Str(65535), "55VF")
    assertEquals(encodeToOCIb25Str(123456), "8WJ7")
    assertEquals(encodeToOCIb25Str(1048575), "3Q3R1")
    assertEquals(encodeToOCIb25Str(1234567), "4518Q")
    assertEquals(encodeToOCIb25Str(16777215), "2QXRJL")
    assertEquals(encodeToOCIb25Str(12345678), "27L434")
    assertEquals(encodeToOCIb25Str(268435455), "23H5VR6")
    assertEquals(encodeToOCIb25Str(123456789), "HN26VK")
  }

  test("decode OCIb25 empty-string") {
    intercept[IllegalArgumentException] {
      decodeFromOCIb25Str("")
    }
  }

  test("decode OCIb25 invalid char") {
    intercept[IllegalArgumentException] {
      decodeFromOCIb25Str("1A2")
    }
    intercept[IllegalArgumentException] {
      decodeFromOCIb25Str("1D")
    }
    intercept[IllegalArgumentException] {
      decodeFromOCIb25Str("1P")
    }
  }

  test("decode OCIb25 small") {
    assertEquals(decodeFromOCIb25Str("1"), 0)
    assertEquals(decodeFromOCIb25Str("2"), 1)
    assertEquals(decodeFromOCIb25Str("3"), 2)
    assertEquals(decodeFromOCIb25Str("4"), 3)
    assertEquals(decodeFromOCIb25Str("5"), 4)
    assertEquals(decodeFromOCIb25Str("6"), 5)
    assertEquals(decodeFromOCIb25Str("7"), 6)
    assertEquals(decodeFromOCIb25Str("8"), 7)
    assertEquals(decodeFromOCIb25Str("9"), 8)
    assertEquals(decodeFromOCIb25Str("C"), 9)
    assertEquals(decodeFromOCIb25Str("F"), 10)
    assertEquals(decodeFromOCIb25Str("G"), 11)
    assertEquals(decodeFromOCIb25Str("H"), 12)
    assertEquals(decodeFromOCIb25Str("J"), 13)
    assertEquals(decodeFromOCIb25Str("K"), 14)
    assertEquals(decodeFromOCIb25Str("L"), 15)
    assertEquals(decodeFromOCIb25Str("N"), 16)
    assertEquals(decodeFromOCIb25Str("Q"), 17)
    assertEquals(decodeFromOCIb25Str("R"), 18)
    assertEquals(decodeFromOCIb25Str("S"), 19)
    assertEquals(decodeFromOCIb25Str("T"), 20)
    assertEquals(decodeFromOCIb25Str("V"), 21)
    assertEquals(decodeFromOCIb25Str("W"), 22)
    assertEquals(decodeFromOCIb25Str("X"), 23)
    assertEquals(decodeFromOCIb25Str("Y"), 24)
    assertEquals(decodeFromOCIb25Str("21"), 25)
    assertEquals(decodeFromOCIb25Str("22"), 26)
    assertEquals(decodeFromOCIb25Str("23"), 27)
  }

  test("decode OCIb25 large") {
    assertEquals(decodeFromOCIb25Str("5X"), 123)
    assertEquals(decodeFromOCIb25Str("F6"), 255)
    assertEquals(decodeFromOCIb25Str("2YC"), 1234)
    assertEquals(decodeFromOCIb25Str("7JT"), 4095)
    assertEquals(decodeFromOCIb25Str("SRT"), 12345)
    assertEquals(decodeFromOCIb25Str("55VF"), 65535)
    assertEquals(decodeFromOCIb25Str("8WJ7"), 123456)
    assertEquals(decodeFromOCIb25Str("3Q3R1"), 1048575)
    assertEquals(decodeFromOCIb25Str("4518Q"), 1234567)
    assertEquals(decodeFromOCIb25Str("2QXRJL"), 16777215)
    assertEquals(decodeFromOCIb25Str("27L434"), 12345678)
    assertEquals(decodeFromOCIb25Str("23H5VR6"), 268435455)
    assertEquals(decodeFromOCIb25Str("HN26VK"), 123456789)
  }

  @throws[IOException]
  private def encodeToOCIb25Str(value: Int) : String = {
    val encoded = BaseCoder.encode(value, 25)
    Alphabet.loadAlphabet(Right(IncludedAlphabet.OCIb25)) match {
      case Right(alphabet) =>
        Alphabet.toString(alphabet, encoded)
      case Left(ioe) => throw ioe
    }
  }

  @throws[IOException]
  private def decodeFromOCIb25Str(str: String) : Int = {
    Alphabet.loadAlphabet(Right(IncludedAlphabet.OCIb25)) match {
      case Right(alphabet) =>
        BaseCoder.decode(str, 25, Alphabet.valueOf(alphabet, _))
      case Left(ioe) => throw ioe
    }
  }
}
