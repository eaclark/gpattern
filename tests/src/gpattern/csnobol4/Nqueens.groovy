package gpattern.csnobol4

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Nqueens {
            /*
             *
             ** N queens problem, a string oriented version to
             **   demonstrate the power of pattern matching.
             ** A numerically oriented version will run faster than this.
             *        N = 5
             *        NM1 = N - 1; NP1 = N + 1; NSZ = N * NP1; &STLIMIT = 10 ** 9; &ANCHOR = 1
             *        DEFINE('SOLVE(B)I')
             ** This pattern tests if the first queen attacks any of the others:
             *        TEST = BREAK('Q') 'Q' (ARBNO(LEN(N) '-') LEN(N) 'Q'
             *+             | ARBNO(LEN(NP1) '-') LEN(NP1) 'Q'
             *+             | ARBNO(LEN(NM1) '-') LEN(NM1) 'Q')
             *        P = LEN(NM1) . X LEN(1); L = 'Q' DUPL('-',NM1) ' '
             *        SOLVE()        :(END)
             *SOLVE   EQ(SIZE(B),NSZ)             :S(PRINT)
             ** Add another row with a queen:
             *        B = L B
             *LOOP    I = LT(I,N) I + 1 :F(RETURN)
             *        B TEST :S(NEXT)
             *        SOLVE(B)
             ** Try queen in next square:
             *NEXT    B P = '-' X :(LOOP)
             *PRINT   SOLUTION = SOLUTION + 1
             *        OUTPUT = 'Solution number ' SOLUTION ' is:'
             *PRTLOOP B LEN(NP1) . OUTPUT = :S(PRTLOOP)F(RETURN)
             *END
             *
             *
             */

    SblBuilder builder = new SblBuilder()
    int SOLUTION = 0

    String DUPL( String base, int i) {
        return base*i
    }

    def solve( SblString B_orig) {
        // have to copy the incoming SblString, because the SNOBOL
        // logic assumes the incoming variable is completely local and
        // writes all over it (e.g. see what the print portion does)
        //
        // Groovy, OTOH, is passing a reference, not a copy.

        SblString B = new SblString( B_orig.toString())

        SblMatchContext matchCtx = builder.matchContext()

        matchCtx.with {
            _AnchorMode = true
            N = 5
            NM1 = N - 1
            NP1 = N + 1
            NSZ = N * NP1

            X = '' // can we get away without initializing this?

            // This pattern tests if the first queen attacks any of the others:
            TEST = ~(Break('Q') + 'Q' +
                     ( Arbno(Len(N)   + '-') + Len(N)   + 'Q' |
                       Arbno(Len(NP1) + '-') + Len(NP1) + 'Q' |
                       Arbno(Len(NM1) + '-') + Len(NM1) + 'Q'))

            P = Len(NM1).ca('X') + Len(1)
            L = 'Q' + DUPL('-',NM1) + ' '

            I = 0
            if( B.length() < NSZ) {
                // Add another row with a queen:
                B = L + B
                while( I < N ) {
                    I++
                    if( B[ TEST] == null) {
                        solve(B)
                    }

                    // Try a queen in next square:
                    //B[~P] = '-' + X
                    //
                    // SNOBOL does the LHS match (if it's present) *before* building the assignment string
                    // so X has the right value before the RHS string is built. But, there is a bug in the
                    // Groovy compiler where it does the RHS before the LHS, so X won't be properly assigned.
                    // One solution is to defer the evaluation of the X variable by enclosing it in a closure
                    // that will be call internally *after* doing the LHS pattern match.
                    B[ P] = {'-' + X}
                }
            } else  {
                // we have parsed the required number of rows,  Time to break the string
                // apart into substrings of length NP1 to form the answer array.
                this.SOLUTION = this.SOLUTION + 1
                println 'Solution number ' + this.SOLUTION + ' is:'
                while( B.putAt(  Len(NP1).ca('_OUTPUT'), '') != null) {}
                if( this.SOLUTION >= 10) System.exit(0)
            }
        }
    }

    static main(args) {
        def nq = new Nqueens()
        nq.solve( new SblString(''))
    }
}
