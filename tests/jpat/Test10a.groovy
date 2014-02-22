package jpat

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 2/6/14.
 */

//
//   This is the pattern from the Jpattern Test10a.pat file
//
//    public Pattern makePattern()
//    {
//        // use embedded Java code
//        int LEN = 1;
//        Pattern p = @(len(1) $ x) & (len(`LEN`) = +x)@;
//        return p;
//    }


public class Test10a
{
    static main(args) {

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('abcabc')

        matchCtx.with {
            int LEN = 1
            p = Len(1).ia('x') + Len(LEN).cr('x')

            println 'subject = ' + subject
            subject[ p]
            println '|>' + subject[ p] + '<|'
            println 'subject = ' + subject
            println 'x = "' + x + '"'
        }
    }
}

