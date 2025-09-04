(ns fn-in.performance.get-benchmarks
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
   [fn-in.performance.benchmark-utils :refer [coll-of-n-rand-ints]]
   [fn-in.core :refer [get*]]))


(def max-seq-length 6)
(def seq-of-n-rand-ints (coll-of-n-rand-ints :sequence max-seq-length))


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


(def vec-of-n-rand-ints (coll-of-n-rand-ints :vector max-seq-length))


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


(def max-list-length 4)
(def list-of-n-rand-ints (coll-of-n-rand-ints :list max-list-length))


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


(def max-hashmap-length 6)
(def map-of-n-key-vals (coll-of-n-rand-ints :map max-hashmap-length))


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
    (is (true? (every? true? (map #(= (last (seq-of-n-rand-ints %1))
                                      ((test-get-seq :f) %1))
                                  (range-pow-10 max-seq-length)))))

    (is (true? (every? true? (map #(= (last (seq-of-n-rand-ints %1))
                                      ((test-get*-seq :f) %1))
                                  (range-pow-10 max-seq-length))))))
  (testing "vectors"
    (is (true? (every? true? (map #(= (last (vec-of-n-rand-ints %1))
                                      ((test-get-vec :f) %1))
                                  (range-pow-10 max-seq-length)))))
    (is (true? (every? true? (map #(= (last (vec-of-n-rand-ints %1))
                                      ((test-get*-vec :f) %1))
                                  (range-pow-10 max-seq-length))))))
  (testing "lists"
    ;; `get` fails on lists
    (is (true? (every? true? (map #(nil? ((test-get-list :f) %1))
                                  (range-pow-10 max-list-length)))))

    (is (true? (every? true? (map #(= (last (list-of-n-rand-ints %1))
                                      ((test-get*-list :f) %1))
                                  (range-pow-10 max-list-length))))))
  (testing "hashmaps"
    (is (true? (every? true? (map #(= ((map-of-n-key-vals %1) (dec %1))
                                      ((test-get-map :f) %1))
                                  (range-pow-10 max-hashmap-length)))))
    (is (true? (every? true? (map #(= ((map-of-n-key-vals %1) (dec %1))
                                      ((test-get*-map :f) %1))
                                  (range-pow-10 max-hashmap-length)))))))


#_(run-tests)

