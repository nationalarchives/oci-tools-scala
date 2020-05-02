# Omega Catalog Identifier tools (Scala)
 
[![Build Status](https://travis-ci.com/nationalarchives/oci-tools-scala.png?branch=master)](https://travis-ci.com/nationalarchives/oci-tools-scala)
[![Scala 2.13](https://img.shields.io/badge/scala-2.13-red.svg)](http://scala-lang.org)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
 
**Note**: There is also an alternative implementation in TypeScript available at:
https://github.com/nationalarchives/oci-tools-ts.git

## Examples of Command Line tool use
Encoding to Base16 (e.g. hexadecimal):
```bash
❯ target/scala-2.13/oci-tools-scala-assembly-0.0.1.jar encode --round-trip 16 123456 HEX
Input: 123456
Encoded: '1E240'
Round-trip decoded: '123456'
```

Encoding to GCR b25 (e.g. as used by DRI for digital records):
```bash
❯ target/scala-2.13/oci-tools-scala-assembly-0.0.1.jar encode --round-trip 25 123456 GCRb25
Input: 123456
Encoded: 'K5RJ'
Round-trip decoded: '123456'
```

Encoding to OCI b25 (e.g. as used by Project Omega):
```bash
❯ target/scala-2.13/oci-tools-scala-assembly-0.0.1.jar encode --round-trip 25 123456 OCIb25
Input: 123456
Encoded: '8WJ7'
Round-trip decoded: '123456'
```

### Compiling the Command Line tool
To build from source you will need the following pre-requisites:

1. Git Command Line tools.
2. Java 8+
3. SBT (Simple Build Tool) 1.1.2+

If you wish to create a standalone application (also known as an Uber Jar, Assembly, etc.)
you can run `sbt assembly`, which will generate `target/scala-2.13/oci-tools-scala-assembly-0.0.1.jar`.

### Running the Command Line tool
Given the standalone application, you can execute it by running either:

1. `java -jar oci-tools-scala-assembly-0.0.1.jar`

2. or, even by just executing the `oci-tools-scala-assembly-0.0.1.jar` file directly, as we
compile an executable header into the Jar file. e.g. (on Linux/Mac): `./exist-xqts-runner-assembly-1.0.0.jar`.