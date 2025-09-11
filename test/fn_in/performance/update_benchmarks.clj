(ns fn-in.performance.update-benchmarks
  "Benchmarks that measure `update*` performance."
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
   [fn-in.core :refer [update*
                       get*]]))


;; Sequences


(defbench
  test-update-seq
  "Sequences"
  (fn [n] (update (seq-of-n-rand-ints n) (dec n) inc))
  (range-pow-10 max-seq-length))


(defbench
  test-update*-seq
  "Sequences"
  (fn [n] (update* (seq-of-n-rand-ints n) (dec n) inc))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-update-seq :lightning)


;; Vectors


(defbench
  test-update-vec
  "Vectors"
  (fn [n] (update (vec-of-n-rand-ints n) (dec n) inc))
  (range-pow-10 max-seq-length))


(defbench
  test-update*-vec
  "Vectors"
  (fn [n] (update* (vec-of-n-rand-ints n) (dec n) inc))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-update-vec :lightning)


;; Lists


(defbench
  test-update*-list
  "Lists"
  (fn [n] (update* (list-of-n-rand-ints n) (dec n) inc))
  (range-pow-10 3))


#_(run-one-defined-benchmark test-update*-list :lightning)


;; Hashmaps


(defbench
  test-update-map
  "Hashmaps"
  (fn [n] (update (map-of-n-key-vals n) (dec n) inc))
  (range-pow-10 max-hashmap-length))


(defbench
  test-update*-map
  "Hashmaps"
  (fn [n] (update* (map-of-n-key-vals n) (dec n) inc))
  (range-pow-10 max-hashmap-length))


#_(run-one-defined-benchmark test-update-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest update-update*-benchmark-tests
  (are [structure benchmark-name n]
      (every? true? (map #(= (inc (get* (structure %) (dec %))) (fn-then-get* (benchmark-name :f) %)) n))
    seq-of-n-rand-ints test-update-seq (range-pow-10 max-seq-length)
    seq-of-n-rand-ints test-update*-seq (range-pow-10 max-seq-length)
    vec-of-n-rand-ints test-update-vec (range-pow-10 max-seq-length)
    vec-of-n-rand-ints test-update*-vec (range-pow-10 max-seq-length)
    list-of-n-rand-ints test-update*-list (range-pow-10 3)
    map-of-n-key-vals test-update-map (range-pow-10 max-hashmap-length)
    map-of-n-key-vals test-update*-map (range-pow-10 max-hashmap-length)))


#_(run-tests)

