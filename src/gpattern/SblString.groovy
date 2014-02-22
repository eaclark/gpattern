package gpattern

import jpattern.ExternalVariable
import jpattern.VarMap

/**
 * Created by Ed Clark on 12/9/13.
 */

class SblString implements ExternalVariable {
    private StringBuilder _is    // internal string
    private SblMatchContext _rc  // internal root match context

    SblString() {
        setValue( '')
    }

    SblString( str) {
        setValue( str)
    }

    String getValue() {
        return _is.toString()
    }

    String setValue( str) {
        _is = new StringBuilder( str)
    }

    def getAt( SblPatNode pat) {
        boolean ok
        String matched
        int start, stop

        // first, try to compile the pat
        pat = ~pat
        SblMatchContext context = pat.context

        context.matcher.subject = _is.toString()
        context.matcher.varMap = context.vars
        ok = context.matcher.match()
//        ok = context.matcher.Match( _is.toString(), context.vars, context.extVars)

        if( ok) {
            start = context.matcher.start
            stop = context.matcher.stop
            // can't use the _is string because the matching may have altered the subject
            matched = context.matcher.subject.substring( start, stop)
            // if the matching did alter the subject string, then we also need to update _is
            if( _is != context.matcher.subject) _is[ 0 .. -1 ] = context.matcher.subject
        }
        return  ok ? matched : null
    }

    def putAt( SblPatNode pat, String str) {
        String front, middle, end
        int start, stop
        boolean ok

        // first, try to compile the pat
        pat = ~pat
        SblMatchContext context = pat.context

        context.matcher.subject = _is.toString()
        context.matcher.varMap = context.vars
        ok = context.matcher.match()

//        ok = context.matcher.Match( _is.toString(), context.vars, context.extVars)
        if( ok) {
            context.matcher.Replace( _is, str, context.matcher.start, context.matcher.stop)
            return str
        } else return null
    }

    def putAt( SblPatNode pat, Closure cls) {
        boolean ok

        // first, try to compile the pat
        pat = ~pat
        SblMatchContext context = pat.context

        context.matcher.subject = _is.toString()
        context.matcher.varMap = context.vars
        ok = context.matcher.match()

//        ok = context.matcher.Match( _is.toString(), context.vars, context.extVars)
        if( ok) {
            //call the closure to resolve the string value
            def str = cls()
            context.matcher.Replace( _is, str, context.matcher.start, context.matcher.stop)
            return str
        } else return null
    }

    def collect( SblPatNode pat) {
        SblMatchContext context = pat.context
        ArrayList cv
        String cn

        (cv, cn)  = context.getCollection()
        SblPatNode newPat = pat.ia( cn) + context.'Fail'()
        this[ newPat]
        context.releaseCollection( cn)
        return cv
    }

    def collect( SblPatNode pat, Closure clos) {
        SblMatchContext context = pat.context
        ArrayList cv
        String cn

        (cv, cn) = context.getCollection( clos)
        SblPatNode newPat = pat.ia( cn) + context.'Fail'()
        this[ newPat]
        context.releaseCollection( cn)
        return cv
    }

    def each( SblPatNode pat, Closure clos) {
        SblMatchContext context = pat.context
        String name = UUID.randomUUID().toString()

        context.registerVariable( name,
                                  [ get : { vars -> },
                                    put : { vars, val -> clos( val) }
                                  ] as ExternalVariable )

        SblPatNode newPat = pat.ia( name) + context.'Fail'()
        this[ newPat]
        context.vars.remove( name)
    }

    String toString() {
        _is.toString()
    }

    Object get(VarMap vars) {
        toString()
    }

    void put(VarMap vars, Object value) {
        _is = new StringBuilder( value.toString())
    }

    String lpad( int len, String str = ' ') {
        int curLen = _is.length()
        if( curLen < len) _is = new StringBuilder() << (str[0..0]*(len-curLen)) << _is
        return _is
    }

    String rpad( int len, String str = ' ') {
        int curLen = _is.length()
        if( curLen < len) _is.append( str[0..0] * (len - curLen) )
        return _is
    }

    SblString reverse() {
        return new SblString( _is.toString().reverse() )
    }

    SblString plus( SblString str2) {
        _is.append( str2._is)
        return this
    }

    int length() {
        _is.length()
    }
}

