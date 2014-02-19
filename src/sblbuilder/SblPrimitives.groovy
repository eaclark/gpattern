package sblbuilder

import jpattern.Pattern
import sblbuilder.factory.AbortFactory
import sblbuilder.factory.AnyFactory
import sblbuilder.factory.ArbFactory
import sblbuilder.factory.ArbnoFactory
import sblbuilder.factory.BalFactory
import sblbuilder.factory.BreakFactory
import sblbuilder.factory.BreakXFactory
import sblbuilder.factory.CancelFactory
import sblbuilder.factory.CharPatFactory
import sblbuilder.factory.DeferFactory
import sblbuilder.factory.FailFactory
import sblbuilder.factory.FenceFactory
import sblbuilder.factory.LenFactory
import sblbuilder.factory.BitwiseNegateFactory
import sblbuilder.factory.NSpanFactory
import sblbuilder.factory.NotAnyFactory
import sblbuilder.factory.OrFactory
import sblbuilder.factory.ParentPatFactory
import sblbuilder.factory.PlusFactory
import sblbuilder.factory.PosFactory
import sblbuilder.factory.PositiveFactory
import sblbuilder.factory.RemFactory
import sblbuilder.factory.RestFactory
import sblbuilder.factory.RootPatFactory
import sblbuilder.factory.RposFactory
import sblbuilder.factory.RtabFactory
import sblbuilder.factory.SetcurFactory
import sblbuilder.factory.SpanFactory
import sblbuilder.factory.StringPatFactory
import sblbuilder.factory.SucceedFactory
import sblbuilder.factory.TabFactory

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