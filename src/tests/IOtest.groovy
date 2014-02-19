package tests

import sblbuilder.SblBuilder

/**
 * Created by Ed Clark on 1/10/14.
 */

class IOtest {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()
        matchCtx.matcher.setIO()

        matchCtx.with {
            println 'type something (and hit Enter)'
            def typed = INPUT

            println 'The input was "' + typed + '"'

            _ALPHABET[ Break( typed) + Setcur( 'ASCII')]

            println "The ASCII value for the (lexically) smallest character typed is $ASCII"
        }
    }
}
