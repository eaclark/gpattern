package gpattern.csnobol4

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Match4 {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *        X = "Hello World!"
             *LOOP    X 'o' = 'x'             :S(LOOP)
             *        OUTPUT = X
             *END
             *
             *
             */


            SblString X = new SblString('Hello World!')
//            while ( (X[~StringPat('o')] = 'x') != null) {}
            while ( X.putAt( ~('o'), 'x')) {}
            println X

//            // the following is an infinite loop because 'a[b]=c' is NOT 100%
//            // compatible with 'a.putAt(b,c)'
//            def z = { -> X[~StringPat('x')] = 'o' }        // this seems to execute the pattern match and assignment!!!!
//            while ( z() != null) {}
//            println X
        }
    }
}
