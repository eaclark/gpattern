package csnobol4

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Match3 {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *        X = "Hello World?"
             *LOOP    X '?' = '!'
             *        OUTPUT = X
             *END
             *
             *
             */


            SblString X = new SblString('Hello World?')
            X[ StringPat('?')] = '!'
            println X
        }
    }
}
