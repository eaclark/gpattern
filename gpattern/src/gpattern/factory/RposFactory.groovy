package gpattern.factory

import gpattern.SblPatNode

import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.RPOS

/**
 * Created by eac on 12/9/13.
 */


class RposFactory extends SblBaseFactory {

    def RposFactory() {
        name = 'RPOS'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = RPOS
        ni.getPatString = { -> return 'RPos(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
