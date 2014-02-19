package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.BAL

/**
 * Created by Ed Clark on 12/9/13.
 */


class BalFactory extends SblBaseFactory {

    def BalFactory() {
        name = 'BAL'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = BAL
        return ni
    }
}
