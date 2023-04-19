# Omega Catalog Identifier tools (Scala)
 
[![Build Status](https://travis-ci.com/nationalarchives/oci-tools-scala.png?branch=master)](https://travis-ci.com/nationalarchives/oci-tools-scala)
[![Build status](https://ci.appveyor.com/api/projects/status/s8ps0eyulq9mqo6k/branch/master?svg=true)](https://ci.appveyor.com/project/AdamRetter/oci-tools-scala/branch/master)
[![Scala 2.13](https://img.shields.io/badge/scala-2.13-red.svg)](http://scala-lang.org)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.gov.nationalarchives.oci/oci-tools-scala_2.13/badge.svg)](https://search.maven.org/search?q=g:uk.gov.nationalarchives.oci)
 
**Note**: There is also an alternative implementation in TypeScript available at:
https://github.com/nationalarchives/oci-tools-ts

This project is split into two parts:
1. a library ([`BaseCoder.scala`](https://github.com/nationalarchives/oci-tools-scala/blob/master/src/main/scala/uk/gov/nationalarchives/oci/BaseCoder.scala))
which can be used in other projects.

2. a command line tool ([`Main.scala`](https://github.com/nationalarchives/oci-tools-scala/blob/master/src/main/scala/uk/gov/nationalarchives/oci/Main.scala))
which is useful in itself and also serves as an example of using the `BaseCoder.scala` library.


## Examples of Command Line tool use
Encoding to Base16 (e.g. hexadecimal):
```bash
❯ target/scala-2.13/oci-tools-scala-assembly-0.3.0.jar encode --round-trip 16 123456 HEX
Input: 123456
Encoded: '1E240'
Round-trip decoded: '123456'
```

Encoding to GCR b25 (e.g. as used by DRI for digital records):
```bash
❯ target/scala-2.13/oci-tools-scala-assembly-0.3.0.jar encode --round-trip 25 123456 GCRb25
Input: 123456
Encoded: 'K5RJ'
Round-trip decoded: '123456'
```

Encoding to OCI b25 (e.g. as used by Project Omega):
```bash
❯ target/scala-2.13/oci-tools-scala-assembly-0.3.0.jar encode --round-trip 25 123456 OCIb25
Input: 123456
Encoded: '8WJ7'
Round-trip decoded: '123456'
```

**EXPERIMENTAL:** There is also support for experimental URI encoding using a Base78 and Base68 alphabet, built-in as: `OCIb78` and `OCIb68`.

### Compiling the Command Line tool
To build from source you will need the following pre-requisites:

1. Git Command Line tools.
2. Java 8+
3. SBT (Simple Build Tool) 1.1.2+

If you wish to create a standalone application (also known as an Uber Jar, Assembly, etc.)
you can run `sbt assembly`, which will generate `target/scala-2.13/oci-tools-scala-assembly-0.3.0.jar`.

### Running the Command Line tool
Given the standalone application, you can execute it by running either:

1. `java -jar oci-tools-scala-assembly-0.3.0.jar`

2. or, even by just executing the `oci-tools-scala-assembly-0.3.0.jar` file directly, as we
compile an executable header into the Jar file. e.g. (on Linux/Mac): `./oci-tools-scala-assembly-0.3.0.jar`.


## Publishing a Release to Maven Central
1. Run `sbt clean release`
2. Answer the questions
3. Login to https://oss.sonatype.org/ then Close, and Release the Staging Repository
