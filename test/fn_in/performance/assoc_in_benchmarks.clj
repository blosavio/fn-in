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
                                                   n-levels]]
   [fn-in.performance.benchmark-utils :refer [fn-then-get-in*
                                              fn-then-get-in*-2]]
   [fn-in.core :refer [assoc-in*
                       get-in*]]))


;; Sequences


;; `clojure.core/assoc-in` won't consume nested sequences


(defbench
  test-assoc-in*-seq
  "Sequences"
  (fn [n] (assoc-in* (nested-seq n)
                     (repeat n (dec n))
                     :benchmark-sentinel))
  (range 1 max-in))


#_(run-one-defined-benchmark test-assoc-in*-seq :lightning)


;; Vectors


(defbench
  test-assoc-in-vec
  "Vectors"
  (fn [n] (assoc-in (nested-vec n) (repeat n (dec n)) :benchmark-sentinel))
  (range 1 max-in))


(defbench
  test-assoc-in*-vec
  "Vectors"
  (fn [n] (assoc-in* (nested-vec n) (repeat n (dec n)) :benchmark-sentinel))
  (range 1 max-in))


(defbench
  test-assoc-in-vec-2
  "Vectors"
  (fn [n] (assoc-in (narrow-deep-vec n)
                    (concat (repeat n-levels n) [(dec n)])
                    :benchmark-sentinel))
  (range-pow-10 5))


(defbench
  test-assoc-in*-vec-2
  "Vectors"
  (fn [n] (assoc-in* (narrow-deep-vec n)
                     (concat (repeat n-levels n) [(dec n)])
                     :benchmark-sentinel))
  (range-pow-10 5))


#_(run-one-defined-benchmark test-assoc-in*-vec-2 :lightning)


;; Lists


(defbench
  test-assoc-in*-list
  "Lists"
  (fn [n] (assoc-in* (nested-list n) (repeat n (dec n)) :benchmark-sentinel))
  (range 1 5))


#_(run-one-defined-benchmark test-assoc-in*-list :lightning)


;; Hashmaps


(defbench
  test-assoc-in-map
  "Hashmaps"
  (fn [n] (assoc-in (nested-map n) (repeat n 0) :benchmark-sentinel))
  (range 1 max-in))


(defbench
  test-assoc-in*-map
  "Hashmaps"
  (fn [n] (assoc-in* (nested-map n) (repeat n 0) :benchmark-sentinel))
  (range 1 max-in))


#_(run-one-defined-benchmark test-assoc-in-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest assoc-in-assoc-in*-benchmark-tests
  (testing "sequences"
    (is (every? true? (map #(= :benchmark-sentinel
                               (fn-then-get-in* (test-assoc-in*-seq :f) %))
                           (range 1 max-in)))))
  (testing "vectors"
    (is (every? true? (map #(= :benchmark-sentinel
                               (fn-then-get-in* (test-assoc-in-vec :f) %))
                           (range 1 max-in))))
    (is (every? true? (map #(= :benchmark-sentinel
                               (fn-then-get-in* (test-assoc-in*-vec :f) %))
                           (range 1 max-in))))
    (is (every? true?
                (map #(= :benchmark-sentinel
                         (fn-then-get-in*-2 (test-assoc-in-vec-2 :f) % n-levels))
                     (range-pow-10 5))))
    (is (every? true?
                (map #(= :benchmark-sentinel
                         (fn-then-get-in*-2 (test-assoc-in*-vec-2 :f) % n-levels))
                     (range-pow-10 5)))))
  (testing "lists"
    ;; lists are not associative, so skip `clojure.core/assoc-in`
    (is (every? true? (map #(= :benchmark-sentinel
                               (fn-then-get-in* (test-assoc-in*-list :f) %))
                           (range 1 5)))))
  (testing "hashmaps"
    (is (every? true? (map #(= :benchmark-sentinel
                               (get-in* ((test-assoc-in-map :f) %) (repeat % 0)))
                           (range 1 max-in))))
    (is (every? true? (map #(= :benchmark-sentinel
                               (get-in* ((test-assoc-in*-map :f) %) (repeat % 0)))
                           (range 1 max-in))))))


#_(run-tests)

