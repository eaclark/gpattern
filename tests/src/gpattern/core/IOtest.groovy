package gpattern.core

import gpattern.SblBuilder
import gpattern.SblMatchContext

/**
 * Created by Ed Clark on 1/10/14.
 */

class IOtest {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx = builder.matchContext()
        matchCtx.matcher.setIO()

        matchCtx.with {
            println 'type something (and hit Enter)'
            //strip the newline character
            def typed = INPUT[0..-2]

            println 'The input was "' + typed + '"'

            _ALPHABET[ Break( typed) + Setcur( 'ASCII')]

            println "The ASCII value for the (lexically) smallest character typed is $ASCII"
        }
    }
}
