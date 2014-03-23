package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.CHARPAT

/**
 * Created by Ed Clark on 12/9/13.
 */


class CharPatFactory extends SblBaseFactory {

    def CharPatFactory() {
        name = 'CHARPAT'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = CHARPAT
        ni.getPatString = { -> return 'Char(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
