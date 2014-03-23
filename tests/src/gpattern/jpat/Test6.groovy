package gpattern.jpat

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 2/4/14.
 */

//
//   This is the pattern from the Jpattern Test6.pat file
//
//    public Pattern makePattern()
//    {
//        Pattern p = @fence(arb & "b") & "c"@;
//        return p;
//    }


public class Test6
{
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        SblString subject = new SblString('b')

        matchCtx.with {
            p = ~(Fence(Arb() + 'b') + 'c')

            result = subject[ p]
            if ( result == null) println 'failed'
            else println '|^' + result + '^|'
        }
    }
}

