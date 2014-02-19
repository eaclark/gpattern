package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.NOTANY

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
        return ni
    }
}
