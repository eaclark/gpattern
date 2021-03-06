package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.BREAK

/**
 * Created by Ed Clark on 12/9/13.
 */


class BreakFactory extends SblBaseFactory {

    def BreakFactory() {
        name = 'BREAK'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = BREAK
        ni.getPatString = { -> return 'Break(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
