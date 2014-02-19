package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.SPAN

/**
 * Created by Ed Clark on 12/9/13.
 */


class SpanFactory extends SblBaseFactory {

    def SpanFactory() {
        name = 'SPAN'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = SPAN
        ni.handleDefer = true
        return ni
    }
}
