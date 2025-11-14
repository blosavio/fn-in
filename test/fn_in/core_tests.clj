(ns fn-in.core-tests
  "Perhaps a bit exuberant, but it can be tricky to know when a collection
  instance is auto-promoted or otherwise coerced to another type. So, test with
  a defensive mindset so that we can keep the 'any Clojure collection' promise.

  See also `in-tests` and `helper-tests`."
  (:require
   [fn-in.core :refer :all]
   [clojure.test :refer [are
                         is
                         deftest
                         testing
                         run-tests
                         run-test]]
   [clojure.string :as string])
  (:import (clojure.lang Tuple)))


(defrecord TestRecord [a b c])
(defstruct test-struct :a :b :c)


(defn queue
  "Returns a `clojure.lang.PersistentQueue` of the elements in `coll`."
  {:UUIDv4 #uuid "bce76d1f-0541-453e-894c-6fbc83ceb625"
   :no-doc true}
  [coll]
  (into (clojure.lang.PersistentQueue/EMPTY) coll))


#_(deftest collection-types
    (are [c-type coll] (instance? c-type coll)
      clojure.lang.PersistentVector (vector 11 22 33)
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3)
      clojure.core.Vec (vector-of :int 11 22 33)
      clojure.lang.PersistentVector (. clojure.lang.LazilyPersistentVector (create (cons 11 (cons 22 '(33)))))
      clojure.lang.MapEntry (first {:a 11})

      clojure.lang.PersistentList$EmptyList (list)
      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 (cons 22 '(33)))

      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentTreeMap (sorted-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentArrayMap (zipmap [:a :b :c] [11 22 33])
      clojure.lang.PersistentStructMap (struct test-struct 11 22 33)
      fn_in.core_tests.TestRecord (->TestRecord 11 22 33)

      clojure.lang.PersistentHashSet (hash-set 11 22 33)
      clojure.lang.PersistentHashSet (set [11 22 33])
      clojure.lang.PersistentTreeSet (sorted-set 11 22 33)

      clojure.lang.Cycle (cycle [11 22 33])
      clojure.lang.LazySeq (interleave [11 33] [22 44])
      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.Iterate (iterate #(+ 11 %) 11)
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [44 33]])
      clojure.lang.LongRange (range 11 33)
      clojure.lang.Range (range 11.0 33.0)
      clojure.lang.Repeat (repeat 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])
      clojure.lang.LazySeq (subseq (sorted-set 0 11 22 33 44 55) > 0 < 44)

      clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.StringSeq (seq "abc")
      clojure.lang.PersistentVector (Tuple/create 11 22 33)

      ;; Java Arrays
      boolean/1 (boolean-array [true false true])
      byte/1 (byte-array [0x11 0x22 0x33])
      char/1 (char-array [\a \b \c])
      double/1 (double-array [11 22 33])
      float/1 (float-array [11.0 22.0 33.0])
      int/1 (int-array [11 22 33])
      long/1 (long-array [11 22 33])
      java.lang.Object/1 (object-array [42 \c true])
      short/1 (short-array [11 22 33])))


(deftest get*-test
  (testing "`nil`"
    (is (nil? (get* nil :foo))))

  (testing "empty sequentials, cons, and sets"
    (are [c-type coll] (and (instance? c-type coll)
                            (nil? (get* coll 0)))
      clojure.lang.PersistentVector (vector)
      clojure.lang.PersistentVector (subvec [11 22 33] 0 0)
      ;; clojure.lang.Vec (vector-of :int)

      clojure.lang.IPersistentList (list)

      clojure.lang.PersistentHashSet (hash-set)
      clojure.lang.PersistentHashSet (set [])
      clojure.lang.PersistentTreeSet (sorted-set)

      clojure.lang.IPersistentList (cycle [])
      clojure.lang.LazySeq (interleave [] [])
      clojure.lang.LazySeq (interpose nil [])
      clojure.lang.LazySeq (lazy-cat)
      clojure.lang.LazySeq (lazy-seq [])
      clojure.lang.LazySeq (mapcat + [])
      clojure.lang.IPersistentList (range 0 0)
      clojure.lang.IPersistentList (repeat 0 nil)
      clojure.lang.IPersistentList (sequence [])

      ;;clojure.lang.PersistentQueue (get* (queue []) 0)
      clojure.lang.PersistentVector (Tuple/create)))

  (testing "empty associatives"
    (are [c-type coll] (and (instance? c-type coll)
                            (nil? (get* coll :d)))
      clojure.lang.PersistentArrayMap (array-map)
      clojure.lang.PersistentArrayMap (hash-map)
      clojure.lang.PersistentTreeMap (sorted-map)
      clojure.lang.PersistentArrayMap (zipmap [] [])
      clojure.lang.PersistentStructMap (struct test-struct)))

  #_(testing "arrays"
      (are [c-type coll] (and (instance? c-type coll)
                              (nil? (get* coll 0)))
        boolean/1 (boolean-array [])
        byte/1 (byte-array [])
        char/1 (char-array [])
        double/1 (double-array [])
        float/1 (float-array [])
        int/1 (int-array [])
        long/1 (long-array [])
        java.lang.Object/1 (object-array [])
        short/1 (short-array [])))

  (testing "zero-th index, sequentials, cons, and sets"
    (are [c-type coll] (and (instance? c-type coll)
                            (= 11 (get* coll 0)))
      clojure.lang.PersistentVector (vector 11 22 33)
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3)
      ;;clojure.lang.Vec (vector-of :int 11 22 33)
      clojure.lang.PersistentVector (. clojure.lang.LazilyPersistentVector (create (cons 11 '(22 33))))
      clojure.lang.MapEntry (first {11 22})

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.Cycle (cycle [11 22 33])
      clojure.lang.LazySeq (interleave [11 22 33] [:a :b :c])
      clojure.lang.LazySeq (interpose :foo [11 22 33])
      clojure.lang.Iterate (iterate inc 11)
      clojure.lang.LazySeq (lazy-cat [11 22 33] [44 55 66])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[33 22 11] [66 55 44] [99 88 77]])
      clojure.lang.LongRange (range 11 44 11)
      clojure.lang.Repeat (repeat 3 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      #_(get* (queue [11 22 33]) 0)
      clojure.lang.PersistentVector (Tuple/create 11 22 33)))

  #_(testing "zero-th indexes, arrays"
      (are [x y] (= x y)
        \a (get* (seq "abc") 0)
        true (boolean-array [true false true])
        0x11 (byte-array [0x11 0x22 0x33])
        \a (char-array [\a \b \c])
        11 (double-array [11 22 33])
        11.0 (float-array [11.0 22.0 33.0])
        11 (int-array [11 22 33])
        11 (long-array [11 22 33])
        42 (object-array [42 \c true])
        22 (short-array [11 22 33])))

  (testing "provided indexes, sequentials, cons, and sets"
    (are [c-type coll idx] (and (instance? c-type coll)
                                (= 33 (int (get* coll idx))))
      clojure.lang.PersistentVector (vector 11 22 33) 2
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3) 2
      ;;clojure.lang.Vec (vector-of :int 11 22 33) 2
      clojure.lang.PersistentVector (. clojure.lang.LazilyPersistentVector (create (cons 11 '(22 33)))) 2
      clojure.lang.MapEntry (first {22 33}) 1

      clojure.lang.PersistentList (list 11 22 33) 2
      clojure.lang.Cons (cons 11 '(22 33)) 2

      clojure.lang.PersistentHashSet (hash-set 11 22 33) 33
      clojure.lang.PersistentHashSet (set [11 22 33]) 33
      clojure.lang.PersistentTreeSet (sorted-set 11 22 33) 33

      clojure.lang.Cycle (cycle [11 22 33]) 2
      clojure.lang.LazySeq (interleave [:a :b :c] [11 22 33]) 5
      clojure.lang.LazySeq (interpose :foo [11 22 33]) 4
      clojure.lang.Iterate (iterate inc 11) 22
      clojure.lang.LazySeq (lazy-cat [11 22 33] [44 55 66]) 2
      clojure.lang.LazySeq (lazy-seq [11 22 33 44 55]) 2
      clojure.lang.LazySeq (mapcat reverse [[33 22 11] [66 55 44] [99 88 77]]) 2
      clojure.lang.LongRange (range 11 44 11) 2
      ;;clojure.lang.Range (range 11.0 44.0 11.0) 2
      clojure.lang.Repeat (repeat 3 33) 2
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11]) 2

      ;;clojure.lang.PersistentQueue (queue [11 22 33]) 2
      clojure.lang.PersistentVector (Tuple/create 11 22 33) 2))

  (testing "provided keys, associatives"
    (are [c-type coll] (and (instance? c-type coll)
                            (= 33 (get* coll :c)))
      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentTreeMap (sorted-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentArrayMap (zipmap [:a :b :c] [11 22 33])
      clojure.lang.PersistentStructMap (struct test-struct 11 22 33)
      fn_in.core_tests.TestRecord (->TestRecord 11 22 33)))

  #_(testing "provided indexes, arrays"
      (are [c-type coll result] (and (instance? c-type coll)
                                     (= result (get* coll 2)))
        clojure.lang.StringSeq (seq "abcde") \c
        boolean/1 (boolean-array [true false true]) true
        byte/1 (byte-array [0x11 0x22 0x33]) 0x33
        char/1 (char-array [\a \b \c]) \c
        double/1 (double-array [11 22 33]) 33
        float/1 (float-array [11.0 22.0 33.0]) 33.0
        int/1 (int-array [11 22 33]) 33
        long/1 (long-array [11 22 33]) 33
        java.lang.Object/1 (object-array [42 \c true]) true
        short/1 (short-array [11 22 33]) 33))

  (testing "not-found"
    (are [c-type coll] (and (instance? c-type coll)
                            (= :not-found (get* coll 5 :not-found)))
      clojure.lang.PersistentVector (vector 11 22 33)
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3)
      ;;clojure.core.Vec (vector-of :int 11 22 33)
      clojure.lang.PersistentVector (. clojure.lang.LazilyPersistentVector (create (cons 11 '(22 33))))
      clojure.lang.MapEntry (first {11 22})

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentTreeMap (sorted-map 0 11 1 22 2 33)
      clojure.lang.PersistentArrayMap (zipmap [:a :b :c] [11 22 33])
      clojure.lang.PersistentStructMap (struct test-struct 11 22 33)
      fn_in.core_tests.TestRecord (->TestRecord 11 22 33)

      clojure.lang.PersistentHashSet (hash-set 11 22 33 44 55)
      clojure.lang.PersistentHashSet (set [11 22 33 44 55])
      clojure.lang.PersistentTreeSet (sorted-set 11 22 33 44 55)

      clojure.lang.LazySeq (interleave [:a :b] [11 22])
      clojure.lang.LazySeq (interpose :foo [11 22])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [44 33]])
      clojure.lang.LongRange (range 0 3)
      clojure.lang.Range (range 0.0 3.0)
      clojure.lang.Repeat (repeat 3 99)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [33 44 55])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])
      clojure.lang.LazySeq (subseq (sorted-set 11 22 33) >= 11 < 44)

      ;;clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.StringSeq (seq "abc")
      clojure.lang.PersistentVector (Tuple/create 11 22 33)

      ;;boolean/1 (boolean-array [true false true])
      ;;byte/1 (byte-array [0x11 0x22 0x33])
      ;;char/1 (char-array [\a \b \c])
      ;;double/1 (double-array [11 22 33])
      ;;float/1 (float-array [11.0 22.0 33.0])
      ;;int/1 (int-array [11 22 33])
      ;;long/1 (long-array [11 22 33])
      ;;java.lang.Object/1 (object-array [42 \c true])
      ;;short/1 (short-array [11 22 33])
      ))

  (testing "composite keys on maps"
    (are [x y] (= x y)
      :vector-key (get* {[1 2] :vector-key {:a :b} :map-key '(11 22) :list-key} [1 2])
      :map-key (get* {[1 2] :vector-key {:a :b} :map-key '(11 22) :list-key} {:a :b})
      :list-key (get* {[1 2] :vector-key {:a :b} :map-key '(11 22) :list-key} '(11 22))))

  (testing "composite indexes on sets"
    (are [x y] (= x y)
      [22 33] (get* #{{:a 11} [22 33] '(44 55)} [22 33])
      {:a 11} (get* #{{:a 11} [22 33] '(44 55)} {:a 11})
      nil (get* #{{:a 11} [22 33] '(44 55)} {:b 99})
      '(44 55) (get* #{{:a 11} [22 33] '(44 55)} '(44 55))
      #{66 77} (get* #{{:a 11} [22 33] '(44 55) #{66 77}} #{66 77}))))

(deftest assoc*-test
  (testing "empty collections"
    (are [c-type coll keydex result] (and (instance? c-type coll)
                                          (= result (assoc* coll keydex 99)))
      clojure.lang.PersistentVector (vector) 2 [nil nil 99]
      clojure.lang.PersistentVector (subvec [] 0 0) 2  [nil nil 99]
      ;;clojure.core.Vec (vector-of :int) 2 [nil nil 99]

      clojure.lang.IPersistentList (list) 2 '(nil nil 99)

      clojure.lang.PersistentArrayMap (array-map) :a {:a 99}
      clojure.lang.PersistentArrayMap (hash-map) :a {:a 99}
      clojure.lang.PersistentTreeMap (sorted-map) :a {:a 99}
      clojure.lang.PersistentArrayMap (zipmap [] []) :a {:a 99}

      clojure.lang.PersistentHashSet (hash-set) 99 #{99}
      clojure.lang.PersistentTreeSet (sorted-set) 99 #{99}

      clojure.lang.LazySeq (interleave [] []) 0 '(99)
      clojure.lang.LazySeq (interpose nil []) 0 '(99)
      clojure.lang.LazySeq (lazy-cat) 0 '(99)
      clojure.lang.LazySeq (lazy-seq []) 0 '(99)
      clojure.lang.LazySeq (mapcat + []) 0 '(99)
      clojure.lang.IPersistentList (range 0 0) 0 '(99)
      clojure.lang.IPersistentList (sequence []) 0 '(99)

      ;;clojure.lang.PersistentQueue (queue []) 0 [99]
      clojure.lang.PersistentVector (Tuple/create) 0 [99]))

  (testing "non-terminating sequences"
    (are [c-type coll result] (and (instance? c-type coll)
                                   (= result (take 5 (assoc* coll 2 99))))
      clojure.lang.Cycle (cycle [11 22 33 44 55]) '(11 22 99 44 55)
      clojure.lang.Iterate (iterate inc 11) '(11 12 99 14 15)
      clojure.lang.LazySeq (lazy-seq [11 22 33 44 55]) '(11 22 99 44 55)
      clojure.lang.LongRange (range 0 1000)'(0 1 99 3 4)
      clojure.lang.Repeat (repeat 11) '(11 11 99 11 11)))

  (testing "at the beginning of a sequential"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [99 22 33] (assoc* coll 0 99)))
      clojure.lang.PersistentVector [11 22 33]
      clojure.lang.APersistentVector$SubVector(subvec [11 22 33] 0 3)
      ;;clojure.core.Vec (vector-of :int 11 22 33)

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]])
      clojure.lang.LongRange (range 11 44 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      ;;clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.PersistentVector (Tuple/create 11 22 33)))

  (testing "in the middle of a sequential"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [11 99 33] (assoc* coll 1 99)))
      clojure.lang.PersistentVector [11 22 33]
      clojure.lang.APersistentVector$SubVector(subvec [11 22 33] 0 3)
      ;;clojure.core.Vec (vector-of :int 11 22 33)

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]])
      clojure.lang.LongRange (range 11 44 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      ;;clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.PersistentVector (Tuple/create 11 22 33)))

  (testing "at the end of a sequential"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [11 22 99] (assoc* coll 2 99)))
      clojure.lang.PersistentVector [11 22 33]
      clojure.lang.APersistentVector$SubVector(subvec [11 22 33] 0 3)
      ;;clojure.core.Vec (vector-of :int 11 22 33)

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]])
      clojure.lang.LongRange (range 11 44 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      ;;clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.PersistentVector (Tuple/create 11 22 33)))

  (testing "beyond the end of a sequential"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [11 22 33 99] (assoc* coll 3 99)))
      clojure.lang.PersistentVector [11 22 33]
      clojure.lang.APersistentVector$SubVector(subvec [11 22 33] 0 3)
      ;;clojure.core.Vec (vector-of :int 11 22 33)

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]])
      clojure.lang.LongRange (range 11 44 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      ;;clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.PersistentVector (Tuple/create 11 22 33)))

  (testing "adding to a non-emtpy associative"
    (are [c-type coll] (and (instance? c-type coll)
                            (= {:a 11 :b 22 :c 33 :d 99} (assoc* coll :d 99)))
      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentTreeMap (sorted-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentArrayMap (zipmap [:a :b :c] [11 22 33])
      clojure.lang.PersistentStructMap (struct test-struct 11 22 33)))

  (testing "assoc-ing over an associative's pre-existing key-value pair"
    (are [x y] (= x y)
      {:a 11 :b 99 :c 33} (assoc* {:a 11 :b 22 :c 33} :b 99)
      {:a 99, :b 2, :c 3} (assoc* (zipmap [:a :b :c] [1 2 3]) :a 99)))

  (testing "assoc-ing away non-unique set members"
    (are [x y] (= x y)
      #{11 33} (assoc* #{11 22 33} 22 33)))

  (testing "assoc-ing records"
    (are [x y] (= x y)
      (->TestRecord 99 :foo :bar) (assoc* (->TestRecord nil :foo :bar) :a 99)
      (->TestRecord 99 100 101) (assoc* (->TestRecord nil nil nil) :a 99 :b 100 :c 101)))

  (testing "extra key/index-vals"
    (are [x y] (= x y)
      [:foo :bar 33] (assoc* [11 22 33] 0 :foo 1 :bar)
      [:foo :bar :baz] (assoc* [11 22 33] 0 :foo 1 :bar 2 :baz)
      {:a 11 :b 22} (assoc* {} :a 11 :b 22)
      {:a 11 :b 22 :c 33} (assoc* {} :a 11 :b 22 :c 33)
      [:foo :bar 2] (assoc* (range 3) 0 :foo 1 :bar)
      [:foo :bar :baz] (assoc* (range 3) 0 :foo 1 :bar 2 :baz)
      #{:foo :bar 33} (assoc* #{11 22 33} 11 :foo 22 :bar)
      #{:foo :bar :baz} (assoc* #{11 22 33} 11 :foo 22 :bar 33 :baz)
      '(:foo :bar 2) (assoc* (range 3) 0 :foo 1 :bar)
      '(:foo :bar :baz) (assoc* (range 3) 0 :foo 1 :bar 2 :baz)
      '(:foo :bar) (assoc* '() 0 :foo 1 :bar)
      '(:foo :bar 33) (assoc* '(11 22 33) 0 :foo 1 :bar)
      '(:foo :bar :baz) (assoc* '(11 22 33) 0 :foo 1 :bar 2 :baz)
      '(:foo :bar 33) (assoc* (cons 11 '(22 33)) 0 :foo 1 :bar)
      '(:foo :bar :baz) (assoc* (cons 11 '(22 33)) 0 :foo 1 :bar 2 :baz)
      nil (assoc* nil :foo :bar)
      nil (assoc* nil :foo :bar :baz :boz)
      nil (assoc* nil :foo :bar :baz :boz :qux :quz))))


(deftest update*-test
  (testing "updated values within bounds"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [11 22 330] (update* coll 2 #(* % 10))))
      clojure.lang.PersistentVector (vector 11 22 33)
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3)
      ;;clojure.core.Vec (vector-of :int 11 22 33)

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]])
      clojure.lang.LongRange (range 11 44 11)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      ;;clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.PersistentVector (Tuple/create 11 22 33)))

  (testing "updating set elements"
    (are [c-type coll] (and (instance? c-type coll)
                            (= #{11 22 330} (update* coll 33 #(* % 10))))
      clojure.lang.PersistentHashSet (hash-set 11 22 33)
      clojure.lang.PersistentTreeSet (sorted-set 11 22 33)))

  (testing "updating elements of non-terminating sequentials"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [11 22 330] (take 3 (update* coll 2 #(* % 10)))))
      clojure.lang.Cycle (cycle [11 22 33])
      clojure.lang.Iterate (iterate #(+ 11 %) 11)
      clojure.lang.LongRange (range 11 44 11)))

  (testing "updating existing elements in an associative"
    (are [c-type coll] (and (instance? c-type coll)
                            (= {:a 11 :b 22 :c 330} (update* coll :c #(* % 10))))
      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentTreeMap (sorted-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentArrayMap (zipmap [:a :b :c] [11 22 33])
      clojure.lang.PersistentStructMap (struct test-struct 11 22 33)))

  (testing "updating fields of a record"
    (are [x y] (= x y)
      (->TestRecord 11 22 330) (update* (->TestRecord 11 22 33) :c #(* % 10))))

  (testing "'updated' values beyond bounds; update function must accept nil"
    (are [x y] (= x y)
      [11 22 33 nil :beyond-end] (update* [11 22 33] 4 #(if (nil? %) :beyond-end))
      {:a 11 :b 22 :c 33 :d "foo"} (update* {:a 11 :b 22 :c 33} :d #(if (nil? %) "foo"))
      '(11 22 33 nil :past-end-of-list) (update* '(11 22 33) 4 #(if (nil? %) :past-end-of-list))
      #{11 22 33 :not-in-set} (update* #{11 22 33} 44 #(if (nil? %) :not-in-set))
      '(11 22 33 nil :beyond-end) (update* (subvec [11 22 33] 0 3) 4 #(if (nil? %) :beyond-end))))

  (testing "updated set memmbers pruned because they're non-unique"
    (are [x y] (= x y)
      #{11 33} (update* #{11 22 33} 22 #(+ % 11))))

  (testing "testing supplied args"
    (are [x y] (= x y)
      [11 22 333 44 55] (update* [11 22 33 44 55] 2 + 300)
      [11 22 3333 44 55] (update* [11 22 33 44 55] 2 + 300 3000)
      [11 22 33333 44 55] (update* [11 22 33 44 55] 2 + 300 3000 30000)
      [11 22 333333 44 55] (update* [11 22 33 44 55] 2 + 300 3000 30000 300000)
      [0 1 2 33333 4 5] (update* (range 0 6) 3 + 30 300 3000 30000))))

(deftest dissoc*-test
  (testing "removing zero-th element, sequentials"
    (are [c-type coll result] (and (instance? c-type coll)
                                   (= result (dissoc* coll 0)))
      clojure.lang.PersistentVector (vector 11 22 33) [22 33]
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3) [22 33]
      ;;clojure.core.Vec (vector-of :int 11 22 33) [22 33]
      clojure.lang.MapEntry (first {11 22}) [22]

      clojure.lang.PersistentList (list 11 22 33) '(22 33)
      clojure.lang.Cons (cons 11 '(22 33)) '(22 33)

      clojure.lang.LazySeq (interleave [11 33] [22 44]) [22 33 44]
      clojure.lang.LazySeq (interpose 22 [11 33]) [22 33]
      clojure.lang.LazySeq (lazy-cat [11 22] [33]) [22 33]
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [22 33]
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]]) [22 33]
      clojure.lang.Repeat (repeat 3 11) [11 11]
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11]) [22 33]
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33]) [22 33]
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33]) [22 33]

      ;;clojure.lang.PersistentQueue (queue [11 22 33]) [22 33]
      clojure.lang.StringSeq (seq "abc") [\b \c]
      clojure.lang.PersistentVector (Tuple/create 11 22 33) [22 33]))

  (testing "removing middle element, sequentials"
    (are [c-type coll result] (and (instance? c-type coll)
                                   (= result (dissoc* coll 1)))
      clojure.lang.PersistentVector (vector 11 22 33) [11 33]
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3) [11 33]
      ;;clojure.core.Vec (vector-of :int 11 22 33) [11 33]
      clojure.lang.MapEntry (first {11 22}) [11]

      clojure.lang.PersistentList (list 11 22 33) '(11 33)
      clojure.lang.Cons (cons 11 '(22 33)) '(11 33)

      clojure.lang.LazySeq (interleave [11 33] [22 44]) [11 33 44]
      clojure.lang.LazySeq (interpose 22 [11 33]) [11 33]
      clojure.lang.LazySeq (lazy-cat [11 22] [33]) [11 33]
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [11 33]
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]]) [11 33]
      clojure.lang.Repeat (repeat 3 11) [11 11]
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11]) [11 33]
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33]) [11 33]
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33]) [11 33]

      ;;clojure.lang.PersistentQueue (queue [11 22 33]) [11 33]
      clojure.lang.StringSeq (seq "abc") [\a \c]
      clojure.lang.PersistentVector (Tuple/create 11 22 33) [11 33]))

  (testing "removing final element, sequentials"
    (are [c-type coll result] (and (instance? c-type coll)
                                   (= result (dissoc* coll 2)))
      clojure.lang.PersistentVector (vector 11 22 33) [11 22]
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3) [11 22]
      ;;clojure.core.Vec (vector-of :int 11 22 33) [11 22]

      clojure.lang.PersistentList (list 11 22 33) '(11 22)
      clojure.lang.Cons (cons 11 '(22 33)) '(11 22)

      clojure.lang.LazySeq (interleave [11 33] [22 44]) [11 22 44]
      clojure.lang.LazySeq (interpose 22 [11 33]) [11 22]
      clojure.lang.LazySeq (lazy-cat [11 22] [33]) [11 22]
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [11 22]
      clojure.lang.LazySeq (mapcat reverse [[22 11] [33]]) [11 22]
      clojure.lang.Repeat (repeat 3 11) [11 11]
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11]) [11 22]
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33]) [11 22]
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33]) [11 22]

      ;;clojure.lang.PersistentQueue (queue [11 22 33]) [11 22]
      clojure.lang.StringSeq (seq "abc") [\a \b]
      clojure.lang.PersistentVector (Tuple/create 11 22 33) [11 22]))

  (testing "removing an element, non-terminating sequences"
    (are [c-type coll] (and (instance? c-type coll)
                            (= [11 33 44] (take 3 (dissoc* coll 1))))
      clojure.lang.Cycle (cycle [11 22 33 44])
      clojure.lang.Iterate (iterate #(+ 11 %) 11)
      clojure.lang.LongRange (range 11 55 11)))

  (testing "removing an element, associatives and sets"
    (are [c-type coll key-or-val result] (and (instance? c-type coll)
                                              (= result (dissoc* coll key-or-val)))
      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33) :b {:a 11 :c 33}
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33) :b {:a 11 :c 33}
      clojure.lang.PersistentTreeMap (sorted-map :a 11 :b 22 :c 33) :b {:a 11 :c 33}
      clojure.lang.PersistentArrayMap (zipmap [:a :b :c] [11 22 33]) :b {:a 11 :c 33}
      ;;clojure.lang.PersistentStructMap (struct test-struct 11 22 33) :b {:a 11 :c 33}
      fn_in.core_tests.TestRecord (->TestRecord 11 22 33) :b {:a 11 :c 33}

      clojure.lang.PersistentHashSet (hash-set 11 22 33) 22 #{11 33}
      clojure.lang.PersistentTreeSet (sorted-set 11 22 33) 22 #{11 33})))


#_(run-tests)


(get nil 0) ;; nil
(assoc nil :a 99) ;; {:a 99}
(update nil :a (constantly 99)) ;; {:a 99}
(dissoc nil 0) ;; nil

(get (int-array [11 22 33]) 2) ;; 33

