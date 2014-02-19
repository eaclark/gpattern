package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.CONCATENATE

/**
 * Created by Ed Clark on 12/9/13.
 */


class PlusFactory extends SblBaseFactory {

    def PlusFactory() {
        name = 'PLUS'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {

        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = CONCATENATE
        return ni
    }
}
