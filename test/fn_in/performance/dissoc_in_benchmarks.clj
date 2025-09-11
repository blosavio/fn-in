(ns fn-in.performance.dissoc-in-benchmarks
  "Benchmarks that measure `dissoc-in*` performance."
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
   [fn-in.core :refer [dissoc-in*
                       get-in*]]))


;; Vectors


(defbench
  test-dissoc-in*-vec
  "Vectors"
  (fn [n] (dissoc-in* (nested-vec n) (repeat n (dec n))))
  (range 1 max-in))


(defbench
  test-dissoc-in*-vec-2
  "Vectors"
  (fn [n] (dissoc-in* (narrow-deep-vec n) (concat (repeat n-levels n) [(dec n)])))
  (range-pow-10 5))


#_(run-one-defined-benchmark test-dissoc-in*-vec :lightning)
#_(run-one-defined-benchmark test-dissoc-in*-vec-2 :lightning)


;; Sequences


(defbench
  test-dissoc-in*-seq
  "Sequences"
  (fn [n] (dissoc-in* (nested-seq n) (repeat n (dec n))))
  (range 1 max-in))


#_(run-one-defined-benchmark test-dissoc-in*-seq :lightning)


;; Lists


(defbench
  test-dissoc-in*-list
  "Lists"
  (fn [n] (dissoc-in* (nested-list n) (repeat n (dec n))))
  (range 1 max-list))


#_(run-one-defined-benchmark test-dissoc-in*-list :lightning)


;; Hashmaps


(defbench
  test-dissoc-in*-map
  "Maps"
  (fn [n] (dissoc-in* (nested-map n) (repeat n 0)))
  (range 1 max-in))


#_(run-one-defined-benchmark test-dissoc-in*-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest dissoc-in-dissoc-in*-benchmark-tests
  (are [structure path-fn benchmark-name n]
      (every? true? (map #(= (dec (count (get-in* (structure %) (path-fn %)))) (count (get-in* ((benchmark-name :f) %) (path-fn %)))) n))
    nested-list #(repeat (dec %) (dec %)) test-dissoc-in*-list (range 1 max-list)
    nested-seq #(repeat (dec %) (dec %)) test-dissoc-in*-seq (range 1 max-in)
    nested-vec #(repeat (dec %) (dec %)) test-dissoc-in*-vec (range 1 max-in)
    nested-map #(repeat (dec %) 0) test-dissoc-in*-map (range 1 max-in)
    narrow-deep-vec #(repeat n-levels %) test-dissoc-in*-vec-2 (range-pow-10 5)))


#_(run-tests)

