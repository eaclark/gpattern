package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.NOTANY

/**
 * Created by eac on 12/9/13.
 */


class NotAnyFactory extends SblBaseFactory {

    def NotAnyFactory() {
        name = 'NOTANY'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = NOTANY
        ni.getPatString = { -> return 'NotAny(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
