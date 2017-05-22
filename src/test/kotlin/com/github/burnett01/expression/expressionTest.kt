/*
* The MIT License (MIT)
*
* Product:      Kotlin Expression Builder (DSL)
* Description:  A fairly decent expression (Regex) builder written in Kotlin.
*
* Copyright (c) 2017 Steven Agyekum <agyekum@posteo.de>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software
* and associated documentation files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
* and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
* subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies
* or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
*/

package com.github.burnett01.expression

import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec


class ExpressionTest : StringSpec({

    val expression = Expression()

    /*         INTEGRITY TESTS          */

    "expression.result should be of type <StringBuilder>" {
        expression.result should beOfType<StringBuilder>()
    }

    "expression.markStart should mark start position" {
        expression.markStart()
        expression.result.toString() should startWith("^")
    }

    "expression.markEnd should mark end position" {
        expression.markEnd()
        expression.result.toString() should endWith("$")
    }

    "expression.markOr should mark 'or' position" {
        expression.markOr()
        expression.result.toString() should endWith("|")
    }

    "expression.quantity should add quantifier 1 = '?'" {
        expression.quantity(Q.ZERO_OR_ONE)
        expression.result.toString() should endWith("?")
    }

    "expression.quantity should add quantifier 2 = '*'" {
        expression.quantity(Q.ZERO_OR_MORE)
        expression.result.toString() should endWith("*")
    }

    "expression.quantity should add quantifier 3 = '+'" {
        expression.quantity(Q.ONE_OR_MORE)
        expression.result.toString() should endWith("+")
    }

    "expression.range should add range from 0 to 9 | delim = '-'" {
        expression.range(0, 9)
        expression.result.toString() should endWith("[0-9]")
    }

    "expression.range should add range from 0 to 9 | delim = ','" {
        expression.range(0, 9, ',')
        expression.result.toString() should endWith("{0,9}")
    }
    
    "expression.range should add range from A to z | delim = '-'" {
        expression.range('A', 'z')
        expression.result.toString() should endWith("A-z")
    }

    "expression.exact should add exact 9" {
        expression.exact(9)
        expression.result.toString() should endWith("{9,9}")
    }

    "expression.setChar should add a character (Char) = 'A'" {
        expression.setChar('A')
        expression.result.toString() should endWith("A")
    }

    "expression.setString should add a string (setString) = TestString" {
        expression.setString("TestString")
        expression.result.toString() should endWith("TestString")
    }

    "expression.setLiteral should add a literal (Char) = '\\%'" {
        expression.setLiteral('%')
        expression.result.toString() should endWith("\\%")
    }

    "expression.setDigit should add digit class = '\\d'" {
        expression.setDigit()
        expression.result.toString() should endWith("\\d")
    }

    "expression.setWord should add word class = '\\w'" {
        expression.setWord()
        expression.result.toString() should endWith("\\w")
    }

    "expression.startMatch should start match class = '['" {
        expression.startMatch()
        expression.result.toString() should endWith("[")
    }

    "expression.endMatch should end match class = ']'" {
        expression.endMatch()
        expression.result.toString() should endWith("]")
    }

    "expression.startGroup should start capture group (0) = '('" {
        expression.startGroup(0)
        expression.result.toString() should endWith("(")
    }

    "expression.endGroup should end capture group (0) = ')'" {
        expression.endGroup()
        expression.result.toString() should endWith(")")
    }

    "expression.startGroup should start non-capture group (1) = '(?:'" {
        expression.startGroup(1)
        expression.result.toString() should endWith("(?:")
    }

    "expression.endGroup should end non-capture group (1) = ')'" {
        expression.endGroup()
        expression.result.toString() should endWith(")")
    }

    "expression.compile should be of type <Regex>" {
        expression.compile() should beOfType<Regex>()
    }

    /*         RUNTIME TESTS          */

    val date: String = "20.05.2017"

    val origPattern = "(\\d{2,2}.\\d{2,2}.\\d{4,4})"
    val origVal = Regex(origPattern).find(date)!!.value
    
    val runtimeExpr = Expression()

    runtimeExpr.startGroup(0)
    runtimeExpr.setDigit()
    runtimeExpr.exact(2)
    runtimeExpr.setChar('.')
    runtimeExpr.setDigit()
    runtimeExpr.exact(2)
    runtimeExpr.setChar('.')
    runtimeExpr.setDigit()
    runtimeExpr.exact(4)
    runtimeExpr.endGroup()

    "runtimeExpr.compile should compile and return pattern = '$origPattern'" {
        runtimeExpr.compile()
            .pattern.toString() should include(origPattern)
    }

    "runtimeExpr.compile.matchEntire().value should return origVal = '$date'" {
        runtimeExpr.compile()
            .matchEntire(date)?.value.toString() should include(origVal.toString())
    }

})
