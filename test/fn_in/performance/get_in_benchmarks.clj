(ns fn-in.performance.get-in-benchmarks
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
   [fn-in.performance.benchmark-utils :refer [narrow-deep
                                              nested]]
   [fn-in.core :refer [get-in*]]))


(def max-in 7)


;; Vectors


(def nested-vec
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (nested k :vector)))
    (transient {})
    (range 1 max-in))))


(defbench
  test-get-in-vec
  "Vectors"
  (fn [n] (get-in (nested-vec n) (take n (repeat (dec n)))))
  (range 1 max-in))


(defbench
  test-get-in*-vec
  "Vectors"
  (fn [n] (get-in* (nested-vec n) (take n (repeat (dec n)))))
  (range 1 max-in))


(def n-levels 3)


(def narrow-deep-vec
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (narrow-deep :vector k n-levels)))
    (transient {})
    (range-pow-10 5))))


(defbench
  test-get-in-vec-2
  "Vectors"
  (fn [n] (get-in (narrow-deep-vec n) (concat (take n-levels (repeat n))
                                              [(dec n)])))
  (range-pow-10 5))


(defbench
  test-get-in*-vec-2
  "Vectors"
  (fn [n] (get-in* (narrow-deep-vec n) (concat (take n-levels (repeat n))
                                               [(dec n)])))
  (range-pow-10 5))


#_(run-one-defined-benchmark test-get-in-vec-2 :lightning)



;; Sequences


(def nested-seq
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (nested k :sequence)))
    (transient {})
    (range 1 max-in))))


(defbench
  test-get-in*-seq
  "Sequences"
  (fn [n] (get-in* (nested-seq n) (take n (repeat (dec n)))))
  (range 1 max-in))


;; Lists


(def max-list 5)


(def nested-list
  (doall
   (reduce
    (fn [m k] (assoc m k (nested k :list)))
    {}
    (range 1 max-list))))


(defbench
  test-get-in*-list
  "Lists"
  (fn [n] (get-in* (nested-list n) (take n (repeat (dec n)))))
  (range 1 max-list))


;; Hashmaps

(def nested-map
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (nested k :map)))
    (transient {})
    (range 1 max-in))))


(defbench
  test-get-in-map
  "Maps"
  (fn [n] (get-in (nested-map n) (take n (repeat 0))))
  (range 1 max-in))


(defbench
  test-get-in*-map
  "Maps"
  (fn [n] (get-in* (nested-map n) (take n (repeat 0))))
  (range 1 max-in))


#_(run-one-defined-benchmark test-get-in-map :lightning)


#_(run-benchmarks)
#_(generate-documents)

