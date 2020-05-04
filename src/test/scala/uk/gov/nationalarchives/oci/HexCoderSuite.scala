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
class HexCoderSuite extends FunSuite {

  test("encode hex negative") {
    intercept[IllegalArgumentException] {
      encodeToHexStr(-1)
    }
    intercept[IllegalArgumentException] {
      encodeToHexStr(-2)
    }
  }

  test("encode hex short") {
    assertEquals(encodeToHexStr(0), "0")
    assertEquals(encodeToHexStr(1), "1")
    assertEquals(encodeToHexStr(2), "2")
    assertEquals(encodeToHexStr(3), "3")
    assertEquals(encodeToHexStr(4), "4")
    assertEquals(encodeToHexStr(5), "5")
    assertEquals(encodeToHexStr(6), "6")
    assertEquals(encodeToHexStr(7), "7")
    assertEquals(encodeToHexStr(8), "8")
    assertEquals(encodeToHexStr(9), "9")
    assertEquals(encodeToHexStr(10), "A")
    assertEquals(encodeToHexStr(11), "B")
    assertEquals(encodeToHexStr(12), "C")
    assertEquals(encodeToHexStr(13), "D")
    assertEquals(encodeToHexStr(14), "E")
    assertEquals(encodeToHexStr(15), "F")
    assertEquals(encodeToHexStr(16), "10")
    assertEquals(encodeToHexStr(17), "11")
    assertEquals(encodeToHexStr(18), "12")
    assertEquals(encodeToHexStr(19), "13")
    assertEquals(encodeToHexStr(20), "14")
    assertEquals(encodeToHexStr(21), "15")
    assertEquals(encodeToHexStr(22), "16")
    assertEquals(encodeToHexStr(23), "17")
    assertEquals(encodeToHexStr(24), "18")
    assertEquals(encodeToHexStr(25), "19")
    assertEquals(encodeToHexStr(26), "1A")
    assertEquals(encodeToHexStr(27), "1B")
  }

  test("encode hex long") {
    assertEquals(encodeToHexStr(123), "7B")
    assertEquals(encodeToHexStr(255), "FF")
    assertEquals(encodeToHexStr(1234), "4D2")
    assertEquals(encodeToHexStr(4095), "FFF")
    assertEquals(encodeToHexStr(12345), "3039")
    assertEquals(encodeToHexStr(65535), "FFFF")
    assertEquals(encodeToHexStr(123456), "1E240")
    assertEquals(encodeToHexStr(1048575), "FFFFF")
    assertEquals(encodeToHexStr(1234567), "12D687")
    assertEquals(encodeToHexStr(16777215), "FFFFFF")
    assertEquals(encodeToHexStr(12345678), "BC614E")
    assertEquals(encodeToHexStr(268435455), "FFFFFFF")
    assertEquals(encodeToHexStr(123456789), "75BCD15")
  }

  test("decode hex empty-string") {
    intercept[IllegalArgumentException] {
      decodeFromHexStr("")
    }
  }

  test("decode hex invalid char") {
    intercept[IllegalArgumentException] {
      decodeFromHexStr("ABZA")
    }
    intercept[IllegalArgumentException] {
      decodeFromHexStr("FG")
    }
  }

  test("decode hex short") {
    assertEquals(decodeFromHexStr("0"), 0)
    assertEquals(decodeFromHexStr("1"), 1)
    assertEquals(decodeFromHexStr("2"), 2)
    assertEquals(decodeFromHexStr("3"), 3)
    assertEquals(decodeFromHexStr("4"), 4)
    assertEquals(decodeFromHexStr("5"), 5)
    assertEquals(decodeFromHexStr("6"), 6)
    assertEquals(decodeFromHexStr("7"), 7)
    assertEquals(decodeFromHexStr("8"), 8)
    assertEquals(decodeFromHexStr("9"), 9)
    assertEquals(decodeFromHexStr("A"), 10)
    assertEquals(decodeFromHexStr("B"), 11)
    assertEquals(decodeFromHexStr("C"), 12)
    assertEquals(decodeFromHexStr("D"), 13)
    assertEquals(decodeFromHexStr("E"), 14)
    assertEquals(decodeFromHexStr("F"), 15)
    assertEquals(decodeFromHexStr("10"), 16)
    assertEquals(decodeFromHexStr("11"), 17)
    assertEquals(decodeFromHexStr("12"), 18)
    assertEquals(decodeFromHexStr("13"), 19)
    assertEquals(decodeFromHexStr("14"), 20)
    assertEquals(decodeFromHexStr("15"), 21)
    assertEquals(decodeFromHexStr("16"), 22)
    assertEquals(decodeFromHexStr("17"), 23)
    assertEquals(decodeFromHexStr("18"), 24)
    assertEquals(decodeFromHexStr("19"), 25)
    assertEquals(decodeFromHexStr("1A"), 26)
    assertEquals(decodeFromHexStr("1B"), 27)
  }

  test("decode hex long") {
    assertEquals(decodeFromHexStr("7B"), 123)
    assertEquals(decodeFromHexStr("FF"), 255)
    assertEquals(decodeFromHexStr("4D2"), 1234)
    assertEquals(decodeFromHexStr("FFF"), 4095)
    assertEquals(decodeFromHexStr("3039"), 12345)
    assertEquals(decodeFromHexStr("FFFF"), 65535)
    assertEquals(decodeFromHexStr("1E240"), 123456)
    assertEquals(decodeFromHexStr("FFFFF"), 1048575)
    assertEquals(decodeFromHexStr("12D687"), 1234567)
    assertEquals(decodeFromHexStr("FFFFFF"), 16777215)
    assertEquals(decodeFromHexStr("BC614E"), 12345678)
    assertEquals(decodeFromHexStr("FFFFFFF"), 268435455)
    assertEquals(decodeFromHexStr("75BCD15"), 123456789)
  }

  @throws[IOException]
  private def encodeToHexStr(value: Int) : String = {
    val encoded = BaseCoder.encode(value, 16)
    Alphabet.loadAlphabet(Right(IncludedAlphabet.HEX)) match {
      case Right(alphabet) =>
        Alphabet.toString(alphabet, encoded)
      case Left(ioe) => throw ioe
    }
  }

  @throws[IOException]
  private def decodeFromHexStr(hex: String) : Int = {
    Alphabet.loadAlphabet(Right(IncludedAlphabet.HEX)) match {
      case Right(alphabet) =>
        BaseCoder.decode(hex, 16, Alphabet.valueOf(alphabet, _))
      case Left(ioe) => throw ioe
    }
  }
}
