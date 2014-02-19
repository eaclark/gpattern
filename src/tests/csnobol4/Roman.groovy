package tests.csnobol4

import sblbuilder.SblBuilder
import sblbuilder.SblMatchContext
import sblbuilder.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Roman {
            /*
             *
             ** ROMAN(N) - Convert integer N to Roman numeral form
             **       N must be positive and less than 4000
             **       An asterisk appears in the result if N >+ 4000
             **       The function fails if N is not an integer
             *
             *        DEFINE('ROMAN(N)UNITS')         :(ROMAN_END)
             *
             ** Get rightmost digit to UNITS and remove it from N
             ** Return null result if argument is null
             *ROMAN   N RPOS(1) LEN(1) . UNITS =      :F(RETURN)
             *
             ** Search for digit, replace with its Roman form.
             ** Return failing if not a digit.
             *        '0,1I,2II,3III,4IV,5V,6VI,7VII,8VIII,9IX,' UNITS
             *+               BREAK(',') . UNITS              :F(FRETURN)
             *
             ** Convert rest of N and multiply by 10.  Propagate a
             ** failure return from recursive call back to caller
             *        ROMAN = REPLACE(ROMAN(N),'IVXLCDM','XLCDM**') UNITS
             *+                       :S(RETURN) F(FRETURN)
             *
             *ROMAN_END
             *
             *
             *        DEFINE("TEST(I,J)")                             :(TEST_END)
             *TEST    OUTPUT = I ' -> ' ROMAN(I)
             *        EQ(I,J)                                         :S(RETURN)
             *        I = I + 1                                       :(TEST)
             *TEST_END
             *
             *        TEST(1,100)
             *        TEST(149,151)
             *        TEST(480,520)
             *        TEST(1900,2100)
             *
             *END
             *
             *
             */

    SblBuilder builder = new SblBuilder()

    def replace( String str, String targets, String replacements) {
        StringBuilder sb = new StringBuilder( str)
        if( targets.length() == replacements.length() ) {
            SblMatchContext matchCtx = builder.matchContext()
            SblString ss = new SblString( targets)
            str.eachWithIndex { chr, idx ->
                matchCtx.with {
                    def pass = ss[ Break( chr) + Setcur( 'LOC')]
                    if( pass != null) sb.replace( idx, idx+1, replacements[ LOC])
                }
            }
        }
        return sb.toString()
    }

    def roman( int n) {
        def res
        SblString convStr = new SblString('0,1I,2II,3III,4IV,5V,6VI,7VII,8VIII,9IX,')
        SblMatchContext matchCtx = builder.matchContext()

        SblString N = new SblString( n.toString())
        matchCtx.with {
            res = N.putAt( RPos(1) + Len(1).ca('UNITS'), '')
            if( res != null) {
                if( convStr[ StringPat( UNITS) + Break(',').ca('UNITS')] != null)
                    if( N.length() > 0) {
                        def rep = replace( roman( N.toString().toInteger()), 'IVXLCDM', 'XLCDM**')
                        res = rep + UNITS
                    }
                    else res = UNITS
            }
        }

        return res
    }


    static main(args) {
        def br = new Roman()
        def testIt = { int I -> println I + ' -> ' + br.roman( I) }
        (1i .. 100i).each testIt
        (149i .. 151i).each testIt
        (480i .. 520i).each testIt
        (1900i .. 2100i).each testIt
    }
}
