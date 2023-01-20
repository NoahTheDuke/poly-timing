(ns poly-timing.core
  (:require [criterium.core :refer (quick-bench)]
            [methodical.core :as m]
            [clojure.core.match :refer (match)])
  (:gen-class))

(defn value-case [n]
  (case n
    1 "1"
    2 "2"
    3 "3"
    4 "4"
    5 "5"
    6 "6"
    7 "7"
    8 "8"
    9 "9"
    10 "10"))

(defn value-cond [n]
  (cond
    (= n 1) "1"
    (= n 2) "2"
    (= n 3) "3"
    (= n 4) "4"
    (= n 5) "5"
    (= n 6) "6"
    (= n 7) "7"
    (= n 8) "8"
    (= n 9) "9"
    (= n 10) "10"))

(defmulti value-multi identity)
(defmethod value-multi 1 [n] "1")
(defmethod value-multi 2 [n] "2")
(defmethod value-multi 3 [n] "3")
(defmethod value-multi 4 [n] "4")
(defmethod value-multi 5 [n] "5")
(defmethod value-multi 6 [n] "5")
(defmethod value-multi 7 [n] "5")
(defmethod value-multi 8 [n] "5")
(defmethod value-multi 9 [n] "5")
(defmethod value-multi 10 [n] "5")

(m/defmulti value-methodical identity)
(m/defmethod value-methodical 1 [n] "1")
(m/defmethod value-methodical 2 [n] "2")
(m/defmethod value-methodical 3 [n] "3")
(m/defmethod value-methodical 4 [n] "4")
(m/defmethod value-methodical 5 [n] "5")
(m/defmethod value-methodical 6 [n] "6")
(m/defmethod value-methodical 7 [n] "7")
(m/defmethod value-methodical 8 [n] "8")
(m/defmethod value-methodical 9 [n] "9")
(m/defmethod value-methodical 10 [n] "10")

(defn value-match [n]
  (match [n]
         [1] "1"
         [2] "2"
         [3] "3"
         [4] "4"
         [5] "5"
         [6] "6"
         [7] "7"
         [8] "8"
         [9] "9"
         [10] "10"))

(def value-map
  {1 "1"
   2 "2"
   3 "3"
   4 "4"
   5 "5"
   6 "6"
   7 "7"
   8 "8"
   9 "9"
   10 "10"})

(defmulti type-multi class)
(defmethod type-multi String [x] "string")
(defmethod type-multi Long [x] "long")
(defmethod type-multi :default [x] "default")

(m/defmulti type-methodical class)
(m/defmethod type-methodical String [x] "string")
(m/defmethod type-methodical Long [x] "long")
(m/defmethod type-methodical :default [x] "default")

(defprotocol TypeProto
  (type-proto [_]))

(extend-protocol TypeProto
  String
  (type-proto [_] "string")
  Long
  (type-proto [_] "long")
  Object
  (type-proto [_] "default"))

(defmacro bench [s expr]
  `(do
     (println "\nBenchmarking" ~s)
     (quick-bench ~expr)))

(defn -main [& args]
  (println "## Value-based dispatch")
  (bench "case 1st" (value-case 1)) ; 7.089573 ns
  (bench "case 10th" (value-case 10)) ; 7.775557 ns
  (bench "cond 1st" (value-cond 1)) ; 3.726845 ns
  (bench "cond 10th" (value-cond 10)) ; 73.922515 ns
  (bench "multi 1st" (value-multi 1)) ; 31.488748 ns
  (bench "multi 10th" (value-multi 10)) ; 36.216900 ns
  (bench "methodical 1st" (value-methodical 1)) ; 97.625009 ns
  (bench "methodical 10th" (value-methodical 10)) ; 247.249630 ns
  (bench "match 1st" (value-match 1)) ; 4.091694 ns
  (bench "match 10th" (value-match 10)) ; 75.735722 ns
  (bench "map 1st" (value-map 1)) ;  27.031554 ns
  (bench "map 10th" (value-map 10)) ; 25.753022 ns

  (println "\n## Type-based dispatch")
  (bench "multi" (type-multi "abc")) ; 36.761352 ns
  (bench "multi default" (type-multi 1/2)) ; 29.768112 ns
  (bench "multi bi" (do (type-multi "abc") (type-multi 10))) ; 65.402393 ns

  (bench "methodical" (type-methodical "abc")) ; 228.833431 ns
  (bench "methodical default" (type-methodical 1/2)) ; 254.345562 ns
  (bench "methodical bi" (do (type-methodical "abc") (type-methodical 10))) ; 568.624490 ns

  (bench "proto" (type-proto "abc")) ; 5.282544 ns
  (bench "proto default" (type-proto 1/2)) ; 6.266890 ns
  (bench "proto bi" (do (type-proto "abc") (type-proto 10))) ; 19.087130 ns
  )
