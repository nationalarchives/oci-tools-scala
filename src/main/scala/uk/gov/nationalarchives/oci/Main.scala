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

import java.nio.file.{Files, Paths}

import scopt.{DefaultOParserSetup, OParser, OParserSetup}
import uk.gov.nationalarchives.oci.Alphabet._
import uk.gov.nationalarchives.oci.IncludedAlphabet.IncludedAlphabet

import scala.util.Try



/**
 * Simple command line interface to {@link BaseCoder}.
 *
 * Encodes numbers from Base10 to BaseN
 * and Decodes numbers from BaseN to Base10.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
object Main extends App {

  case class Config(command: String, roundTrip: Boolean, base: Int, input: Either[String, Int], alphabet: Option[Either[String, IncludedAlphabet]])

  val parserBuilder = OParser.builder[Config]
  val parser = {
    import parserBuilder._
    OParser.sequence(
      programName("oci-tools"),
      head("oci-tools", "0.0.1"),
      help('h', "help"),

      cmd("encode")
        .text("Encode a number from Base10 into BaseN")
        .action((_, c) => c.copy(command = "encode"))
        .children(
          opt[Unit]('r', "round-trip")
              .text("Shows the round-trip of the number e.g. encode then decode")
              .action((_, c) => c.copy(roundTrip = true)),
          arg[Int]("<base>")
            .text("The baseN to encode the number in")
            .action((value, c) => c.copy(base = value)),
          arg[Int]("<number>")
            .text("The number to encode")
            .action((value, c) => c.copy(input = Right(value))),
          arg[String](name = "<alphabet>")
            .optional()
            .text("Can be omitted for debugging; will print alphabet offsets instead of characters from the alphabet. " +
              "Either the name of a built-in alphabet i.e. (OCIb25, GCRb25, HEX) or a file with one character per-line " +
              "for the alphabet. Alphabet must have the same number of characters as the <base>.")
            .action(alphabetArgAction)
            .validate(alphabetArgValidator)
        ),

      cmd("decode")
        .text("Decode a number from BaseN into Base10")
        .action((_, c) => c.copy(command = "decode"))
        .children(
          opt[Unit]('r', "round-trip")
            .text("Shows the round-trip of the number e.g. decode then encode")
            .action((_, c) => c.copy(roundTrip = true)),
          arg[Int]("<base>")
            .text("The baseN to decode the number from")
            .action((value, c) => c.copy(base = value)),
          arg[String]("<encoded>")
            .text("The encoded string to decode")
            .action((value, c) => c.copy(input = Left(value))),
          arg[String](name = "<alphabet>")
            .text("Either the name of a built-in alphabet i.e. (OCIb25, GCRb25, HEX) or a file with one character per-line " +
              "for the alphabet. Alphabet must have the same number of characters as the <base>.")
            .action(alphabetArgAction)
            .validate(alphabetArgValidator)
        ),

        checkConfig { c =>
          if (Option(c.command).filterNot(_.isEmpty).nonEmpty) success else failure("A command is required.")
        }
    )
  }

  val parserSetup: OParserSetup = new DefaultOParserSetup {
    override def showUsageOnError = Some(true)
  }

  OParser.parse(parser, args, Config("", false, 10, Right(0), None), parserSetup) match {
    case Some(config) =>

      config.alphabet.map(loadAlphabet) match {
        case Some(Left(ioError)) =>
          System.err.println(s"Unable to load alphabet: ${ioError.getMessage}")
          ioError.printStackTrace()
          System.exit(3);

        case Some(Right(alphabet)) if (alphabet.size != config.base) =>
          System.err.println(s"Base is: ${config.base}, but alphabet is of length: ${alphabet.size}")
          System.exit(4)

        case ma @ _ =>
          val maybeAlphabet = ma.flatMap(_.toOption)

          config.command match {
            case "encode" =>
              println(s"Input: ${config.input.fold(identity, _.toString)}")
              val encoded = BaseCoder.encode(config.input.right.get, config.base)
              maybeAlphabet match {
                case Some(alphabet: Alphabet) =>
                  val encodedStr = Alphabet.toString(alphabet, encoded)
                  println(s"Encoded: '${encodedStr}'")

                  if (config.roundTrip) {
                    val decoded = BaseCoder.decode(encodedStr, config.base, Alphabet.valueOf(alphabet, _))
                    println(s"Round-trip decoded: '${decoded}'")
                  }

                case None =>
                  println(s"Encoded: ${encoded}")

                  if (config.roundTrip) {
                    System.err.println("Cannot round-trip decoded encoded value without a specified alphabet");
                  }
              }

            case "decode" =>
              println(s"Input: ${config.input.fold(identity, _.toString)}")
              val alphabet = maybeAlphabet.get
              val decoded = BaseCoder.decode(config.input.left.get, config.base, Alphabet.valueOf(alphabet, _))
              println(s"Decoded: '${decoded}'")

              if (config.roundTrip) {
                val encoded = BaseCoder.encode(decoded, config.base)
                val encodedStr = Alphabet.toString(alphabet, encoded)
                println(s"Round-trip encoded: '${encodedStr}'")
              }

            case _ =>
              System.err.println(s"Error, no Command specified'")
              System.exit(2)
          }

      }

    case None =>
      // arguments are bad, error message will have been displayed
      System.exit(1)
  }

  private def alphabetArgAction(value: String, c: Config) : Config = {
    c.copy(alphabet =
      Some(Try(IncludedAlphabet.withName(value))
        .map(Right(_))
        .getOrElse(Left(value)))
    )
  }

  private def alphabetArgValidator(value: String) : Either[String, Unit] = {
    Try(IncludedAlphabet.withName(value))
      .map(_ => Right(()))
      .getOrElse(
        if (Files.isReadable(Paths.get(value))) {
          Right(())
        } else {
         Left(s"Unknown alphabet: $value")
        }
      )
  }
}
