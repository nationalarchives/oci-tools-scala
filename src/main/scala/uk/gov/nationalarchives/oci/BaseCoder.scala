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

package uk.gov.nationalarchives.oci;

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.collection.immutable.Seq;

/**
 * Encodes numbers from Base10 to BaseN
 * and Decodes numbers from BaseN to Base10.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
object BaseCoder {

  /**
   * Encode a Base10 integer value into BaseN.
   *
   * @param value the Base10 integer to encode
   * @param baseN the base into which to encode the integer
   *
   * @return A sequence of offsets into the baseN alphabet
   *         for the encoded integer
   *
   * @throws IllegalArgumentException if {@code value} is less than zero.
   */
  @throws[IllegalArgumentException]
  def encode(value: Int, baseN: Int): Seq[Int] = {

    @tailrec
    def encode(v: Int, accum: List[Int]): List[Int] = {
      if(v == 0 && accum.nonEmpty) {
        accum
      } else if(v <= 1) {
        (v :: accum)
      } else {
        val div = v / baseN
        val mod = v % baseN
        encode(div, mod :: accum)
      }
    }

    if (value < 0) {
      throw new IllegalArgumentException("Negative values cannot be encoded")
    }

    encode(value, List.empty[Int])
  }

  /**
   * Decode a BaseN string into a Base10 integer.
   *
   * @param str the BaseN encoded string
   * @param baseN the base into which to encode the integer
   * @param characterToNumericValue a function that converts an encoded BaseN char to a numeric value
   *                                the function should return -1 if there is no possible conversion
   *
   * @throws IllegalArgumentException if {@code value} is less than zero.
   */
  @throws[IllegalArgumentException]
  def decode(str: String, baseN: Int, characterToNumericValue: Char => Int): Int = {
    if (str.isEmpty) {
      throw new IllegalArgumentException("Cannot decode empty-string")
    }

    val numericValues = str.map { character =>
      val numericValue = characterToNumericValue(character)
      if (numericValue < 0) {
        throw new IllegalArgumentException(s"Character '$character' cannot be resolved to a numeric value")
      }
      numericValue
    }

    val vs = for(i <- 0 until numericValues.length) yield {
      val exp = (numericValues.length - i) - 1
      numericValues(i) * Math.pow(baseN, exp).toInt
    }

    vs.reduceLeft(_ + _)
  }
}
