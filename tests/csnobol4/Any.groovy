package csnobol4

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Any {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        String subject = 'the quick brown fox jumped'

        matchCtx.with {
            /*
             *
             *	&alphabet any("xyzabc123")  $ output fail
             *	"the quick brown fox jumped" notany("etian")  $ output fail
             *end
             *
             *
             */

            def S = new SblString( subject)
            _ALPHABET[ Any('xyzabc123').ia('_OUTPUT') + Fail()]
            S[ NotAny('etian').ia('_OUTPUT') + Fail()]
        }
    }
}
