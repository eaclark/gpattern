package sblbuilder.factory
import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.ROOT

/**
 * Created by Ed Clark on 12/9/13.
 */


class BitwiseNegateFactory extends SblBaseFactory {

    def BitwiseNegateFactory() {
        name = 'rootPat'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {

        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.name = ni.toString()
        ni.type = ROOT
        ni.parent = null

        return ni
    }
}
