
# kotlin-expression-builder

[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/Burnett01/paypal-basket/master/LICENSE) 
[![Build Status](https://travis-ci.org/Burnett01/kotlin-expression-builder.svg?branch=master)](https://travis-ci.org/Burnett01/kotlin-expression-builder) [![](https://jitpack.io/v/burnett01/kotlin-expression-builder.svg)](https://jitpack.io/#burnett01/kotlin-expression-builder)


<img src="http://i.imgur.com/I0DES9A.png" align="right"/>

A decent expression class and builder (DSL), written in Kotlin.

This package will assist you in writing and managing your regular expressions (Regex).<br>

I'll be using this package in my upcoming [HAFAS-parser](https://github.com/Burnett01/hafas-parser) project, in order to simplify very complex regular expressions. Read more about it [on my blog](https://burnett01.blogspot.de/2017/06/developers-should-ease-code-readability.html)

Features:
  * Easy Regex management
  * Usable as pure class (Expression)
  * Usable as builder (ExpressionBuilder)
  * Unit Tests

---

# Table of contents
* [API Reference](#api-reference)
  * [Expression](#expression)
  * [ExpessionBuilder](#expressionbuilder)
* [Usage (ExpressionBuilder)](#usage-expressionbuilder)
* [Example](../master/contrib)
* [Setup / Install](#setup-install)
* [Build](#build)
  * [Gradle](#gradle)
* [Unit-Tests](#unit-tests)
  * [Gradle](#gradle-1)
* [Contributing](#contributing)
* [License](#license)

---

## API Reference

### Expression:

```kotlin
class Expression( op: Set<RegexOption> )

fun markStart() = // '^'
fun markEnd() =   // '$'
fun markOr() =    // '|'

/* Quantity (Q)
 * Q.ZERO_OR_ONE  = '?'
 * Q.ZERO_OR_MORE = '*'
 * Q.ONE_OR_MORE  = '+' 
 */
fun quantity(quant: Q = Q.ZERO_OR_ONE)

/* Range
 * Delimiter '-' = [min-max] (default)
 * Delimiter ',' = {min,max}
 *
 * Example: range(0,9)     = [0-9]
 * Example: range(0,9,',') = {0,9}
 * Example: range('A','z') = A-z
 */
fun range(min: Any, max: Any, delim: Char = '-')

fun exact(times: Int) =      // {times,times}
fun setChar(char: Char) =    // char
fun setString(str: String) = // str
fun setLiteral(lit: Char) =  // \lit
fun setDigit() =             // \d
fun setWord() =              // \w

fun startMatch() =           // [
fun endMatch() =             // ]

/* Start Capture Group
 * 0 = capture -> (
 * 1 = no capture -> (?:
 */
fun startGroup(type: Int)   
fun endGroup() = // )

fun debug()
fun compile(): Regex
```

### ExpressionBuilder:

> For documentation check above (Expression class) or demos/examples.

```kotlin

class ExpressionBuilder { }

fun start()
fun end()
fun or()
fun quantity( quant: Q )
fun exact( times: Int )
fun range( min: Any, max: Any, delim: Char = '-' )
fun literal( lit: Char )
fun decimal( aggr: () -> Unit = {} )
fun string( str: String )
fun char( char: Char )
fun digit( aggr: () -> Unit = {} )
fun word( aggr: () -> Unit = {} )
fun whitespace()
fun match( aggr: () -> Unit = {} )
fun nocapture( aggr: () -> Unit )
fun capture( aggr: () -> Unit )

fun expression( 
    entity: ExpressionBuilder.() -> Unit,
    options: Set<RegexOption> = emptySet()
): Expression?

// In addition to the functions above, 
// you can access every function of Expression class.
```

---

## Usage (ExpressionBuilder):

**Demo 1:**

```kotlin
import com.github.burnett01.expression.*

class main {

    val date: String = "20.05.2017"
    val origExpr = Regex("(\\d{2}.\\d{2}.\\d{4})")

    val myExpr: Expression? = expression {
        capture {
            digit { exact(2) }
            literal('.')
            digit { exact(2) }
            literal('.')
            digit { exact(4) }
        }
    }

    println(origExpr.find(date)?.value)
    println(myExpr!!.compile().find(date)?.value)

    myExpr.debug()

}
```

**Demo 2:**

```kotlin
import com.github.burnett01.expression.*

class main {

    val txt: String = "Hello catch me!%"
  
    val myExpr: Expression? = expression({
        capture {
            string("Hello catch me!")
            literal('%')
        }
    })

    println(myExpr!!.compile().find(txt)?.value)

    myExpr.debug()

}
```

**Passing options to Regex class:**

As of version 0.6, you may forward ``RegexOption``'s to the internal ``Regex`` class.

```kotlin
import com.github.burnett01.expression.*

class main {

    /* Available options (JVM):
    IGNORE_CASE = Enables case-insensitive matching.
    MULTILINE = Enables multiline mode.
    LITERAL = Enables literal parsing of the pattern.
    UNIX_LINES = In this mode, only the '\n' is recognized as a line terminator.
    COMMENTS = Permits whitespace and comments in pattern.
    DOT_MATCHES_ALL = Enables the mode, when the expression . matches any character,
    including a line terminator.
    CANON_EQ = Enables equivalence by canonical decomposition.
    */
    val options: Set<RegexOption> = setOf(RegexOption.IGNORE_CASE)

    val txt: String = "HElLo CaTCh mE!%"
  
    val myExpr: Expression? = expression({
        capture {
            string("Hello catch me!")
            literal('%')
        }
    }, options)

    println(myExpr.compile().find(txt)?.value)

    myExpr!!.debug()

}
```

To be continued...

---

## Setup / Install

Gradle dependency:

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.burnett01:kotlin-expression-builder:0.8'
}
```

Maven dependency:

```kotlin
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Burnett01</groupId>
    <artifactId>kotlin-expression-builder</artifactId>
    <version>0.8</version>
</dependency>
```

Check [here](https://jitpack.io/#Burnett01/kotlin-expression-builder/) for more.


---

## Build

### Gradle

```gradle build```

---

## Unit-Tests

The testing-framework used by this package is [kotlintest](https://github.com/kotlintest/kotlintest/).

Various tests are performed to make sure this package runs as smoothly as possible.


* src/test/kotlin/com/github/burnett01/expression/expressionTest.kt

    `28 tests` | [Source](../master/src/test/kotlin/com/github/burnett01/expression/expressionTest.kt)

* src/test/kotlin/com/github/burnett01/expression/expression-builderTest.kt

    `3 tests` | [Source](../master/src/test/kotlin/com/github/burnett01/expression/expression-builderTest.kt)

Output:   

<img src="http://i.imgur.com/NDHTv1p.png" />

<img src="http://i.imgur.com/cDUo7Au.png" />

<img src="http://i.imgur.com/UcOORgP.png" />

### Gradle

```gradle test```

---

## Contributing

You're very welcome and free to contribute. Thank you.

---

## License

[MIT](LICENSE)
