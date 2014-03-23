package gpattern.core

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class MultiCtxTests {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx1 = builder.matchContext()
        SblMatchContext matchCtx2 = builder.matchContext()

        SblString str1 = new SblString('Much ado about nothing')

        matchCtx1.with {
            // setup a local pattern
            pat = ~('ado')
            // make a local copy
            str2 = str1
        }

        matchCtx2.with {
            // setup a local pattern
            pat = ~('nothing')
            // make a local copy
            str2 = str1
        }

        matchCtx1.with {
            str2[ pat] = 'fuss'
            assert str2.value == 'Much fuss about nothing'
        }

        matchCtx2.with {
            str2[ pat] = 'everything'
            assert str2.value == 'Much ado about everything'
        }

        // original SblString was not changed
        assert str1.value == 'Much ado about nothing'
    }
}
