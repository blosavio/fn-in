(ns fn-in.performance.get-in-benchmarks
  "Benchmarks to measure `get-in*` performance."
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
   [fn-in.core :refer [get-in*]]))


;; Vectors


(defbench
  test-get-in-vec
  "Vectors"
  (fn [n] (get-in (nested-vec n) (repeat n (dec n))))
  (range 1 max-in))


(defbench
  test-get-in*-vec
  "Vectors"
  (fn [n] (get-in* (nested-vec n) (repeat n (dec n))))
  (range 1 max-in))


(defbench
  test-get-in-vec-2
  "Vectors"
  (fn [n] (get-in (narrow-deep-vec n) (concat (repeat n-levels n) [(dec n)])))
  (range-pow-10 5))


(defbench
  test-get-in*-vec-2
  "Vectors"
  (fn [n] (get-in* (narrow-deep-vec n) (concat (repeat n-levels n) [(dec n)])))
  (range-pow-10 5))


#_(run-one-defined-benchmark test-get-in-vec-2 :lightning)


;; Sequences


(defbench
  test-get-in*-seq
  "Sequences"
  (fn [n] (get-in* (nested-seq n) (repeat n (dec n))))
  (range 1 max-in))


;; Lists


(defbench
  test-get-in*-list
  "Lists"
  (fn [n] (get-in* (nested-list n) (repeat n (dec n))))
  (range 1 max-list))


;; Hashmaps


(defbench
  test-get-in-map
  "Maps"
  (fn [n] (get-in (nested-map n) (repeat n 0)))
  (range 1 max-in))


(defbench
  test-get-in*-map
  "Maps"
  (fn [n] (get-in* (nested-map n) (repeat n 0)))
  (range 1 max-in))


#_(run-one-defined-benchmark test-get-in-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest get-in-get-in*-benchmark-tests
  (are [value-fn benchmark-name n]
      (every? true? (map #(= (value-fn %) ((benchmark-name :f) %)) n))
    #(int (dec (Math/pow % %))) test-get-in*-list (range 1 max-list)
    #(int (dec (Math/pow % %))) test-get-in*-seq (range 1 max-in)
    #(int (dec (Math/pow % %))) test-get-in-vec (range 1 max-in)
    #(int (dec (Math/pow % %))) test-get-in*-vec (range 1 max-in)
    #(dec %) test-get-in-vec-2 (range-pow-10 5)
    #(dec %) test-get-in*-vec-2 (range-pow-10 5)
    (constantly 0) test-get-in-map (range 1 max-in)
    (constantly 0) test-get-in*-map (range 1 max-in)))


#_(run-tests)

