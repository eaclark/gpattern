package gpattern.csnobol4

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Reverse {
    static main(args) {

        SblBuilder builder = new SblBuilder()

        SblMatchContext matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *        output = reverse("dlrow olleh")
             *        output = reverse("daed si luap")
             *        output = reverse(reverse(&UCASE))
             *end
             *
             *
             */

            registerVariable( '_UCASE', new SblString('ABCDEFGHIJKLMNOPQRSTUVWXYZ'))

            println new SblString('dlrow olleh').reverse()
            println new SblString('daed si luap').reverse()
            println _UCASE.reverse().reverse()
        }
    }
}
