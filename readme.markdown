# Mathray

![Kiss Function](/example.png?raw=true "Kiss Function")

`x^2 + y^2=(1 - z)*z^4`

![Clebsch Surface](/example-2.png?raw=true "Clebsch Surface")

```
81*(x^3 + y^3 + z^3)
- 189*(x^2*y + x^2*z + y^2*x + y^2*z + z^2*x + z^2*y)
+ 54*x*y*z
+ 126*(x*y + x*z + y*z)
- 9*(x^2 + y^2 + z^2)
- 9*(x + y + z)
+ 1 = 0
```


Mathray is a general-purpose high-quality mathematical plotter.

## Getting Started

Mathray requires java and maven to be installed.

Mathray can be run in one of two ways:

* For *nix platforms, use the ./mathray driver script
* For other platforms, pay attention to the java command in the script.  Use <code>dependency:build-classpath</code> to find the appropriate classpath.  The final command will be something like:

<code>java -cp target/mathray-0.01-SNAPSHOT.jar:{big_long_maven_classpath} mathray.cli.Main</code>

The usage statement is pretty self-explanatory:

<pre>
usage: mathray "<equation or function>"
 -h,--height <height>   height of output in pixels
    --help              print this message
    --output <file>     file to write result to
 -w,--width <width>     width of output in pixels
    --xa <value>        lowest (left-most) x value visible in output
    --xb <value>        highest (right-most) x value visible in output
    --ya <value>        lowest y value visible in output (near bottom)
    --yb <value>        highest y value visible in output (near top)
</pre>

### Examples

Specifying a function in x causes mathray to plot using a very simple and fast function plotter:

<code>./mathray "sin(x)"</code>

<code>./mathray "log(x)+2/3*sin(2x * tau)"</code>

Notice the "tau" constant, the _real_ circle constant (see [tauday.com](http://www.tauday.com)).  Mathray also supports plain-old pi and e.

Specifying an equation in x and y causes mathray to plot using an interval arithmetic solver:

<code>./mathray "y^2*(y^2-(8/10)^2)=x^2*(x^2-1)"</code>

Specifying an equation in x, y, and z causes mathray to plot using a 3D ray-tracer (or a direct analog thereof):

<code>./mathray "x^2+y^2=(1-z)*z^4"</code>
