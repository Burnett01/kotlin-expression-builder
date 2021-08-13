/*
* The MIT License (MIT)
*
* Product:      Kotlin Expression Builder (DSL)
* Description:  A decent expression (Regex) builder written in Kotlin.
*
* Copyright (c) 2017-2021 Steven Agyekum <agyekum@posteo.de>
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

import kotlin.text.Regex
import kotlin.text.RegexOption
import kotlin.collections.Set
import kotlin.collections.emptySet

/**
 * @class: Q <enum>
 * Q.ZERO_OR_ONE  = '?'
 * Q.ZERO_OR_MORE = '*'
 * Q.ONE_OR_MORE  = '+'
 */
enum class Q {
    ZERO_OR_ONE,
    ZERO_OR_MORE,
    ONE_OR_MORE
}

/**
 * @class: Expression
 * @param: {Set<RegexOption>} op | Regex options (optional)
 */
open class Expression(internal val op: Set<RegexOption> = emptySet()) {

    /**
     * @property: result | Final expression
     */
    internal val result: StringBuilder = StringBuilder()

    /**
     * @function: append
     * @param {String} ex | String to append
     */
    private fun append(ex: String?) = result.append(ex)

    /**
     * @function: setStart
     */
    fun markStart(): StringBuilder = append("^")

    /**
     * @function: setEnd
     */
    fun markEnd(): StringBuilder = append("$")

    /**
     * @function: setOr
     */
    fun markOr(): StringBuilder = append("|")

    /**
     * @function: quantity
     * @param {Q} quant | Quantity
     */
    fun quantity(quant: Q = Q.ZERO_OR_ONE) {
        when(quant) {
            Q.ZERO_OR_ONE  -> append("?")
            Q.ZERO_OR_MORE -> append("*")
            Q.ONE_OR_MORE  -> append("+")
        }
    }

    /**
     * @function: range
     * @param {Any} min | Minimum
     * @param {Any} max | Maximum
     * @param {Char} delim | Delimiter
     */
    fun range(min: Any, max: Any, delim: Char = '-') {
        val result = "$min$delim$max"
        when { 
            (min is Int || min is String 
        &&  (max is Int || max is String))
        -> 
            when(delim) {
                '-' -> append("[$result]")
                ',' -> append("{$result}")
                else -> append(result)
            }
        else -> append(result)
        }
    }

    /**
     * @function: exact
     * @param {Int} times | Amount
     */
    fun exact(times: Int) = range(times, times, ',')

    /**
     * @function: setChar
     * @param {Char} char | Character
     */
    fun setChar(char: Char): StringBuilder = append(char.toString())

    /**
     * @function: setChar
     * @param {String} str | String
     */
    fun setString(str: String): StringBuilder = append(str)

    /**
     * @function: setLiteral
     * @param {Char} lit | Literal
     */
    fun setLiteral(lit: Char): StringBuilder = append("\\$lit")

    /**
     * @function: setDigit
     */
    fun setDigit(): StringBuilder = append("\\d")

    /**
     * @function: setWord
     */
    fun setWord(): StringBuilder = append("\\w")

    /**
     * @function: startMatch
     */
    fun startMatch(): StringBuilder = append("[")

    /**
     * @function: endMatch
     */
    fun endMatch(): StringBuilder = append("]")

    /**
     * @function: startGroup
     * @param {Int} type | ID
     */
    fun startGroup(type: Int) {
        when(type) {
            0 -> append("(")
            1 -> append("(?:")
        }
    }

    /**
     * @function: endGroup
     */
    fun endGroup(): StringBuilder = append(")")

    /**
     * @function: debug
     */
    fun debug() = println("$this | Regex: $result")
    
    /**
     * @function: compile
     */
    fun compile(): Regex = Regex(result.toString(), op)
}
