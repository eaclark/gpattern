package gpattern.csnobol4

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Match3 {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx = builder.matchContext()

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
            X[ ~'?'] = '!'
            println X
        }
    }
}
