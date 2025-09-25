(ns fn-in.performance.assoc-in-benchmarks
  "Benchmarks that measure `assoc-in*` performance."
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
   [fn-in.performance.benchmark-structures :refer [max-in
                                                   max-list
                                                   narrow-deep-vec
                                                   nested-list
                                                   nested-map
                                                   nested-seq
                                                   nested-vec
                                                   n-levels
                                                   path-list
                                                   path-map
                                                   path-narrow-deep-vec
                                                   path-nested-vec
                                                   path-seq]]
   [fn-in.performance.benchmark-utils :refer [fn-then-get-in*
                                              fn-then-get-in*-2]]
   [fn-in.core :refer [assoc-in*
                       get-in*]]))


;; Sequences


;; `clojure.core/assoc-in` won't consume nested sequences


(defbench
  test-assoc-in*-seq
  "Sequences"
  (fn [n] (assoc-in* (nested-seq n) (path-seq n) :benchmark-sentinel))
  (range 1 max-in))


#_(run-one-defined-benchmark test-assoc-in*-seq :lightning)


;; Vectors


(defbench
  test-assoc-in-vec
  "Vectors"
  (fn [n] (assoc-in (nested-vec n) (path-nested-vec n) :benchmark-sentinel))
  (range 1 max-in))


(defbench
  test-assoc-in*-vec
  "Vectors"
  (fn [n] (assoc-in* (nested-vec n) (path-nested-vec n) :benchmark-sentinel))
  (range 1 max-in))


(defbench
  test-assoc-in-vec-2
  "Vectors"
  (fn [n] (assoc-in (narrow-deep-vec n)
                    (path-narrow-deep-vec n)
                    :benchmark-sentinel))
  (range-pow-10 5))


(defbench
  test-assoc-in*-vec-2
  "Vectors"
  (fn [n] (assoc-in* (narrow-deep-vec n)
                     (path-narrow-deep-vec n)
                     :benchmark-sentinel))
  (range-pow-10 5))


#_(run-one-defined-benchmark test-assoc-in*-vec-2 :lightning)


;; Lists


(defbench
  test-assoc-in*-list
  "Lists"
  (fn [n] (assoc-in* (nested-list n) (path-list n) :benchmark-sentinel))
  (range 1 5))


#_(run-one-defined-benchmark test-assoc-in*-list :lightning)


;; Hashmaps


(defbench
  test-assoc-in-map
  "Hashmaps"
  (fn [n] (assoc-in (nested-map n) (path-map n) :benchmark-sentinel))
  (range 1 max-in))


(defbench
  test-assoc-in*-map
  "Hashmaps"
  (fn [n] (assoc-in* (nested-map n) (path-map n) :benchmark-sentinel))
  (range 1 max-in))


#_(run-one-defined-benchmark test-assoc-in-map :lightning)


#_(run-benchmarks "resources/assoc_in_options.edn")
#_(generate-documents "resources/assoc_in_options.edn")


;; Unit tests for benchmark functions


(deftest assoc-in-assoc-in*-benchmark-tests
  (are [benchmark-name n]
      (every? true? (map #(= :benchmark-sentinel (fn-then-get-in* (benchmark-name :f) %)) n))
    test-assoc-in*-seq (range 1 max-in)
    test-assoc-in-vec (range 1 max-in)
    test-assoc-in*-vec (range 1 max-in)
    test-assoc-in*-list (range 1 5))
  (are [benchmark-name]
      (every? true? (map #(= :benchmark-sentinel (fn-then-get-in*-2 (benchmark-name :f) % n-levels)) (range-pow-10 5)))
    test-assoc-in-vec-2
    test-assoc-in*-vec-2)
  (are [benchmark-name]
      (every? true? (map #(= :benchmark-sentinel (get-in* ((benchmark-name :f) %) (repeat % 0))) (range 1 max-in)))
    test-assoc-in-map
    test-assoc-in*-map))


#_(run-tests)

