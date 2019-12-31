/*
* The MIT License (MIT)
*
* Product:      Kotlin Expression Builder (DSL)
* Description:  A decent expression (Regex) builder written in Kotlin.
*
* Copyright (c) 2017-2020 Steven Agyekum <agyekum@posteo.de>
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


/**
 * @class: ExpressionBuilder
 */
class ExpressionBuilder() {

    /**
     * @property: opts | Regex options
     */
    internal var opts: Set<RegexOption> = emptySet()

    /**
     * @constructor
     * @param: {ExpressionBuilder} entity | Self instance
     * @param: {Set<RegexOption>} options | Regex options (optional)
     */
    constructor( 
        entity: ExpressionBuilder.() -> Unit,
        options: Set<RegexOption>
    ) : this() {
        opts = options
        entity()
    }

    /**
     * @property: expression | Final expression
     */
    internal val expression: Expression? by lazy {
        Expression(opts)
    }

    /**
     * @function: start
     */
    fun start() = expression!!.markStart()

    /**
     * @function: end
     */
    fun end() = expression!!.markEnd()

    /**
     * @function: or
     */
    fun or() = expression!!.markOr()

    /**
     * @function: quantity
     * @param {Q} quant | Quantity
     */
    fun quantity( quant: Q ) = expression!!.quantity(quant)

    /**
     * @function: exact
     * @param {Int} times | Amount
     */
    fun exact( times: Int ) = expression!!.exact(times)

    /**
     * @function: range
     * @param {Int} min | Minimum
     * @param {Int} max | Maximum
     * @param {Char} delim | Delimiter
     */
    fun range( min: Any, max: Any, delim: Char = '-' ) 
      = expression!!.range(min, max, delim)

    /**
     * @function: literal
     * @param {Char} lit | Literal
     */
    fun literal( lit: Char ) = expression!!.setLiteral(lit)

    /**
     * @function: decimal
     * @desc: Convinience wrapper
     */
    fun decimal( aggr: () -> Unit = {} ) = aggr()

    /**
     * @function: string
     * @param {String} str | String
     */
    fun string( str: String ) = expression!!.setString(str)

    /**
     * @function: char
     * @param {Char} char | A single char
     */
    fun char( char: Char ) = expression!!.setChar(char)

    /**
     * @function: digit
     */
    fun digit( aggr: () -> Unit = {} ) {
        expression!!.setDigit()
        aggr()
    }

    /**
     * @function: word
     */
    fun word( aggr: () -> Unit = {} ) {
        expression!!.setWord()
        aggr()
    }

    fun whitespace() = char(' ').toString() //\s etc

    /**
     * @function: match
     * @param {String} chars | String
     * @param {()->Unit)} aggr | Aggregator
     */
    fun match( aggr: () -> Unit = {} ) {
        expression!!.startMatch()
        aggr()
        expression!!.endMatch()
    }

    /**
     * @function: nocapture
     * @param {()->Unit)} aggr | Aggregator
     */
    fun nocapture( aggr: () -> Unit ) {
        expression!!.startGroup(1)
        aggr()
        expression!!.endGroup()
    }

    /**
     * @function: capture
     * @param {()->Unit)} aggr | Aggregator
     */
    fun capture( aggr: () -> Unit ) {
        expression!!.startGroup(0)
        aggr()
        expression!!.endGroup()
    }

    /**
     * @function: boot
     */
    fun boot(): Expression? = expression
}

/**
 * @function: expression
 * @param {ExpressionBuilder} entity | ExpressionBuilder
 * @param {Set<RegexOption>} options | Regex options
 */
fun expression( 
    entity: ExpressionBuilder.() -> Unit,
    options: Set<RegexOption> = emptySet()
): Expression? {
    return ExpressionBuilder(entity, options).boot()
}
