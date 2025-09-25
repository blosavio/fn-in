(ns fn-in.performance.benchmark-structures
  "Example heterogeneous, arbitrarily-nested data structures used by fn-in
  library benchmarks."
  (:require
   [fastester.measure :refer [range-pow-10]]
   [fn-in.core :refer [update*]]
   [fn-in.performance.benchmark-utils :refer [coll-of-n-rand-ints
                                              narrow-deep
                                              nested]]))


(def max-seq-length 6)
(def seq-of-n-rand-ints (coll-of-n-rand-ints :sequence max-seq-length))
(def vec-of-n-rand-ints (coll-of-n-rand-ints :vector max-seq-length))


(def max-list-length 4)
(def list-of-n-rand-ints (coll-of-n-rand-ints :list max-list-length))


(def max-hashmap-length 6)
(def map-of-n-key-vals (coll-of-n-rand-ints :map max-hashmap-length))


(def max-in 7)


(def nested-vec
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (nested k :vector)))
    (transient {})
    (range 1 max-in))))


(def n-levels 3)


(def narrow-deep-vec
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (narrow-deep :vector k n-levels)))
    (transient {})
    (range-pow-10 5))))


(def nested-seq
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (nested k :sequence)))
    (transient {})
    (range 1 max-in))))


(def max-list 5)


(def nested-list
  (doall
   (reduce
    (fn [m k] (assoc m k (nested k :list)))
    {}
    (range 1 max-list))))


(def nested-map
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (nested k :map)))
    (transient {})
    (range 1 max-in))))


(def path-seq
  (let [f (fn [m k] (assoc m k (repeat k (dec k))))]
    (reduce f {} (range 1 max-in))))


(def path-nested-vec path-seq)


(def path-narrow-deep-vec
  (let [f (fn [m k] (assoc m k (update* (repeat (inc n-levels) k) n-levels dec)))]
    (reduce f {} (range-pow-10 5))))


(def path-list path-seq)


(def path-map
  (let [f (fn [m k] (assoc m k (repeat k 0)))]
    (reduce f {} (range 1 max-in))))

