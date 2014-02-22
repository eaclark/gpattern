package jpat

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/2/14.
 */

//
//   This is the pattern from the Jpattern Test2b.pat file
//
//    public Pattern makePattern()
//    {
//        B = @nspan(" ")@;
//        N = @span("0123456789")@;
//        T = @nspan(" ") +N*Num1 span(" ,") +N*Num2@;
//        vars.put("B",B);
//        vars.put("N",N);
//        vars.put("T",T);
//        return T; // top level pattern
//    }

public class Test2b
{
    static main(args) {

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('  456 004  ')

        matchCtx.with {
            B = NSpan(" ")
            N = Span("0123456789")
            T = NSpan(" ") + N.ia('Num1') + Span(" ,") + N.ca('Num2')

            result = subject[ T]
            if ( result == null) println 'failed'
            else println '|^' + result + '^|'
            println 'Num1 = ' + vars.get('Num1')
            println 'Num2 = ' + vars.get('Num2')
        }
    }
}

