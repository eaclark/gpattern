package csnobol4

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Alt1 {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *  S = 'NETWORK PARSE_CLAUSE END PARSE_CLAUSE'
             *
             ** Keyword settings
             *
	         *  &ANCHOR = 0
             *  &TRIM	= 1
             *
             ** Utility patterns
             *
             *	BLANK	= ' '
             *
             *	LEFT_END  = POS(0)
             *	RIGHT_END = RPOS(0)
             *
             *	BLANKS	= SPAN(BLANK)
             *	OPT_BLANKS = BLANKS | null
             *	BB	= BREAK(BLANK)
             *
             *****************************************************************
             *
             *	COMPLETE_PAT =
             *+		(LEFT_END 'EXEC' BLANKS) |
             *+		(LEFT_END BB BLANKS (BB $ TNAME) BLANKS FAIL) |
             *+		('END' OPT_BLANKS *TNAME RIGHT_END)
             *
             *****************************************************************
             *
             *	S COMPLETE_PAT					:F(DONE)
             *	OUTPUT = 'yes'
             *DONE	OUTPUT = 'NAME: ' TNAME
             *END
             *
             */


            SblString S = new SblString('NETWORK PARSE_CLAUSE END PARSE_CLAUSE')

            // Keyword settings
            // &ANCHOR = 0
            // &TRIM	= 1
            _AnchorMode = false
            // matcher.setTrimMode( true)

            // Utility patterns
            TNAME = ''
            BLANK = ' '

            LEFT_END  = Pos(0)
            RIGHT_END = RPos(0)

            BLANKS	= Span( BLANK)
            OPT_BLANKS = BLANKS | StringPat( '')
            BB	= Break( BLANK)

            //****************************************************************

            COMPLETE_PAT = (LEFT_END + StringPat('EXEC') + BLANKS) |
                           (LEFT_END + BB + BLANKS + BB.ia('TNAME') + BLANKS + Fail()) |
                           ( StringPat('END') + OPT_BLANKS + Defer('TNAME') + RIGHT_END)

            //****************************************************************

            result = S[ COMPLETE_PAT]

            if ( result != null) println 'yes'
            println 'NAME: ' + TNAME
        }
    }
}
