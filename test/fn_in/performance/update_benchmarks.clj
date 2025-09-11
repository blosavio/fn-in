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
  (testing "sequences"
    (is (every? true? (map #(= (inc (get (seq-of-n-rand-ints %) (dec %)))
                               (fn-then-get* (test-update-seq :f) %))
                           (range-pow-10 max-seq-length))))
    (is (every? true? (map #(= (inc (get (seq-of-n-rand-ints %) (dec %)))
                               (fn-then-get* (test-update*-seq :f) %))
                           (range-pow-10 max-seq-length)))))
  (testing "vectors"
    (is (every? true? (map #(= (inc (get (vec-of-n-rand-ints %) (dec %)))
                               (fn-then-get* (test-update-vec :f) %))
                           (range-pow-10 max-seq-length))))
    (is (every? true? (map #(= (inc (get (vec-of-n-rand-ints %) (dec %)))
                               (fn-then-get* (test-update*-vec :f) %))
                           (range-pow-10 max-seq-length)))))
  (testing "lists"
    ;; lists are not associative, so skip `clojure.core/update`
    (is (every? true? (map #(= (inc (get* (list-of-n-rand-ints %) (dec %)))
                               (fn-then-get* (test-update*-list :f) %))
                           (range-pow-10 3)))))
  (testing "hashmaps"
    (is (every? true? (map #(= (inc (get (map-of-n-key-vals %) (dec %)))
                               (fn-then-get* (test-update-map :f) %))
                           (range-pow-10 max-hashmap-length))))
    (is (every? true? (map #(= (inc (get (map-of-n-key-vals %) (dec %)))
                               (fn-then-get* (test-update*-map :f) %))
                           (range-pow-10 max-hashmap-length))))))


#_(run-tests)

