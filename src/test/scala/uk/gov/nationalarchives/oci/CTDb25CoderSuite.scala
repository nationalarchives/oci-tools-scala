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
import org.apache.commons.codec.digest.DigestUtils

import java.io.IOException
import java.nio.file.{Files, Paths}


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
    assertEquals(encodeToCTDb25Str(0), "1")
    assertEquals(encodeToCTDb25Str(1), "2")
    assertEquals(encodeToCTDb25Str(2), "3")
    assertEquals(encodeToCTDb25Str(3), "4")
    assertEquals(encodeToCTDb25Str(4), "5")
    assertEquals(encodeToCTDb25Str(5), "6")
    assertEquals(encodeToCTDb25Str(6), "7")
    assertEquals(encodeToCTDb25Str(7), "8")
    assertEquals(encodeToCTDb25Str(8), "9")
    assertEquals(encodeToCTDb25Str(9), "C")
    assertEquals(encodeToCTDb25Str(10), "F")
    assertEquals(encodeToCTDb25Str(11), "G")
    assertEquals(encodeToCTDb25Str(12), "H")
    assertEquals(encodeToCTDb25Str(13), "J")
    assertEquals(encodeToCTDb25Str(14), "K")
    assertEquals(encodeToCTDb25Str(15), "L")
    assertEquals(encodeToCTDb25Str(16), "N")
    assertEquals(encodeToCTDb25Str(17), "Q")
    assertEquals(encodeToCTDb25Str(18), "R")
    assertEquals(encodeToCTDb25Str(19), "S")
    assertEquals(encodeToCTDb25Str(20), "T")
    assertEquals(encodeToCTDb25Str(21), "V")
    assertEquals(encodeToCTDb25Str(22), "W")
    assertEquals(encodeToCTDb25Str(23), "X")
    assertEquals(encodeToCTDb25Str(24), "Y")
    assertEquals(encodeToCTDb25Str(25), "21")
    assertEquals(encodeToCTDb25Str(26), "22")
    assertEquals(encodeToCTDb25Str(27), "23")
  }

  test("encode CTDb25 large") {
    assertEquals(encodeToCTDb25Str(123), "5X")
    assertEquals(encodeToCTDb25Str(255), "F6")
    assertEquals(encodeToCTDb25Str(1234), "2YC")
    assertEquals(encodeToCTDb25Str(4095), "7JT")
    assertEquals(encodeToCTDb25Str(12345), "SRT")
    assertEquals(encodeToCTDb25Str(65535), "55VF")
    assertEquals(encodeToCTDb25Str(123456), "8WJ7")
    assertEquals(encodeToCTDb25Str(1048575), "3Q3R1")
    assertEquals(encodeToCTDb25Str(1234567), "4518Q")
    assertEquals(encodeToCTDb25Str(16777215), "2QXRJL")
    assertEquals(encodeToCTDb25Str(12345678), "27L434")
    assertEquals(encodeToCTDb25Str(268435455), "23H5VR6")
    assertEquals(encodeToCTDb25Str(123456789), "HN26VK")
  }

  test("decode CTDb25 empty-string") {
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("")
    }
  }

  test("decode CTDb25 invalid char") {
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("1A2")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("1D")
    }
    intercept[IllegalArgumentException] {
      decodeFromCTDb25Str("1P")
    }
  }

  test("decode CTDb25 small") {
    assertEquals(decodeFromCTDb25Str("1").toInt, 0)
    assertEquals(decodeFromCTDb25Str("2").toInt, 1)
    assertEquals(decodeFromCTDb25Str("3").toInt, 2)
    assertEquals(decodeFromCTDb25Str("4").toInt, 3)
    assertEquals(decodeFromCTDb25Str("5").toInt, 4)
    assertEquals(decodeFromCTDb25Str("6").toInt, 5)
    assertEquals(decodeFromCTDb25Str("7").toInt, 6)
    assertEquals(decodeFromCTDb25Str("8").toInt, 7)
    assertEquals(decodeFromCTDb25Str("9").toInt, 8)
    assertEquals(decodeFromCTDb25Str("C").toInt, 9)
    assertEquals(decodeFromCTDb25Str("F").toInt, 10)
    assertEquals(decodeFromCTDb25Str("G").toInt, 11)
    assertEquals(decodeFromCTDb25Str("H").toInt, 12)
    assertEquals(decodeFromCTDb25Str("J").toInt, 13)
    assertEquals(decodeFromCTDb25Str("K").toInt, 14)
    assertEquals(decodeFromCTDb25Str("L").toInt, 15)
    assertEquals(decodeFromCTDb25Str("N").toInt, 16)
    assertEquals(decodeFromCTDb25Str("Q").toInt, 17)
    assertEquals(decodeFromCTDb25Str("R").toInt, 18)
    assertEquals(decodeFromCTDb25Str("S").toInt, 19)
    assertEquals(decodeFromCTDb25Str("T").toInt, 20)
    assertEquals(decodeFromCTDb25Str("V").toInt, 21)
    assertEquals(decodeFromCTDb25Str("W").toInt, 22)
    assertEquals(decodeFromCTDb25Str("X").toInt, 23)
    assertEquals(decodeFromCTDb25Str("Y").toInt, 24)
    assertEquals(decodeFromCTDb25Str("21").toInt, 25)
    assertEquals(decodeFromCTDb25Str("22").toInt, 26)
    assertEquals(decodeFromCTDb25Str("23").toInt, 27)
  }

  test("decode CTDb25 large") {
    assertEquals(decodeFromCTDb25Str("5X").toInt, 123)
    assertEquals(decodeFromCTDb25Str("F6").toInt, 255)
    assertEquals(decodeFromCTDb25Str("2YC").toInt, 1234)
    assertEquals(decodeFromCTDb25Str("7JT").toInt, 4095)
    assertEquals(decodeFromCTDb25Str("SRT").toInt, 12345)
    assertEquals(decodeFromCTDb25Str("55VF").toInt, 65535)
    assertEquals(decodeFromCTDb25Str("8WJ7").toInt, 123456)
    assertEquals(decodeFromCTDb25Str("3Q3R1").toInt, 1048575)
    assertEquals(decodeFromCTDb25Str("4518Q").toInt, 1234567)
    assertEquals(decodeFromCTDb25Str("2QXRJL").toInt, 16777215)
    assertEquals(decodeFromCTDb25Str("27L434").toInt, 12345678)
    assertEquals(decodeFromCTDb25Str("23H5VR6").toInt, 268435455)
    assertEquals(decodeFromCTDb25Str("HN26VK").toInt, 123456789)
  }

  test("transcode 128-bit CTDb25 string") {
    val sha1Hex = DigestUtils.sha1Hex(Files.readAllBytes(Paths.get(getClass.getResource("CTDb25.alphabet").toURI))).toUpperCase()
    val sha1Int = BigInt(sha1Hex, 16)

    val encoded = encodeToCTDb25Str(sha1Int)
    assertEquals(encoded.length, 34)
    assertEquals(encoded, "TF13YQFT83YF2J19WQNRQ5VV9VHH2SFC3T")  // known good value!

    val decoded = decodeFromCTDb25Str(encoded)
    assertEquals(decoded, sha1Int)
  }

  test("transcode 256-bit CTDb25 string") {
    val sha256Hex = DigestUtils.sha256Hex(Files.readAllBytes(Paths.get(getClass.getResource("HEX.alphabet").toURI))).toUpperCase()
    val sha256Int = BigInt(sha256Hex, 16)

    val encoded = encodeToCTDb25Str(sha256Int)
    assertEquals(encoded.length, 55)
    assertEquals(encoded, "Y3XH277CCCGHXYLHQTYQC557N56LGLC9CQXYXS7CG4CCSY97J44W8CR")  // known good value!

    val decoded = decodeFromCTDb25Str(encoded)
    assertEquals(decoded, sha256Int)
  }

  test("transcode 512-bit CTDb25 string") {
    val sha512Hex = DigestUtils.sha512Hex(Files.readAllBytes(Paths.get(getClass.getResource("HEX.alphabet").toURI))).toUpperCase()
    val sha512Int = BigInt(sha512Hex, 16)

    val encoded = encodeToCTDb25Str(sha512Int)
    assertEquals(encoded.length, 111)
    assertEquals(encoded, "2QVR2V2GL538V8C62WLL4F1V2HXH4FJ3CJTHSXW54F3F7RQJV331996YL4GSV8Q7Y93WYH9C9JNTQYTJ1376V8L29KF5W9GNG9173T1H8JWG6YQ")  // known good value!

    val decoded = decodeFromCTDb25Str(encoded)
    assertEquals(decoded, sha512Int)
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
