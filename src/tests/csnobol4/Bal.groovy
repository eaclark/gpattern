package tests.csnobol4

import sblbuilder.SblBuilder
import sblbuilder.SblString

import javax.management.remote.rmi._RMIConnection_Stub

/**
 * Created by Ed Clark on 1/10/14.
 */

class Bal {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *    DEFINE("X(S)")					:(EX)
             *X   S BAL . B					:F(F)
             *    OUTPUT = '  ' S ' => ' B			:(RETURN)
             *F   OUTPUT = '  ' S ' => *FAIL*'			:(RETURN)
             *EX
             *
             *    DEFINE("Y()")					:(EY)
             *Y   X('A')
             *    X('A(X)')
             *    X('AA(X)')
             *    X('()')
             *    X('((()))')
             *    X('((())')
             *    X(')(')
             *                            :(RETURN)
             *EY
             *
             *    OUTPUT = '&ANCHOR = 0'
             *    &ANCHOR = 0
             *    Y()
             *
             *    OUTPUT = '&ANCHOR = 1'
             *    &ANCHOR = 1
             *    Y()
             *END
             *
             *
             */

            def X = { s ->
                if( s[ Bal().ca('B')] != null) println '  ' + s + ' => ' + B
                else println '  ' + s + ' => *FAIL*'
            }

            def Y = { ->
                X( new SblString('A'))
                X( new SblString('A(X)'))
                X( new SblString('AA(X)'))
                X( new SblString('()'))
                X( new SblString('((()))'))
                X( new SblString('((())'))
                X( new SblString(')('))
            }

            println '&ANCHOR = 0'
            _AnchoredMode = false
            Y()

            println '&ANCHOR = 1'
            _AnchorMode = true
            Y()
        }
    }
}
