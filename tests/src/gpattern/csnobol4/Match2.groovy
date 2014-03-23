package gpattern.csnobol4

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Match2 {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx = builder.matchContext()

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
