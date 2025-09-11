(ns fn-in.performance.dissoc-benchmarks
  "Benchmarks that measure `dissoc*` performance."
  (:require
   [clojure.test :refer [are
                         is
                         deftest
                         run-test
                         run-tests
                         testing]]
   [fastester.define :refer [defbench]]
   [fastester.display :refer [generate-documents]]
   [fastester.measure :refer [range-pow-10
                              run-benchmarks
                              run-manual-benchmark
                              run-one-defined-benchmark]]
   [fn-in.performance.benchmark-structures :refer [list-of-n-rand-ints
                                                   map-of-n-key-vals
                                                   max-hashmap-length
                                                   max-list-length
                                                   max-seq-length
                                                   seq-of-n-rand-ints
                                                   vec-of-n-rand-ints]]
   [fn-in.core :refer [dissoc*]]))


;; Sequences


;; `clojure.core/dissoc` doesn't work on sequences


(defbench
  test-dissoc*-seq
  "Sequences"
  (fn [n] (dissoc* (seq-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-dissoc*-seq :lightning)


;; Vectors


(defbench
  test-dissoc*-vec
  "Vectors"
  (fn [n] (dissoc* (vec-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-dissoc*-vec :lightning)


;; Lists


(defbench
  test-dissoc*-list
  "Lists"
  (fn [n] (dissoc* (list-of-n-rand-ints n) (dec n)))
  (range-pow-10 3))


#_(run-one-defined-benchmark test-dissoc*-list :lightning)


;; Hashmaps


(defbench
  test-dissoc-map
  "Hashmaps"
  (fn [n] (dissoc (map-of-n-key-vals n) (dec n)))
  (range-pow-10 max-hashmap-length))


(defbench
  test-dissoc*-map
  "Hashmaps"
  (fn [n] (dissoc* (map-of-n-key-vals n) (dec n)))
  (range-pow-10 max-hashmap-length))


#_(run-one-defined-benchmark test-dissoc-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest dissoc-dissoc*-benchmark-tests
  (are [structure benchmark-name n]
      (every? true? (map #(= (dec (count (seq-of-n-rand-ints %))) (count ((test-dissoc*-seq :f) %))) n))
    seq-of-n-rand-ints test-dissoc*-seq (range-pow-10 max-seq-length)
    vec-of-n-rand-ints test-dissoc*-vec (range-pow-10 max-seq-length)
    list-of-n-rand-ints test-dissoc*-list (range-pow-10 3)
    map-of-n-key-vals test-dissoc-map (range-pow-10 max-hashmap-length)
    map-of-n-key-vals test-dissoc*-map (range-pow-10 max-hashmap-length)))


#_(run-tests)

