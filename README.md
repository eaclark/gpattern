# Gpattern - Snobol4-Style Pattern Matching Primitives for Groovy

## Introduction

Gpattern is a library that tries to integrate Snobol style pattern matching functionality smoothly into Groovy programs.

Gpattern uses the Jpattern library written by [Dennis Heimbigner] (http://www.unidata.ucar.edu/staff/dmh/) to do the actual pattern matching.  Snobol patterns require a non-trivial state machine to handle all of the intricacies of matching on patterns that can do back tracking and be self-referential.  It is better to reuse an existing, running machine rather than try to build one from scratch.  (JPattern follows this advice itself in that it reuses the state machine logic of the [Ada Spitbol package] (http://gcc.gnu.org/onlinedocs/gcc-4.8.2/gnat_rm/The-GNAT-Library.html#The-GNAT-Library).

In essence, Jpattern does the heavy lifting while Gpattern pretties up the integration into Groovy.

## Status

This is the first release of this software and it should be considered **alpha** quality; I've only just begun putting it together.  While all of the tests I currently have pass, there has been no extensive shakedown of the code.  So don't use it in production code.

Having said that, it's probably at a point where it can be played with.  I would like get feedback on whether you think the approach I've take here looks promising to you.

## Download

One distribution JAR file is required to use Gpattern - *gpattern-dist-x.y.jar* - which is available at [this repository] (https://github.com/eaclark/gpattern).  It contains three files:  *gpattern.jar*, *jpattern.jar*, and *SblMatchDSL.jar* that need to be extracted and placed somewhere on you classpath.

The [source and some tests](https://github.com/eaclark/gpattern) are there as well.

If you want to play around with JPattern directly, then you can get it from [Dennis Heimbigner's software page] (http://www.unidata.ucar.edu/staff/dmh/software.html).  Dennis maintains the [source for JPattern at github](https://github.com/Unidata/jpattern).

## Integration into Groovy

The semantics of string in Groovy is such that the individual characters and substrings of a given string can be accessed using an array notation:  str1[ 3] to get at the fourth character or str1[0..3] to get at the substring comprising the first four characters.  (And, of course, the indexing may be done by a variable that holds the numeric value or range rather than 

Gpatterns extends this indexing approach by allowing for the use of a pattern to provide the index.  This means that the core Snobol pattern construct:

<code>Snobol>>  SUBJECT PATTERN</code>

for matching the PATTERN to the SUBJECT string becomes with Gpattern:

<code>Groovy>>  SUBJECT[ PATTERN]</code>

And, the more complex Snobol replacement construct:

<code>Snobol>>  SUBJECT PATTERN = REPLACEMENT</code>

becomes with Gpattern:

<code>Groovy>>  SUBJECT[ PATTERN] = REPLACEMENT</code>

Gpattern even handles the case where the replacement side also has a pattern match.

<code>Snobol>>  TARGET PATTERN1 = SOURCE PATTERN2</code>

becomes:

<code>Groovy>>  TARGET[ PATTERN] = SOURCE[ PATTERN2]</code>

### Structure

* SblBuilder

  The SblBuilder provides a set of factory methods for constructing the various primitive nodes used in a pattern.
  
* SblMatchContext
  
  The SblMatchContext provides a run time repository for the patterns and variables as they are constructed and used during pattern matching.  As a given pattern is being executed, any needed pattern or variable name resolution is done through the containing SblMatchContext.  Note that more than one SblMatchContext can be instantiated at the same time, and their patterns are independent of each other.

* SblPatNode
 
  SblPatNodes are built at run time by the factory methods provided by SblBuilder and they hold the matching primitive information.  E.g., Break('aeiou') will be turned into a pattern node that knows the break primitive is to be used with an argument of 'aeiou'.  In addition, the SblPatNodes have the 'plus' and 'or' methods used to compose primitives into larger patterns through concatenation and alternation.  SblPatNodes also provide the methods used to do in pattern assignments.  (More on assignments in a bit.)
  
* SblString

  A SblStrings hold the subject string and it provides the 'getAt' and 'putAt' methods that implement the string array indexing semantics.
  
One of the powerful capabilities of a Snobol pattern match is to do internal assignments as part of the matching process.  This means that information gleaned during the matching process can be used as a later part of the same match or as part of the final replacement.

Consider a snippet of Snobol code:

```
      rainbow = 'red,orange,yellow,green,blue,indigo,violet'
      cycle = break(',') . item len(1) rem . rest
      rainbow cycle = rest item ','
```

This function of this snippet is to rotate the first word from the head of the string and appends it to the end of the string.  

The first line defines variable 'rainbow' that holds the string that is used as the subject of the match.

The second line defines the 'cycle' variable that holds the pattern.  This pattern breaks on a comma and assigns what is before the comma to the 'item' variable.  It then skips over the comma and assigns the remainder of the string to the 'rest' variable.

The third line does the pattern match on the left hand side of the equals sign - includign the population of "item" and "rest" - then builds the replacement string using those variables (plus a new comma).

The equivalent Gpattern code is:

```
      rainbow = new SblString('red,orange,yellow,green,blue,indigo,violet')
      cycle = Break(',').ca('item') + Len(1) + Rem().ca('rest')
      rainbow[ cycle]
      rainbow = rest + ',' + item
```

Note, the third line of the Snobol code had to be split into two lines because of a bug in today's Groovy compiler.  When this gets fixed, those two lines can be <code>rainbow[ cycle] = rest + ',' + item</code>.

Also note, Gpattern handles doing immediate assignments in addition to conditional assignments.

### Mechanism

The JPattern library uses a two step process for building working patterns.  As a first step, JPattern uses a separate "compiler" to parse a very Snobol-esque pattern definition and generate an equivalent set of Java method calls.  As a second step, this set of Java code is then compiled by the normal Java compiler.

The Gpattern library, on the other hand, uses an AST transformation together with the MOP and builder technologies of the Groovy world to blend these two steps into one compiler step.  (Granted, it's a rather complex single step, but the Groovy compiler handles it automagically.)

Putting the above snippet into a complete program, running this in a groovyConsole (after adding the two needed JAR files to the classpath...):


```
  import gpattern.SblBuilder
  import gpattern.SblMatchContext
  import gpattern.SblString

  SblBuilder builder = new SblBuilder()
  SblMatchContext matchCtx = builder.matchContext()

  matchCtx.with {
      rainbow = new SblString('red,orange,yellow,green,blue,indigo,violet')
      cycle = Break(',').ca('item') + Len(1) + Rem().ca('rest')
      rainbow[ cycle]
      rainbow = rest + ',' + item
      println rainbow
      rainbow[ cycle]
      rainbow = rest + ',' + item
      println rainbow
  }
```

results in the following output:

```
orange,yellow,green,blue,indigo,violet,red
yellow,green,blue,indigo,violet,red,orange
```

### Caveats

Because of the way that Gpattern is implemented, there are a few caveats that you'll run into.  Part of releasing Gpattern now is to get a feel for whether these issues are too much.  Another part is see if someone out there can see away around one or more of them.

#### Current Groovy compiler bug

This issue is painful, but hopefully it is temporary.

Because of the order that Groovy evaluates operands in an expression like:

`subject[ pattern] = replacement`

Currently, Groovy evaluates the "replacement" portion before the "pattern" portion.  This breaks any Snobol like pattern where the "pattern" evaluation is suppose to have side effects that are used in the "replacement".

One example of such a pattern appears in the program above.  There, the "cycle" pattern used in the line:

```      rainbow[ cycle] = rest + ',' + item```

has the side effect of assigning values to the "rest" and "item" variables.  In Gpattern it currently has to be broken into two lines:

```
    rainbow[ cycle]
    rainbow = rest + ',' + item
```
      
While this works for this program, splitting lines won't work for all replacement patterns.  Hopefully, this is only a temporary problem.

#### Patterns with explicit Strings or GStrings

Unfortunately, patterns that use strings (both String and Gstring) directly have to be "signaled" by using the "~" unary operator at the start of the string.  This means that a Snobol statement like:

`subject ('s' | 'es' | '')`

has to become:

`subject[ ~('s' | 'es' | '')]`

Internally, an AST transformation changes the above expression into

`subject[ ~(StringPat('s') | StringPat('es') | StringPat(''))]`

If you wish to avoid the use of the transformation, then the StringPat() method calls can be used.



Similarly, a GString can be used if the pattern is prefixed with a "~":

```
    color = 'red'
    // strip a color from the rainbow
    rainbow[ ~"$color,"] = ''
```


#### Unevaluated pattern nodes are methods

Another bit of painful syntax is how expressions are marked as being unevaluated.  In Snobol, this is done using the '*' character.  An example pattern might be:

`Snobol>>  tab(*I) span(*S)`

where the lookup for variables I and S are done at runtime rather than compile time.

In JPattern, the equivalent would be:

`JPattern>>  @tab(+I) span(+S)@`

(Note, the "@" characters are the default begin/end pattern markers used by the JPattern preprocessor to delineate patterns.)

While in Gpattern, it is:

`Gpattern>>  Tab( Defer('I')) + Span( Defer('S'))`

Note that the variables "I" and "S" have to be referenced by name rather than directly.

It would have been nice to overload the "positive" method (the "+" unary operator) to be syntatically simimlar to JPattern, but this runs into the same issues for String and GString as mentioned above.  Likewise for using AST transformations.

#### Internal assignments are methods

This caveat is not so much of an issue as it is a syntatic departure from how Snobol (and JPattern) code handles immediate and conditional assignments within a pattern.  Consider the following Snobol pattern that uses a conditional assignment:

`pat = ':' len(n) . item`

This pattern matches a ":" character and then grabs the next "n" characters and assigns it to the variable "item".

The equivalent Gpattern code is:

`pat = StringPat(':') + Len(n).ca('item')`

Notice that the binary operator "." for Snobol is replaced by a "ca" method call on the SblPatNode ("Len" in this case).

In a similar fashion, the binary Snobol operator for immediate assignment ("$") is replaced by a "ia" method call.  So the pattern match:

`subject (len(n) arb) $ output span('aeiou')`

becomes:

`subject[ (Len(n) + Arb()).ia('output') + Span('aeiou')]`

(Here, the "ia" method is called on the SblPatNode returned by the concatination operation done by the "+" binary operator.)

#### Null as FAilure

The last caveat I'll mention here has to do with the fact that Snobol programmers are use to the success or failure of a pattern match being signalled 'outside' of the pattern itself.  That outside channel is not available with Groovy.

Gpattern tries to mimic this signalling by having pattern matches always return a String when successful.  The string might by the empty string, but it will be a String.

On failure, a Gpattern match will return a "null".

This gives the programmer a way to differentiate between success and failure when matching.  For example, one might code:

```
if( subj[ pat] != null) success()
else failure()
```

This runs into problems, however, when tying to look for success/failure with a replacment match.  Unfortunately, this doesn't work:

```
if( (subj[ pat] = 'a') != null) success()
else failure()
```

This can be gotten around by using the "putAt" method directly:

```
if( subj.putAt( pat, 'a') != null) success()
else failure()
```
Clumsy, but it (should) work.

### Enhancements that go beyond Snobol

Fortunately, in contrast to the caveats, some nice extensions that go beyond the basic Snobol pattern matching capabilities are available.

#### External variables and functions

The first one is one that is provided by the JPattern library itself, and that is the ability for the programmer to add new operations that can be used in conjunction with the set of basic Snobol primitives.  They can even be written to be used in patterns that do backtracking.

This is not unique to JPattern (e.g., Snobol4+ from Catspaw and CSNOBOL4 written by Phil Budne also do this), but it does allow the user to extend the basic platform easily.

One interesting example of this that JPattern provides is adding a "primitive" that lets the user include pattern expressions that use RE syntax.

#### In-pattern replacements

Another capability present in the JPattern library (and even in the Ada module after which it was fashioned) that goes beyond what is available in Snobol is the ability to do replacements as part of the individual component matches.  For example, the JPattern statement:

`Pattern p = @(len(1) $ x) & (len(1) = x)@;`

shows a pattern that does an internal replacement on the second "len" component.  Note, though, that this replacement is conditional.  That is, it only takes place if the entire pattern match succeeds.

This is also available in Gpattern, but it is done in a way that parallels the approach taken with immediate and conditional assignments - a method call ("cr") on the pattern node is used in place of the binary "=" operator.
The Gpattern pattern definition equivalent to the JPattern one above is:

`p = Len(1).ia( 'x') + Len(1).cr( 'x')`

#### Collection methods for SblStrings and patterns

One of the nice features of Groovy - like many modern languages - is it provides a set of methods for processing collections.  Gpattern provides some equivalent methods for handling multiple potential matches of a pattern on a subject.

In Snobol, this can be approached by taking the desired pattern and appending the "fail" primitive to the end of the pattern.  The "fail" primitive will force the pattern to backtrack after each success to find the next possible match.  And, by using immediate assignment, you can try to capture each successive result.  Unfortunately, I know of only one "variable" in Snobol that can capture multiple values being assigned to it - "OUTPUT".

While this is useful for the matches to the terminal or a file, it isn't all that flexible.

It's the external variables provided by JPattern mentioned above that provides the extra magic to let Gpattern have a couple of nice methods for iterating a pattern over a SblString.

  * collect( pattern)

    The SblString "collect" method takes a pattern and creates a list of all of the possible matches of that pattern on the SblString.  For example:
  
```
            str = new SblString('xy[ab{cd}]')
            Element = NotAny("[]{}") |
                      (StringPat('[') +  Defer( 'Balanced_String') + StringPat(']')) |
                      (StringPat('{') +  Defer( 'Balanced_String') + StringPat('}'))
            Balanced_String = Defer('Element') + Arbno(Defer('Element'))

            str.collect( Balanced_String)
```

    creates this list: [ 'x', 'xy', 'xy[ab{cd}]', 'y', 'y[ab{cd}]', '[ab{cd}]', 'a', 'ab', 'ab{cd}', 'b', 'b{cd}', '{cd}', 'c', 'cd', 'd']
    
  * collect( pattern) { closure }  
  
    This sister collect method also creates a list, but it applies the closure to each element of the list first:

  
```
            str = new SblString('((A+(B*C))+D)')
            str.collect( Bal()) { String s -> s.toLowerCase() } 

```

        generates the list ['((a+(b*c))+d)', '(a+(b*c))', '(a+(b*c))+', '(a+(b*c))+d', 'a', 'a+', 'a+(b*c)', '+', '+(b*c)', '(b*c)', 'b', 'b*', 'b*c', '*', '*c', 'c', '+', '+d', 'd']
        
  * each( pattern) { closure }
  
    The each method uses the pattern to iterate through the SblString and applies the closure to each match.  (Note, it does not collect the results.)

In addition to these three methods, SblMatchContext also provides a `getCollection` method that can be used to do some low level grouping of data.  `getCollection` returns two items - the first is a (reference to a) list that will be filled if used by a pattern and the second is a name that is to be used during that pattern to indicate this collection.

For example, if you happened to have an old dataset of position based records, then you could parse one of those records by using getCollection and Tab:

```
            str1 = new SblString('   William T. Miller         IND   Collingswood')
            ( cv, cn) = getCollection( { String s -> s.trim() })
            str1[ Tab(3) + Tab(29).ca(cn) + Tab(35).ca(cn) + RTab(0).ca(cn)]
```

This will generate the list:   ['William T. Miller', 'IND', 'Collingswood'].
