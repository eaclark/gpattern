package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.STRINGPAT

/**
 * Created by Ed Clark on 12/9/13.
 */


class StringPatFactory extends SblBaseFactory {

    def StringPatFactory() {
        name = 'STRINGPAT'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = STRINGPAT
        ni.getPatString = { -> return "'" + value + "'" }
        ni.getPatString.delegate = ni
        return ni
    }
}
