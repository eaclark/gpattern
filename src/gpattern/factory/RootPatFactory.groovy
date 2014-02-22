package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE

/**
 * Created by Ed Clark on 12/9/13.
 */


class RootPatFactory extends SblBaseFactory {

    def RootPatFactory() {
        name = 'ROOT'
    }
    def RootPatFactory( String name) {
        this.name = name
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        return ni
    }
}
