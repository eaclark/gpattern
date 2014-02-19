package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.CHARPAT

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
        return ni
    }
}
