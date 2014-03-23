package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.ARBNO

/**
 * Created by Ed Clark on 12/9/13.
 */


class ArbnoFactory extends SblBaseFactory {

    def ArbnoFactory() {
        name = 'ARBNO'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = ARBNO
        ni.getPatString = { -> return 'ArbNo(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
