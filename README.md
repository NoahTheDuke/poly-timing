# poly-timing

Minimal library to benchmark performance of value and type based dispatch.

Also see: http://insideclojure.org/2015/04/27/poly-perf/

## Tests

* Value-based dispatch - tests dispatching by value to both the first and fifth case based on case, cond, multimethods, [methodical multimethods](https://github.com/camsaul/methodical), core.match, and a plain map. Multimethods do a linear search through the cases for the best match, so you will see that timings are about the same regardless. Other examples will bail out on the first match, so have faster first match timings.
* Type-based dispatch - tests dispatching by type via multimethods, methodical multimethods, and protocols. Protocols are definitely faster - while this gap has narrowed over the years, it's always been significantly faster which is why protocols are preferred for type-based dispatch. The default case timings are also included (multimethod default was really bad in older Clojure versions).
* Bimorphic type dispatch - tests dispatching when there are two common cases that are active

## To run

```lein do clean, run```

## Example timings

Clojure 1.11.1, Temurin Java 19, 2018 Macbook Pro (Intel)

Value-based dispatch

|           | case   | cond    | multimethod | methodical | core.match | map     |
| --------- | ------ | ------- | ----------- | ---------- | ---------- | --------|
|  1st case | 7.0 ns | 3.7 ns  | 31.4 ns     | 97.6 ns    | 4.0 ns     | 27.0 ns |
| 10th case | 7.7 ns | 73.9 ns | 36.2 ns     | 247.2 ns   | 75.7 ns    | 25.7 ns |

Type-based dispatch

|              | multimethod | methodical | protocol |
| ------------ | ----------- | ---------- | -------- |
| match case   | 36.7 ns     | 228.8 ns   | 5.2 ns   |
| default case | 29.7 ns     | 254.3 ns   | 6.2 ns   |
| bimorphic    | 65.4 ns     | 568.6 ns   | 19.0 ns  |
