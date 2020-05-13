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
import java.nio.file.{Files, Paths}

import munit.FunSuite
import org.apache.commons.codec.digest.DigestUtils

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

  test("encode GCRb25 small") {
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

  test("encode GCRb25 large") {
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

  test("decode GCRb25 small") {
    assertEquals(decodeFromGCRb25Str("B").toInt, 0)
    assertEquals(decodeFromGCRb25Str("C").toInt, 1)
    assertEquals(decodeFromGCRb25Str("D").toInt, 2)
    assertEquals(decodeFromGCRb25Str("F").toInt, 3)
    assertEquals(decodeFromGCRb25Str("G").toInt, 4)
    assertEquals(decodeFromGCRb25Str("H").toInt, 5)
    assertEquals(decodeFromGCRb25Str("J").toInt, 6)
    assertEquals(decodeFromGCRb25Str("K").toInt, 7)
    assertEquals(decodeFromGCRb25Str("L").toInt, 8)
    assertEquals(decodeFromGCRb25Str("M").toInt, 9)
    assertEquals(decodeFromGCRb25Str("N").toInt, 10)
    assertEquals(decodeFromGCRb25Str("P").toInt, 11)
    assertEquals(decodeFromGCRb25Str("Q").toInt, 12)
    assertEquals(decodeFromGCRb25Str("R").toInt, 13)
    assertEquals(decodeFromGCRb25Str("S").toInt, 14)
    assertEquals(decodeFromGCRb25Str("T").toInt, 15)
    assertEquals(decodeFromGCRb25Str("V").toInt, 16)
    assertEquals(decodeFromGCRb25Str("W").toInt, 17)
    assertEquals(decodeFromGCRb25Str("X").toInt, 18)
    assertEquals(decodeFromGCRb25Str("2").toInt, 19)
    assertEquals(decodeFromGCRb25Str("3").toInt, 20)
    assertEquals(decodeFromGCRb25Str("4").toInt, 21)
    assertEquals(decodeFromGCRb25Str("5").toInt, 22)
    assertEquals(decodeFromGCRb25Str("6").toInt, 23)
    assertEquals(decodeFromGCRb25Str("7").toInt, 24)
    assertEquals(decodeFromGCRb25Str("CB").toInt, 25)
    assertEquals(decodeFromGCRb25Str("CC").toInt, 26)
    assertEquals(decodeFromGCRb25Str("CD").toInt, 27)
    assertEquals(decodeFromGCRb25Str("CF").toInt, 28)
    assertEquals(decodeFromGCRb25Str("CG").toInt, 29)
    assertEquals(decodeFromGCRb25Str("CH").toInt, 30)
    assertEquals(decodeFromGCRb25Str("CJ").toInt, 31)
  }

  test("decode GCRb25 large") {
    assertEquals(decodeFromGCRb25Str("G6").toInt, 123)
    assertEquals(decodeFromGCRb25Str("NH").toInt, 255)
    assertEquals(decodeFromGCRb25Str("C7M").toInt, 1234)
    assertEquals(decodeFromGCRb25Str("JR3").toInt, 4095)
    assertEquals(decodeFromGCRb25Str("2X3").toInt, 12345)
    assertEquals(decodeFromGCRb25Str("GG4N").toInt, 65535)
    assertEquals(decodeFromGCRb25Str("K5RJ").toInt, 123456)
    assertEquals(decodeFromGCRb25Str("DWDXB").toInt, 1048575)
    assertEquals(decodeFromGCRb25Str("FGBKW").toInt, 1234567)
    assertEquals(decodeFromGCRb25Str("CW6XRT").toInt, 16777215)
    assertEquals(decodeFromGCRb25Str("CJTFDF").toInt, 12345678)
    assertEquals(decodeFromGCRb25Str("CDQG4XH").toInt, 268435455)
    assertEquals(decodeFromGCRb25Str("QVCH4S").toInt, 123456789)
  }

  test("transcode 128-bit GCRb25 string") {
    val sha1Hex = DigestUtils.sha1Hex(Files.readAllBytes(Paths.get(getClass.getResource("OCIb25.alphabet").toURI))).toUpperCase()
    val sha1Int = BigInt(sha1Hex, 16)

    val encoded = encodeToGCRb25Str(sha1Int)
    assertEquals(encoded.length, 34)
    assertEquals(encoded, "3NBD7WN3KD7NCRBL5WVXWG44L4QQC2NMD3")  // known good value!

    val decoded = decodeFromGCRb25Str(encoded)
    assertEquals(decoded, sha1Int)
  }

  test("transcode 256-bit GCRb25 string") {
    val sha256Hex = DigestUtils.sha256Hex(Files.readAllBytes(Paths.get(getClass.getResource("HEX.alphabet").toURI))).toUpperCase()
    val sha256Int = BigInt(sha256Hex, 16)

    val encoded = encodeToGCRb25Str(sha256Int)
    assertEquals(encoded.length, 55)
    assertEquals(encoded, "7D6QCJJMMMPQ67TQW37WMGGJVGHTPTMLMW6762JMPFMM27LJRFF5KMX")  // known good value!

    val decoded = decodeFromGCRb25Str(encoded)
    assertEquals(decoded, sha256Int)
  }

  test("transcode 512-bit GCRb25 string") {
    val sha512Hex = DigestUtils.sha512Hex(Files.readAllBytes(Paths.get(getClass.getResource("HEX.alphabet").toURI))).toUpperCase()
    val sha512Int = BigInt(sha512Hex, 16)

    val encoded = encodeToGCRb25Str(sha512Int)
    assertEquals(encoded.length, 111)
    assertEquals(encoded, "CW4XC4CPTGDK4KMHC5TTFNB4CQ6QFNRDMR3Q265GFNDNJXWR4DDBLLH7TFP24KWJ7LD57QLMLRV3W73RBDJH4KTCLSNG5LPVPLBJD3BQKR5PH7W")  // known good value!

    val decoded = decodeFromGCRb25Str(encoded)
    assertEquals(decoded, sha512Int)
  }

  @throws[IOException]
  private def encodeToGCRb25Str(value: BigInt) : String = {
    val encoded = BaseCoder.encode(value, 25)
    Alphabet.loadAlphabet(Right(IncludedAlphabet.GCRb25)) match {
      case Right(alphabet) =>
        Alphabet.toString(alphabet, encoded)
      case Left(ioe) => throw ioe
    }
  }

  @throws[IOException]
  private def decodeFromGCRb25Str(str: String) : BigInt = {
    Alphabet.loadAlphabet(Right(IncludedAlphabet.GCRb25)) match {
      case Right(alphabet) =>
        BaseCoder.decode(str, 25, Alphabet.valueOf(alphabet, _))
      case Left(ioe) => throw ioe
    }
  }
}
