package tests.csnobol4

import sblbuilder.SblBuilder
import sblbuilder.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Match2 {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *        'AAA' LEN(1) $ X
             *        OUTPUT = X
             *END
             *
             *
             */


            new SblString('AAA')[ Len(1).ca('X')]
            println X
        }
    }
}
