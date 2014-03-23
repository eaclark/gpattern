package gpattern

import jpattern.Pattern
import gpattern.factory.AbortFactory
import gpattern.factory.AnyFactory
import gpattern.factory.ArbFactory
import gpattern.factory.ArbnoFactory
import gpattern.factory.BalFactory
import gpattern.factory.BreakFactory
import gpattern.factory.BreakXFactory
import gpattern.factory.CancelFactory
import gpattern.factory.CharPatFactory
import gpattern.factory.DeferFactory
import gpattern.factory.FailFactory
import gpattern.factory.FenceFactory
import gpattern.factory.LenFactory
import gpattern.factory.BitwiseNegateFactory
import gpattern.factory.NSpanFactory
import gpattern.factory.NotAnyFactory
import gpattern.factory.OrFactory
import gpattern.factory.ParentPatFactory
import gpattern.factory.PlusFactory
import gpattern.factory.PosFactory
import gpattern.factory.PositiveFactory
import gpattern.factory.RemFactory
import gpattern.factory.RestFactory
import gpattern.factory.RootPatFactory
import gpattern.factory.RposFactory
import gpattern.factory.RtabFactory
import gpattern.factory.SetcurFactory
import gpattern.factory.SpanFactory
import gpattern.factory.StringPatFactory
import gpattern.factory.SucceedFactory
import gpattern.factory.TabFactory

/**
 * Created by Ed Clark on 12/9/13.
 */

public enum SblPrimitives {
    ABORT( new AbortFactory(), 'Abort', Pattern.&Abort),
    ANY( new AnyFactory(), 'Any', Pattern.&Any),
    ARB( new ArbFactory(), 'Arb', Pattern.&Arb),
    ARBNO( new ArbnoFactory(), 'Arbno', Pattern.&Arbno),
    BAL( new BalFactory(),'Bal', Pattern.&Bal),
    BREAK( new BreakFactory(),'Break', Pattern.&Break),
    BREAKX( new BreakXFactory(),'BreakX', Pattern.&BreakX),
    CANCEL( new CancelFactory(), 'Cancel', Pattern.&Cancel),
    CHARPAT( new CharPatFactory(), 'CharPat', Pattern.&CharPattern),
    DEFER( new DeferFactory(), 'Defer', Pattern.&Defer),
    FAIL( new FailFactory(), 'Fail', Pattern.&Fail),
    FENCE( new FenceFactory(), 'Fence', Pattern.&Fence),
    LEN( new LenFactory(), 'Len', Pattern.&Len),
    NOTANY( new NotAnyFactory(), 'NotAny', Pattern.&NotAny),
    NSPAN( new NSpanFactory(), 'NSpan', Pattern.&NSpan),
    POS( new PosFactory(), 'Pos', Pattern.&Pos),
    POSITIVE( new PositiveFactory(), 'Positive', Pattern.&Defer),
    REM( new RemFactory(), 'Rem', Pattern.&Rem),
    REST( new RestFactory(), 'Rest', Pattern.&Rest),
    RPOS( new RposFactory(), 'RPos', Pattern.&RPos),
    RTAB( new RtabFactory(), 'RTab', Pattern.&RTab),
    SETCUR( new SetcurFactory(), 'Setcur', Pattern.&Setcur),
    SPAN( new SpanFactory(), 'Span', Pattern.&Span),
    STRINGPAT( new StringPatFactory(), 'StringPat', Pattern.&StringPattern),
    SUCCEED( new SucceedFactory(), 'Succeed', Pattern.&Succeed),
    TAB( new TabFactory(), 'Tab', Pattern.&Tab),


    PLUS( new PlusFactory(), 'plus', null),
    OR( new OrFactory(), 'or', null),
    BITWISENEGATE( new BitwiseNegateFactory(), 'bitwiseNegate', null),
    PARENT( new ParentPatFactory(), 'parentPat', null),
    ROOT( new RootPatFactory(), 'rootPat', null)

    AbstractFactory factory
    String string
    def method

    SblPrimitives( f, s, m ) {
        this.factory = f
        this.string = s
        this.method = m
    }

    String toString() { string }
}