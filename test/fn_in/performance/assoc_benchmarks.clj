(ns fn-in.performance.assoc-benchmarks
  "Benchmarks that measure `assoc*` performance."
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
   [fn-in.performance.benchmark-utils :refer [fn-then-get*]]
   [fn-in.core :refer [assoc*
                       get*]]))


;; Sequences


(defbench
  test-assoc-seq
  "Sequences"
  (fn [n] (assoc (seq-of-n-rand-ints n) (dec n) :benchmark-sentinel))
  (range-pow-10 max-seq-length))


(defbench
  test-assoc*-seq
  "Sequences"
  (fn [n] (assoc* (seq-of-n-rand-ints n) (dec n) :benchmark-sentinel))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-assoc-seq :lightning)


;; Vectors


(defbench
  test-assoc-vec
  "Vectors"
  (fn [n] (assoc (vec-of-n-rand-ints n) (dec n) :benchmark-sentinel))
  (range-pow-10 max-seq-length))


(defbench
  test-assoc*-vec
  "Vectors"
  (fn [n] (assoc* (vec-of-n-rand-ints n) (dec n) :benchmark-sentinel))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-assoc*-vec :lightning)


;; Lists


(defbench
  test-assoc*-list
  "Lists"
  (fn [n] (assoc* (list-of-n-rand-ints n) (dec n) :benchmark-sentinel))
  (range-pow-10 3))


#_(run-one-defined-benchmark test-assoc*-list :lightning)


;; Hashmaps


(defbench
  test-assoc-map
  "Hashmaps"
  (fn [n] (assoc (map-of-n-key-vals n) (dec n) :benchmark-sentinel))
  (range-pow-10 max-hashmap-length))


(defbench
  test-assoc*-map
  "Hashmaps"
  (fn [n] (assoc* (map-of-n-key-vals n) (dec n) :benchmark-sentinel))
  (range-pow-10 max-hashmap-length))


#_(run-one-defined-benchmark test-assoc-map :lightning)


#_(run-benchmarks "resources/assoc_options.edn")
#_(generate-documents "resources/assoc_options.edn")


;; Unit tests for benchmark functions


(deftest assoc-assoc*-benchmark-tests
  (are [benchmark-name target-sequence]
      (every? true? (map #(= :benchmark-sentinel (fn-then-get* (benchmark-name   :f) %)) target-sequence))
    test-assoc-seq (range-pow-10 max-seq-length)
    test-assoc*-seq (range-pow-10 max-seq-length)
    test-assoc-vec (range-pow-10 max-seq-length)
    test-assoc*-vec (range-pow-10 max-seq-length)
    test-assoc*-list (range-pow-10 3)
    test-assoc-map (range-pow-10 max-hashmap-length)
    test-assoc*-map (range-pow-10 max-hashmap-length)))


#_(run-tests)

