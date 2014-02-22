package jpat

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/2/14.
 */

//
//   This is the pattern from the Jpattern Test1.pat file
//
//        public Pattern makePattern()
//        {
//            // Use the ability to embed java code to include Digs
//            Pattern Digs = @span("0123456789")@;
//            Pattern Lnum = @pos(0) (`Digs` ".") span(" ")@;
//            vars.put("Digs",Digs);
//            vars.put("Lnum",Lnum);
//            return Lnum;
//        }

public class Test1
{
    static main(args) {

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('41234.  ')

        matchCtx.with {
            Digs = Span( '0123456789')
            Lnum = Pos(0) + Digs + StringPat('.') + Span(' ')

            result = subject[ Lnum]
            if ( result == null) println 'failed'
            else println '|^' + result + '^|'
        }
    }
}

