package tests.jpat

import sblbuilder.SblBuilder
import sblbuilder.SblString

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

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('b')

        matchCtx.with {
            p = Fence(Arb() + StringPat('b')) + StringPat('c')

            result = subject[ p]
            if ( result == null) println 'failed'
            else println '|^' + result + '^|'
        }
    }
}

