package gpattern.jpat

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/9/14.
 */

//
//   This is the pattern from the Jpattern Test3a1.pat file
//
//    public Pattern makePattern()
//    {
//        vars.put("DecDigits","0123456789");
//        vars.put("HexDigits","0123456789abcdefABCDEF");
//        Digs = @span(+DecDigits)@;
//        UDigs = @+Digs arbno("_" +Digs)@;
//        Hdig = @span(+HexDigits)@;
//        UHdig = @+Hdig arbno("_" +Hdig)@;
//        Bnum = @+UDigs "#" +UHdig "#"@;
//        vars.put("Digs",Digs);
//        vars.put("UDigs",UDigs);
//        vars.put("Hdig",Hdig);
//        vars.put("UHdig",UHdig);
//        vars.put("Bnum",Bnum);
//        return Bnum; // top level pattern
//    }


public class Test3a1
{
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        SblString subject = new SblString('16#123_abc#')

        matchCtx.with {
            DecDigits  = "0123456789"
            HexDigits = "0123456789abcdefABCDEF"
            Digs = Span(DecDigits)
            UDigs = ~(Digs + Arbno( '_' + Digs))
            Hdig = Span(HexDigits)
            UHdig = ~(Hdig + Arbno( '_' + Hdig))
            Bnum = ~(UDigs + "#" + UHdig + "#")

            result = subject[ Bnum]
            if ( result == null) println 'failed'
            else println '|^' + result + '^|'
        }
    }
}

