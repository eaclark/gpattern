package tests.csnobol4

import sblbuilder.SblBuilder
import sblbuilder.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Unsc {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *        STR = '12 x 23434 x 123'
             *        P = SPAN('0123456789')
             *
             *        STR (*P $ OUTPUT) FAIL
             *END
             *
             *
             */


            SblString S = new SblString('12 x 23434 x 123')
            P = Span('0123456789')

            S[ Defer('P').ia( '_OUTPUT') + Fail()]
        }
    }
}
