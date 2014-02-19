package sblbuilder.factory

import jpattern.Variable
import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.SETCUR

/**
 * Created by Ed Clark on 12/9/13.
 */


class SetcurFactory extends SblBaseFactory {

    def SetcurFactory() {
        name = 'SETCUR'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        if (value.class == String) {
            value = Variable.create( value)
        }
//        } else if (value.class == Closure) {
//            value = new Function() {
//                @Override
//                Object get(VarMap vars) {
//                    value()
//                }
//            }
//        }
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = SETCUR
        return ni
    }
}
