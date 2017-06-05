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


class ExpressionBuilderTest : StringSpec({

    /*         INTEGRITY TESTS          */

    "pattern should match test pattern" {

        val testPattern = "^$|{3,3}[0-9]{0,9}\\%\\d\\w[test](test2)(?:test3)A + * ?"

        val builderA = expression({

            start()
            end()
            or()
            exact(3)
            range(0, 9)
            range(0, 9, ',')
            literal('%')
            digit()
            word()
            match { string("test") }
            capture { string("test2") }
            nocapture { string("test3") }
            char('A')
            whitespace()
            quantity(Q.ONE_OR_MORE)
            whitespace()
            quantity(Q.ZERO_OR_MORE)
            whitespace()
            quantity(Q.ZERO_OR_ONE)
            
        })

        builderA?.compile()?.pattern.toString() should include(testPattern)
    }

    /*         RUNTIME TESTS          */

    "pattern should match original pattern" {

        val origPatternB = "(\\d{2,2}.\\d{2,2}.\\d{4,4})"

        val builderB = expression({

            capture {
                digit { exact(2) }
                char('.')
                digit { exact(2) }
                char('.')
                digit { exact(4) }
            }
        
        })

        builderB?.compile()?.pattern.toString() should startWith(origPatternB)
    }

    /*         RUNTIME TESTS (RegexOption)         */

    "pattern should match original pattern (RegexOption)" {

        val opts: Set<RegexOption> = setOf( RegexOption.IGNORE_CASE )

        val origPatternC = "(Catch me\\!)"

        val builderC = expression({

            capture {
                string("Catch me")
                literal('!')
            }
        
        }, opts)

        builderC?.compile()?.pattern.toString() should startWith(origPatternC)
    }

})
