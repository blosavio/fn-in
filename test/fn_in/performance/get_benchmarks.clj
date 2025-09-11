(ns fn-in.performance.get-benchmarks
  "Benchmarks that measure `get*` performance."
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
   [fn-in.core :refer [get*]]))


;; Sequences


(defbench
  test-get-seq
  "Sequences"
  (fn [n] (get (seq-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-seq-length))


(defbench
  test-get*-seq
  "Sequences"
  (fn [n] (get* (seq-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-get-seq :lightning)


;; Vectors


(defbench
  test-get-vec
  "Vectors"
  (fn [n] (get (vec-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-seq-length))


(defbench
  test-get*-vec
  "Vectors"
  (fn [n] (get* (vec-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-seq-length))


#_(run-one-defined-benchmark test-get*-seq :lightning)


;; Lists


(defbench
  test-get-list
  "Lists"
  (fn [n] (get (list-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-list-length))


(defbench
  test-get*-list
  "Lists"
  (fn [n] (get* (list-of-n-rand-ints n) (dec n)))
  (range-pow-10 max-list-length))


#_(run-one-defined-benchmark test-get-list :lightning)


;; Hashmaps


(defbench
  test-get-map
  "Hashmaps"
  (fn [n] (get (map-of-n-key-vals n) (dec n)))
  (range-pow-10 max-hashmap-length))


(defbench
  test-get*-map
  "Hashmaps"
  (fn [n] (get* (map-of-n-key-vals n) (dec n)))
  (range-pow-10 max-hashmap-length))


#_(run-one-defined-benchmark test-get-map :lightning)


#_(run-benchmarks)
#_(generate-documents)


;; Unit tests for benchmark functions


(deftest get-get*-benchmark-tests
  (testing "sequences"
    (is (every? true? (map #(= (last (seq-of-n-rand-ints %))
                               ((test-get-seq :f) %))
                           (range-pow-10 max-seq-length))))

    (is (every? true? (map #(= (last (seq-of-n-rand-ints %))
                               ((test-get*-seq :f) %))
                           (range-pow-10 max-seq-length)))))
  (testing "vectors"
    (is (every? true? (map #(= (last (vec-of-n-rand-ints %))
                               ((test-get-vec :f) %))
                           (range-pow-10 max-seq-length))))
    (is (every? true? (map #(= (last (vec-of-n-rand-ints %))
                               ((test-get*-vec :f) %))
                           (range-pow-10 max-seq-length)))))
  (testing "lists"
    ;; `get` fails on lists
    (is (every? true? (map #(nil? ((test-get-list :f) %))
                           (range-pow-10 max-list-length))))

    (is (every? true? (map #(= (last (list-of-n-rand-ints %))
                               ((test-get*-list :f) %))
                           (range-pow-10 max-list-length)))))
  (testing "hashmaps"
    (is (every? true? (map #(= ((map-of-n-key-vals %1) (dec %))
                               ((test-get-map :f) %))
                           (range-pow-10 max-hashmap-length))))
    (is (every? true? (map #(= ((map-of-n-key-vals %1) (dec %))
                               ((test-get*-map :f) %))
                           (range-pow-10 max-hashmap-length))))))


#_(run-tests)

