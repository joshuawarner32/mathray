# Mathray Design

## Core Principles

The representation of Mathray values is very abstract: they can be interpreted pretty freely by transformers.  The types of the values that are computed on is left completely unspecified, except in providing a common "Rational" constant type.  Single-precision floating point numbers, local-variable indices for Java code emission, Strings, fragments of OpenCL code, ... are all perfectly valid computational types.

<code>Values</code> are broken into three types:

* <code>Call</code> hold a <code>Function</code> reference and corresponding argument <code>Values</code>.
* <code>Rational</code> represents a fraction.  This is the simplest type that practically all transformations can agree on a meaning for.  This is the only "constant" type that Mathray provides - more complex constants are either represented as <code>Symbols</code>, or left unevaluated.
* <code>Symbol</code> represents a bound or unbound variable.  These are used both for function parameter references and named constants.

To keep the exact computational type unspecified, <code>Functions</code> (even core operators like ADD, SUB, MUL and DIV) don't have any inherent meaning.  Functions are nothing more than symbolic references.  Their implementation is left completely up to each transformation pass.  Where more concrete definitions of functions are needed (for example, for user-defined functions), <code>Definitions</code> / <code>Multidefs</code> or the more general <code>Lambdas</code> are used.

Mathray is built around a composable set of tranformations:

* <code>ParseInfo</code> holds information about how function calls should be rendered to text, and parsed back (for instance, precedence, string "names", etc).
* <code>Derivatives</code> handles computing derivatives of values w.r.t. specific <code>Symbols</code>.  This transformation uses the simplest possible rules (it knows only about the derivatives of functions and the chain rule) and doesn't attempt to do any simplification; thus factors of 0 and 1 are very common.
* <code>Simplifications</code> handles basic simplification transformations such as combining rationals, eliminating terms with factors of 0, computing known values of <code>Functions</code> (such as "sin(0)"), partial constant folding over limited <code>Functions</code>
* <code>MachineEvaluator</code> uses <code>doubles</code> and a simple <code>Processor</code> to compute actual return values for <code>Definitions</code>
* <code>JavaCompiler</code> uses [ASM](http://asm.ow2.org/) to transform <code>Definitions</code> or <code>Lambdas</code> to Java methods that compute concrete function values, given the parameters.
* <code>ComplexTransform</code> changes an expression over the reals to an expression that computes both the real and imaginary components of the same value.
* <code>IntervalTransform</code> transforms an expression into the same expression computed with Interval Arithmetic.
* <code>Project</code> takes <code>Multidefs</code> and returns <code>Lambda&lt;Multidef&gt;s</code>, where the extra parameters bound in the lambda represent the n-dimensional perspective projection tranform.  This allows ray-tracing to proceed in a very simple coordinate space.

The visitor pattern is everywhere.  <code>Visitors</code> never return values, but simple utility methods can turn a <code>Processor</code> - which returns it's own value at each stage in the computation, and takes as input results of recursive invocations - into a <code>Visitor</code>.  "toStringing" a value is implemented as a <code>Processor</code>, which recursively transforms each Value into a <code>PrecedenceString</code> to enable inserting proper parentheses.

## Plotting

In the following, I give denote the dimensionality of various objects as the number of parameters (variables) in their algebraic represenation.

### 1D Cartesian Function

The case of plotting a 1D function is (relatively) straight-forward.  Unfortunately, just evaluating the function at a bunch of evenly-spaced points (which is what's currently implemented) isn't sufficient.  For cases like "sin(1/x)", this leads to artifacts near 0 because the function oscilates with a frequency that rapidly approaches infinity.  Somehow, some form of inclusion arithmetic needs to be folded in, to decide where the function needs to be sampled more frequently (and, perhaps, where it can be sampled less frequently).

### 2D Cartesian Equation

There are lots of options for plotting general 2D equations (such as "y^2(y^2-a^2)=x^2(x^2-b^2)", the [Devil's Curve](http://en.wikipedia.org/wiki/Devil's_curve))

* Evaluating the difference between the two sides of the equation, and plotting the sign changes.  At its simplest, we can just evaluate at every pixel, and use black (or line-colored) pixels to indicate a sign change.  This is the approach that the very earliest incarnation of this math-plotting software took.  This can lead to artifacts when when there are two lines of solutions that get very close togethor (as there is no sign change, then).  This also leads to a very pixelated ouput.
* Using interval arithmetic (or generally, inclusion arithmetic) to evaluate the function over the full range of every pixel.  Color pixels where the output interval contains 0.  This causes overly fat lines where the inclusion arithmetic dramatically over-estimates the real output interval, as well as pixelation artifacts.  Works well enough for simple equations.
* Use a quad-tree and inclusion arithmetic to narrow down the areas that a function has zeros, then (somehow...) extract line features from this data.  Use a traditional graphics API to plot the resulting line features.

Of these, the third approach is the most appealing. The second is currently implemented.

### 2D Cartesian Function

This could either be evaluated ala 1D functions and plotted with a traditional 3D rasterizer, or raytraced as a complete 3D equation surface.

### 3D Cartesian Equation

There are a couple of options:

* Traditional raytracing, using inclusion arithmetic and binary search to find intersections.  This has the down-side of giving fuzzy edges (if the rays are randomly distributed in the pixels.  It can also miss certain thin features, such as the infinitely-thin spindle on the "kiss" surface.
* "Box"-tracing, using inclusion arithmetic and binary search.  This has the advantage of never missing thin features.  It also has the potential to be significantly faster, as the boxes cover many ray evaluations at onces - boxes can eliminate vast swaths of space, without having to check each of the potentially thousands of rays moving through.  Experimentally, and in very rough numbers, this approach uses about 1/3 of the evaluations of the first approach.

## Future Work

### Integrals (and other complex computations)

Right now, it would be easy to implement a generic numerical integrator that takes a <code>FunctionD</code> (a function on Java <code>doubles</code>) and an interval, and returns (an approximation of) the definite integral.  The problem with this is that this only has meaning on the host platform.  You couldn't swap out the <code>JavaCompiler</code> for an <code>OpenCLCompiler</code>, and expect to be able to compute integrals on the GPU, as you could if integrals were a feature of the Value structure itself.

#### Convergences

Integrals could be represented by an abstract infinite loop ("<code>Convergence</code>") that "terminates" at the converged variable values.  All that is need is to plug in your favorite numerical integration algorithm.  The tricky part is getting this to integrate (no pun intended) well with transformation passes, so (for instance) IntervalTransform could return a lower- and upper- bound on the integral.

#### Kernels

Another possibility is to abstract over the "outer" computational system.  Integration, ray-tracing, and other plotting activities would all be <code>Kernels</code>.  <code>Kernels</code> would consist of functions to evaluate and logic to flow results of one computation into the next.  Ideally, <code>Kernel</code> transformations would be completely composable (hopefully also producing efficient algorithms) - so that, for instance, you could apply the "integrate" <code>Kernel</code> transform, followed by the "ray-trace" transform, and get the expected results.

### Tagging Constant Values

The JavaCompiler, for instance, doesn't know that in the function definition "f(x) = x * sin(2)", "sin(2)" doesn't depend on any of the arguments and can thus be constant-folded into the generated Java class.

### Floating-Point Constants in Parser

The Parser doesn't currently handle floating-point constants.  It should, by converting them to fractions.

### Affine Arithmetic (or other similar systems)

Affine arithmetic is promising not only because it offers a better convergence rate (error ~ O(h^2) vs O(h) for interval arithmetic), but also because it provides an approximation of the function in the input space.

An interesting subset of affine arithmetic that would be good to explore is that which yields linear approximations (with guaranteed bounds).  Call this "linear interval arithmetic." Note that the output arity of resultant function will be n + 2, where the input arity is n.  Compare this with 2 for interval arithmetic.  The output arity for affine arithmetic is dependent on the complexity of the output, not the input arity.  Linear approximations should be more useful, and nearly as efficient as the zonotopes produced by affine arithmetic.

### Plotting

Plotting should be simple and intuitive, and deal gracefully with even the most complicated cases without artifacts.

It'd be cool to (at least, under some flag) plot true multivalued functions, like sqrt(x) and asin(x), respecting the multivalued-ness.  For simple cases like "y=asin(x)", this can be solved by unwrapping the inverse functions - essentially transforming that into "sin(y)=x", but this approach is not completely general.  Difficulties occur when combining multivalued outputs, as in "sqrt(x)+sqrt(x)". Can sqrt(x) take on two separate values in this case (and yield a result of 0)?  This contradict traditional arithmetic, where "sqrt(x)+sqrt(x)" = "2sqrt(x)", which can never be zero except where x is 0.  What about the case of "sqrt(x)+sqrt(2x)"?  I'm not aware of any mathematical theory that covers this.

