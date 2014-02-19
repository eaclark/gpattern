package tests.csnobol4

import sblbuilder.SblBuilder
import sblbuilder.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Breakx {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *
             *    &anchor = 1
             *    subj = "this is a test ."
             *    subj breakx(' ') $ output '.'
             *    subj (break(' ') arbno(len(1) break(' '))) $ output '.'
             *end
             *
             */

            _AnchorMode = true
            subj = new SblString( 'this is a test .')
            subj[  BreakX(' ').ia( '_OUTPUT') + StringPat(' .')]
            subj[ ( Break(' ') + Arbno( Len(1) + Break(' '))).ia( '_OUTPUT') + StringPat(' .')]
        }
    }
}
