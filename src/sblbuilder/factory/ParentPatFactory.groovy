package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PARENT

/**
 * Created by Ed Clark on 12/9/13.
 */


class ParentPatFactory extends SblBaseFactory {

    def ParentPatFactory() {
        name = 'PATPARENT'
    }
    def ParentPatFactory( String name) {
        this.name = name
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PARENT
        return ni
    }
}
