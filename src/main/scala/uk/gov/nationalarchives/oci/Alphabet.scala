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

import java.io.{IOException, InputStreamReader, LineNumberReader}
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, NoSuchFileException, Paths}

import resource.managed
import uk.gov.nationalarchives.oci.IncludedAlphabet.IncludedAlphabet

import scala.annotation.tailrec
import scala.jdk.CollectionConverters._

/**
 * Types and functions for working with Alphabets.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
object Alphabet {

  type Alphabet = Array[Char]

  /**
   * Translates indices into an Alphabet into a String.
   *
   * @param alphabet the alphabet to translate to
   * @param indices the indices into the alphabet
   *
   * @return the string of alphabet characters
   */
  def toString(alphabet: Alphabet, indices: Seq[Int]) : String = indices.map(alphabet(_)).mkString

  /**
   * Translates a single Alphabet character into a numeric value.
   *
   * @param alphabet the alphabet to translate from
   * @param char the character in the alphabet
   *
   * @return the numeric value of the character
   */
  def valueOf(alphabet: Alphabet, char: Char) : Int = alphabet.indexOf(char)

  /**
   * Load an alphabet from disk.
   *
   * @param alphabet either the path to an alphabet file, or the name of an included alphabet file
   *
   * @return either the loaded alphabet, or an IOException
   */
  def loadAlphabet(alphabet: Either[String, IncludedAlphabet]) : Either[IOException, Alphabet] = {
    alphabet match {
      case Right(includedAlphabet) =>
        fromClasspath(s"${includedAlphabet}.alphabet")

      case Left(filePath) =>
        try {
          val path = Paths.get(filePath)
          if (Files.exists(path)) {
            if (Files.isReadable(path)) {
              Right(Files.readAllLines(path, UTF_8).asScala.map(_ (0)).toArray)
            } else {
              Left(new IOException(s"File is not readable: ${filePath}"))
            }
          } else {
            Left(new NoSuchFileException(s"No such file: ${filePath}"))
          }
        } catch {
          case e: IOException =>
            Left(e)
        }
    }
  }

  /**
   * Load an alphabet from the classpath.
   *
   * @param resourceName the filename of an alphabet
   *
   * @return either the loaded alphabet, or an IOException
   */
  def fromClasspath(resourceName: String): Either[IOException, Alphabet] = {
    @tailrec
    def readAllLines[B](lnr: LineNumberReader, transform: String => B, accum: Seq[B] = Seq.empty) : Seq[B] = {
      val line = lnr.readLine()
      if (line == null) {
        return accum
      }
      readAllLines(lnr, transform, accum :+ transform(line))
    }

    managed(getClass.getResourceAsStream(resourceName))
      .flatMap(is => managed(new InputStreamReader(is, UTF_8)))
      .flatMap(reader => managed(new LineNumberReader(reader)))
      .map(readAllLines(_, _(0)))
      .either
      .left.map(_.head.asInstanceOf[IOException])
      .map(_.toArray)
  }
}
