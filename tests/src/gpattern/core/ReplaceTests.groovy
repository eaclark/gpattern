package gpattern.core

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class ReplaceTests {
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        matchCtx.with {
            // character replacement
            SblString X = new SblString('Hello World?')
            X[ ~('?')] = '!'
            assert X.value == 'Hello World!'

            // substring replacement
            X[ ~('Hello')] = 'Welcome'
            X[ ~('World')] = 'Earth'
            X[ ~(' ')] = ' to '
            assert X.value == 'Welcome to Earth!'

            // gstring handling
            def world = 'Earth'
            X[ ~("$world")] = 'Mars'
            assert X.value == "Welcome to Mars!"
        }
    }
}
