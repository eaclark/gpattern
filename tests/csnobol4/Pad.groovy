package csnobol4

import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class Pad {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()

        matchCtx.with {
            /*
             *
             *        output = rpad("hello",10,"!")
             *        output = lpad("world",10)
             *        output = rpad("long",2)
             *end
             *
             *
             */

            println new SblString('hello').rpad(10, '!')
            println new SblString('world').lpad(10)
            println new SblString('long').rpad(2)
        }
    }
}
