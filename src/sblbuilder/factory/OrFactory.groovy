package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.ALTERNATE

/**
 * Created by eac on 12/9/13.
 */


class OrFactory extends SblBaseFactory {

    def OrFactory() {
        name = 'OR'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = ALTERNATE
        return ni
    }
}
