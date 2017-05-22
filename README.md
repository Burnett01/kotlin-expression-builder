
# kotlin-expression-builder

[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/Burnett01/paypal-basket/master/LICENSE) 
[![Build Status](https://travis-ci.org/Burnett01/kotlin-expression-builder.svg?branch=master)](https://travis-ci.org/Burnett01/kotlin-expression-builder) [![](https://jitpack.io/v/burnett01/kotlin-expression-builder.svg)](https://jitpack.io/#burnett01/kotlin-expression-builder)


<img src="http://i.imgur.com/I0DES9A.png" align="right"/>

A fairly decent expression class and builder (DSL), written in Kotlin.

This package will assist you in writing and managing your regular expressions (Regex).<br>

I'll be using this package in my upcoming HAFAS-parser project, in order to simplify very complex regular expressions.

Features:
  * Easy Regex management
  * Tests

---

# Table of contents
* [API Reference](#api-reference)
  * [Expression](#expression)
  * [ExpessionBuilder](#expressionbuilder)
* [Usage](#usage)
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
class Expression

fun markStart() /* '^'  */
fun markEnd()   /* '$'  */
fun markOr()    /* '|'  */

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

fun exact(times: Int)       /* {times,times} */
fun setChar(char: Char)     /* char          */
fun setString(str: String)  /* str           */
fun setLiteral(lit: Char)   /* \lit          */
fun setDigit()              /* \d            */
fun setWord()               /* \w            */

fun startMatch()            /* [             */
fun endMatch()              /* ]             */

/* Start Capture Group
 * 0 = capture -> (
 * 1 = no capture -> (?:
 */
fun startGroup(type: Int)   
fun endGroup() //=> )

fun debug()
fun compile(): Regex
```

### ExpressionBuilder:

> For documentation check above (Expression class) or demos/examples.

```kotlin

class ExpressionBuilder

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

  
fun expression( entity: ExpressionBuilder.() -> Unit ): Expression?
```

---

## Usage:

Demo 1:

```kotlin
import com.github.burnett01.expression.*

class main {

    val date: String = "20.05.2017"
    val origExpr = Regex("(\\d{2}.\\d{2}.\\d{4})")

    val myExpr: Expression? = expression {
        capture {
            digit({ exact(2) })
            literal('.')
            digit({ exact(2) })
            literal('.')
            digit({ exact(4) })
        }
    }

    println(origExpr.find(date)?.value)
    println(myExpr.compile().find(date)?.value)

    myExpr!!.debug()

}
```

Demo 2:

```kotlin
import com.github.burnett01.expression.*

class main {

    val txt: String = "Hello catch me!%"
  
    val myExpr: Expression? = expression {
        capture {
            string("Hello catch me!")
            literal('%')
        }
    }

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
    compile 'com.github.burnett01:kotlin-expression-builder:0.5'
}
```

---

## Build

### Gradle

```gradle build```

---

## Unit-Tests

The testing-framework used by this package is [kotlintest](https://github.com/kotlintest/kotlintest/).

Various tests are performed to make sure this package runs as smoothly as possible.


* src/test/kotlin/com/github/burnett01/expression/expressionTest.kt

    `25 tests` | [Source](../master/src/test/kotlin/com/github/burnett01/expression/expressionTest.kt)

* src/test/kotlin/com/github/burnett01/expression/expression-builderTest.kt

    `2 tests` | [Source](../master/src/test/kotlin/com/github/burnett01/expression/expression-builderTest.kt)

Output:   

<img src="http://i.imgur.com/SOJbN61.png" />

<img src="http://i.imgur.com/lihoy5t.png" />

### Gradle

```gradle test```

---

## Contributing

You're very welcome and free to contribute. Thank you.

---

## License

[MIT](../master/LICENSE.MD)
