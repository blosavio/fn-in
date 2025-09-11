(ns fn-in.performance.update-in-benchmarks
  "Benchmarks that measure `update-in*` performance."
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
                                                   n-levels]]
   [fn-in.performance.benchmark-utils :refer [fn-then-get-in*
                                              fn-then-get-in*-2]]
   [fn-in.core :refer [update-in*
                       get-in*]]))


;; Sequences


;; `clojure.core/update-in` won't consume nested sequences


(defbench
  test-update-in*-seq
  "Sequences"
  (fn [n] (update-in* (nested-seq n) (repeat n (dec n)) inc))
  (range 1 max-in))


#_(run-one-defined-benchmark test-update-in*-seq :lightning)


;; Vectors


(defbench
  test-update-in-vec
  "Vectors"
  (fn [n] (update-in (nested-vec n) (repeat n (dec n)) inc))
  (range 1 max-in))


(defbench
  test-update-in*-vec
  "Vectors"
  (fn [n] (update-in* (nested-vec n) (repeat n (dec n)) inc))
  (range 1 max-in))


(defbench
  test-update-in-vec-2
  "Vectors"
  (fn [n] (update-in (narrow-deep-vec n)
                     (concat (repeat n-levels n) [(dec n)])
                     inc))
  (range-pow-10 5))


(defbench
  test-update-in*-vec-2
  "Vectors"
  (fn [n] (update-in* (narrow-deep-vec n)
                      (concat (repeat n-levels n) [(dec n)])
                      inc))
  (range-pow-10 5))


#_(run-one-defined-benchmark test-update-in*-vec-2 :lightning)


;; Lists


(defbench
  test-update-in*-list
  "Lists"
  (fn [n] (update-in* (nested-list n) (repeat n (dec n)) inc))
  (range 1 5))


#_(run-one-defined-benchmark test-update-in*-list :lightning)


;; Hashmaps


(defbench
  test-update-in-map
  "Hashmaps"
  (fn [n] (update-in (nested-map n) (repeat n 0) inc))
  (range 1 max-in))


(defbench
  test-update-in*-map
  "Hashmaps"
  (fn [n] (update-in* (nested-map n) (repeat n 0) inc))
  (range 1 max-in))


#_(run-one-defined-benchmark test-update-in-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest update-in-update-in*-benchmark-tests
  (are [structure benchmark-name n]
      (every? true? (map #(= (inc (get-in* (structure %) (repeat % (dec %)))) (fn-then-get-in* (benchmark-name :f) %)) n))
    nested-seq test-update-in*-seq (range 1 max-in)
    nested-vec test-update-in-vec (range 1 max-in)
    nested-vec test-update-in*-vec (range 1 max-in)
    nested-list test-update-in*-list (range 1 5))
  (are [benchmark-name]
      (every? true? (map #(= (inc (get-in* (nested-map %) (repeat % 0))) (get-in* ((benchmark-name :f) %) (repeat % 0))) (range 1 max-in)))
    test-update-in-map
    test-update-in*-map)
  (are [benchmark-name]
      (every? true? (map #(= (inc (get-in* (narrow-deep-vec %) (concat (take n-levels (repeat %)) [(dec %)]))) (fn-then-get-in*-2 (benchmark-name :f) % n-levels)) (range-pow-10 5)))
    test-update-in-vec-2
    test-update-in*-vec-2))



#_(run-tests)

