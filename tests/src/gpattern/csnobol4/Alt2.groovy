package gpattern.csnobol4

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Alt2 {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *	'AAA' ('FOO' | (SPAN('A') $ TNAME) | 'BAR')	:F(DONE)
	         *   OUTPUT = 'yes'
             *DONE	OUTPUT  = 'NAME: ' TNAME
             *END
             *
             *
             */


            SblString S = new SblString('AAA')

            result = S[ ~('FOO') | (Span('A').ca('TNAME')) | ~('BAR')]

            if ( result != null) println 'yes'
            println 'NAME: ' + TNAME
        }
    }
}
