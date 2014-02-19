package tests

import sblbuilder.SblBuilder
import sblbuilder.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class ReplaceTests {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            // character replacement
            SblString X = new SblString('Hello World?')
            X[ StringPat('?')] = '!'
            assert X.value == 'Hello World!'

            // substring replacement
            X[ StringPat('Hello')] = 'Welcome'
            X[ StringPat('World')] = 'Earth'
            X[ StringPat(' ')] = ' to '
            assert X.value == 'Welcome to Earth!'

            //
        }
    }
}
