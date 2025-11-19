(ns fn-in.in-tests
  "Unit tests for `get-in*`, `assoc-in*`, `update-in*`, and `dissoc-in*`.

  See also `core-tests` and `helper-tests`."
  (:require
   [clojure.test :refer [are
                         deftest
                         is
                         testing
                         run-tests
                         run-test]]
   [fn-in.core-tests :refer [queue]]
   [fn-in.core :refer [assoc-in*
                       dissoc-in*
                       get-in*
                       update-in*]])
  (:import (clojure.lang Tuple)))


(defrecord TestRecord [a b c])


(deftest get-in*-test
  (testing "`nil`"
    (are [f1 f2] (= (f1 nil [:a])
                    (f2 nil [:a]))
      get-in
      get-in*))

  (testing "empty collections"
    (are [c-type coll] (and (instance? c-type coll)
                            (nil? (get-in* coll [1])))
      clojure.lang.PersistentVector []
      clojure.lang.IPersistentList ()
      clojure.lang.PersistentArrayMap {}
      clojure.lang.PersistentHashSet #{}
      clojure.lang.LazySeq (lazy-seq [])))

  (testing "key/index/value not existing"
    (are [c-type coll path] (and (instance? c-type coll)
                                 (nil? (get-in* coll path)))
      clojure.lang.PersistentVector [11 22 33] [3]
      clojure.lang.PersistentList '(11 22 33) [3]
      clojure.lang.PersistentArrayMap {:a 11 :b 22 :c 33} [:d]
      clojure.lang.PersistentHashSet #{11 22 33} [44]
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [3]))

  (testing "empty path vectors addresses the whole input collection"
    (are [c-type coll] (and (instance? c-type coll)
                            (= coll (get-in* coll [])))
      clojure.lang.PersistentVector [11 22 33]
      clojure.lang.PersistentList '(11 22 33)
      clojure.lang.PersistentArrayMap {:a 11 :b 22 :c 33}
      clojure.lang.PersistentHashSet #{11 22 33}
      clojure.lang.LazySeq (lazy-seq [11 22 33])))

  (testing "zero-th indexes"
    (are [c-type coll path] (and (instance? c-type coll)
                                 (= 11 (get-in* coll path)))
      clojure.lang.PersistentVector [11 [22 [33]]] [0]
      clojure.lang.PersistentList '(11 '(22 '(33))) [0]
      clojure.lang.LazySeq (lazy-seq [11 [22 [33]]]) [0]))

  (testing "provided indexes"
    (are [c-type coll path] (and (instance? c-type coll)
                                 (= 33 (get-in* coll path)))
      clojure.lang.PersistentVector [11 [22 [33]]] [1 1 0]
      clojure.lang.PersistentList '(11 (22 (33))) [1 1 0]
      clojure.lang.PersistentArrayMap {:a {:b {:c 33}}} [:a :b :c]
      clojure.lang.PersistentHashSet #{[:v \c [33]]} [[:v \c [33]] 2 0]
      clojure.lang.LazySeq (lazy-seq [11 [22 [33]]]) [1 1 0]))

  (testing "heterogeneous indexes"
    (are [x] (= 55)
      (get-in* [{:a 11 :b [22 33]} {:d [44 [55]] :e 66}] [1 :d 1 0])
      (get-in* {:a [[11 22] {:b 33 :c [44 {:d [55 66]}]}]} [:a 1 :c 1 :d 0])
      (get-in* [[] {:a 11 :b #{55}}] [1 :b 55])
      (get-in* (lazy-seq [11 {:a 22 :c [33 '(44 55)]}]) [1 :c 1 1])
      (get-in* (repeat {:a 11 :b [22 33 44 55]}) [99 :b 3])
      (get-in* (cycle [[11 22 33] {:a 44 :b [55 66]}]) [3 :b 0])
      (get-in* [11 22 (lazy-seq [33 44 55])] [2 2])
      (get-in* {:a 11 :b (repeat 55)} [:b 99])
      (get-in* (list 11 22 (cycle [44 55 66])) [2 7])
      (get-in* [11 22 {:a 33 :b (iterate inc 1)}] [2 :b 54])
      (get-in* (subvec [{:a 11 :b (subvec [22 33] 0 2)} {:d (subvec [44 (subvec [55] 0 1)] 0 2) :e 66}] 0 2) [1 :d 1 0])))

  (testing "composite keys on maps"
    (are [x y] (= x y)
      55 (get-in* {:a 11 [22 33 44] {:b 55}} [[22 33 44] :b])
      :vector-key (get-in* {[1 2 3] :vector-key {:a 1} :map-key '(7 8 9) :list-key} [[1 2 3]])
      33 (get-in* {:a "a" :b "b" (range 1 3) [11 22 (repeat 33) 44 55]} [(range 1 3) 2 99])))

  (testing "nested sets"
    (are [x] (= 77 x)
      (get-in* #{55 66 77} [77])
      (get-in* #{#{55 66} #{77 88}} [#{77 88} 77])
      (get-in* #{#{#{11 22 33 #{77}} 44 55} 66} [#{#{11 22 33 #{77}} 44 55}
                                                 #{11 22 33 #{77}}
                                                 #{77}
                                                 77])))

  (testing "not-found"
    (are [c-type coll path] (and (instance? c-type coll)
                                 (= :not-found (get-in* coll path :not-found)))
      clojure.lang.PersistentVector (vector 11 [22 [33]]) [1 1 1]
      clojure.lang.PersistentList (list 11 '(22 (33))) [1 1 1]
      clojure.lang.PersistentArrayMap (array-map :a {:b {:c 99}}) [:a :b :d]
      clojure.lang.PersistentHashSet (hash-set 11 #{22 #{33}}) [#{22 #{33}} #{33} 99]
      clojure.lang.LazySeq (lazy-seq [11 [22 [33]]]) [1 1 1])))


(deftest assoc-in*-test
  (testing "`nil`"
    (are [f1 f2] (= (f1 nil [:a] 99)
                    (f2 nil [:a] 99))
      assoc-in
      assoc-in*))

  (testing "un-nested collections"
    (are [c-type coll path result] (and (instance? c-type coll)
                                        (= result (assoc-in* coll path 99)))
      clojure.lang.PersistentVector [11 22 33] [1] [11 99 33]
      clojure.lang.PersistentList '(11 22 33) [1] '(11 99 33)
      clojure.lang.PersistentArrayMap {:a 11 :b 22 :c 33} [:b] {:a 11 :b 99 :c 33}
      clojure.lang.PersistentHashSet #{11 22 33} [22] #{11 99 33}
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [1] (lazy-seq [11 99 33])))

  (testing "homogeneous nested collection"
    (are [x y] (= x y)
      [11 [22 [99]]] (assoc-in* [11 [22 [33]]] [1 1 0] 99)
      {:a {:b {:c 99}}} (assoc-in* {:a {:b {:c 33}}} [:a :b :c] 99)
      '(11 (22 (99))) (assoc-in* '(11 (22 (33))) [1 1 0] 99)

      #{#{#{11 22 99}}}
      (assoc-in* #{#{#{11 22 33}}} [#{#{11 22 33}}
                                    #{11 22 33}
                                    33] 99)

      [11 [22 [99]]] (assoc-in* (lazy-seq [11 (lazy-seq [22 (lazy-seq [33])])]) [1 1 0] 99)))

  (testing "heterogeneous nested collections"
    (are [coll path result] (= (assoc-in* coll path 99) result)
      [11 {:a '(22 #{33})}] [1 :a 1 33] [11 {:a '(22 #{99})}]
      {:a '(11 [22 #{33}])} [:a 1 1 33] {:a '(11 [22 #{99}])}
      '(11 [22 {:a #{33}}]) [1 1 :a 33] '(11 [22 {:a #{99}}])
      #{[11 {:a '(22 33)}]} [[11 {:a '(22 33)}] 1 :a 1] #{[11 {:a '(22 99)}]}))

  (testing "throwing when supplied with empty path"
    (is (thrown? Exception (assoc-in* {:a 99} [] :foo)))))


(deftest update-in*-test
  (testing "`nil`"
    (are [f1 f2] (= (f1 nil [:a] (constantly 99))
                    (f2 nil [:a] (constantly 99)))
      update-in
      update-in*))

  (testing "un-nested collections"
    (are [c-type coll path result] (and (instance? c-type coll)
                                        (= result (update-in* coll path #(* % 10))))
      clojure.lang.PersistentVector [11 22 33] [2] [11 22 330]
      clojure.lang.PersistentList '(11 22 33) [2] '(11 22 330)
      clojure.lang.PersistentArrayMap {:a 11 :b 22 :c 33} [:c] {:a 11 :b 22 :c 330}
      clojure.lang.PersistentHashSet #{11 22 33} [33] #{11 22 330}
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [2] (lazy-seq [11 22 330])))

  (testing "homogeneous collections"
    (are [x y] (= x y)
      [11 [22 [99]]] (update-in* [11 [22 [33]]] [1 1 0] #(+ % 66))
      {:a {:b {:c 99}}} (update-in* {:a {:b {:c 33}}} [:a :b :c] #(+ % 66))
      '(11 (22 (99))) (update-in* '(11 (22 (33))) [1 1 0] #(+ % 66))
      #{#{99}} (update-in* #{#{33}} [#{33} 33] #(+ % 66))
      [11 [22 [99]]] (update-in* (lazy-seq [11 (lazy-seq [22 (lazy-seq [33])])]) [1 1 0] #(+ % 66))))

  (testing "heterogeneous nested collections"
    (are [coll path result] (= (update-in* coll path #(+ % 66)) result)
      [11 {:a '(22 #{33})}] [1 :a 1 33] [11 {:a '(22 #{99})}]
      {:a '(11 [22 #{33}])} [:a 1 1 33] {:a '(11 [22 #{99}])}
      '(11 [22 {:a #{33}}]) [1 1 :a 33] '(11 [22 {:a #{99}}])
      #{[11 {:a '(22 33)}]} [[11 {:a '(22 33)}] 1 :a 1] #{[11 {:a '(22 99)}]}))

  (testing "extra f args"
    (are [x y] (= x y)
      [:a {:b 11 :c 22 :d '(33 4)}]
      (update-in* [:a {:b 11 :c 22 :d '(33 44)}] [1 :d 1] #(/ %1 %2) 11)))

  (testing "map-as-a-function"
    (are [x y] (= x y)
      [:a :b 33]
      (update-in* [:a :b :c] [2] {:foo 11 :bar 22 :c 33})))

  (testing "updating a non-existing element on an existing level"
    (are [x y] (= x y)
      [:a :b :c nil nil 99]
      (update-in* [:a :b :c] [5] (fn [_] 99))

      [:a :b :c [:d :e nil :f]]
      (update-in* [:a :b :c [:d :e]] [3 3] (fn [_] :f))

      {:a 11 :b 22 :c 33 :d {:e 44 :f 99}}
      (update-in* {:a 11 :b 22 :c 33 :d {:e 44}} [:d :f] (fn [_] 99))

      '(11 22 33 (44 55 nil 99))
      (update-in* '(11 22 33 (44 55)) [3 3] (fn [_] 99))

      #{11 22 #{33 99}}
      (update-in* #{11 22 #{33}} [#{33} 44] (fn [_] 99))

      (update-in* (subvec [:a :b :c] 0 3) [5] (fn [_] 99))
      '(:a :b :c nil nil 99))))


(deftest dissoc-in*-test
  (testing "`nil`"
    (are [x] (= (nil? x))
      (dissoc-in* nil [:a])))

  (testing "un-nested"
    (are [c-type coll path result] (and (instance? c-type coll)
                                        (= result (dissoc-in* coll path)))
      clojure.lang.PersistentVector [11 22 33] [1] [11 33]
      clojure.lang.PersistentList '(11 22 33) [1] '(11 33)
      clojure.lang.PersistentArrayMap {:a 11 :b 22 :c 33} [:b] {:a 11 :c 33}
      clojure.lang.PersistentHashSet #{11 22 33} [22] #{11 33}
      clojure.lang.LazySeq (lazy-seq [11 22 33]) [1] (lazy-seq [11 33])))

  (testing "preserve empty containing collections, un-nested"
    (are [c-type coll path result] (and (instance? c-type coll)
                                        (= result (dissoc-in* coll path)))
      clojure.lang.PersistentVector [11] [0] []
      clojure.lang.PersistentList '(11) [0] ()
      clojure.lang.PersistentArrayMap {:a 11} [:a] {}
      clojure.lang.PersistentHashSet #{11} [11] #{}
      clojure.lang.LazySeq (lazy-seq [11]) [0] []))

  (testing "preservinng empty containing collections, nested"
    (are [coll path result] (= result (dissoc-in* coll path))
      [11 22 [33 44 [55]]] [2 2 0] [11 22 [33 44 []]]
      {:a 11 :b 22 :c {:d 33 :e 44 :f {:g 55}}} [:c :f :g] {:a 11 :b 22 :c {:d 33 :e 44 :f {}}}
      '(11 22 (33 44 (55))) [2 2 0] '(11 22 (33 44 ()))
      #{11 #{22}} [#{22} 22] #{11 #{}}))

  (testing "nested homogeneous"
    (are [x y] (= x y)
      [11 [22 []]] (dissoc-in* [11 [22 [33]]] [1 1 0])
      {:a {:b {}}} (dissoc-in* {:a {:b {:c 33}}} [:a :b :c])
      '(11 (22 ())) (dissoc-in* '(11 (22 (33))) [1 1 0])
      [11 [22 []]] (dissoc-in* (lazy-seq [11 (lazy-seq [22 (lazy-seq [33])])]) [1 1 0])

      #{11 #{22 #{33}}}
      (dissoc-in* #{11 #{22 #{33 44}}}
                  [#{22 #{33 44}}
                   #{33 44}
                   44])))

  (testing "nested heterogeneous"
    (are [x y] (= x y)
      [11 {:b 33}]
      (dissoc-in* [11 {:a 22 :b 33}] [1 :a])

      '({:a 11 :b [22 33]})
      (dissoc-in* '({:a 11 :b [22 33 "foo"]}) [0 :b 2])

      [11 {:a 22 :b '(33 #{44})}]
      (dissoc-in* [11 {:a 22 :b '(33 #{44 55})}] [1 :b 1 55])

      {:a 11 :b '(22 {:c 33 :d [44 66]})}
      (dissoc-in* {:a 11 :b '(22 {:c 33 :d [44 55 66]})} [:b 1 :d 1])

      '[11 22 {:a 33, :b (0 1 3 4)}]
      (dissoc-in* [11 22 {:a 33 :b (range 0 5)}] [2 :b 2])

      '([11 "a"] [22] [33 "c"] [11 "a"] [22 "b"] [33 "c"])
      (take 6 (dissoc-in* (cycle [[11 "a"] [22 "b"] [33 "c"]]) [1 1]))

      '([11 22] [21] [31 222] [41 322] [51 422] [61 522])
      (take 6 (dissoc-in* (iterate (fn [[x y]] (vector (+ 10 x) (+ 100 y))) [11 22]) [1 1]))

      {:a 11, :b [22 '(0 10 30 40 50)]}
      (dissoc-in* {:a 11 :b [22 (take 6 (map #(* 10 %) (range)))]} [:b 1 2])

      (list 11 [22 33 {:a 44, :b (list 5 6 7 9)}])
      (dissoc-in* (list 11 [22 33 {:a 44 :b (range 5 10)}]) [1 2 :b 3])

      [11 22 33 '([44 55 66] [44 66] [44 55 66])]
      (dissoc-in* [11 22 33 (take 3 (repeat [44 55 66]))] [3 1 1])

      (dissoc-in* (zipmap [:a :b :c] [[11 22 33] [44 55 66] [77 88 99]]) [:a 1])
      {:a [11 33], :b [44 55 66], :c [77 88 99]}

      (dissoc-in* (subvec [11 {:a 22 :b 33}] 0 2) [1 :a])
      [11 {:b 33}])))


(defrecord TestRecord [a b c])
(defstruct test-struct :a :b :c)

(deftest metadata-tests
  (testing "metadata returned from empty path `get-in*`"
    (are [c-type coll] (and (instance? c-type coll)
                            ((meta (get-in* (with-meta coll {:meta? true}) [])) :meta?))
      clojure.lang.PersistentVector (vector 11 22 33)
      clojure.lang.APersistentVector$SubVector (subvec [11 22 33] 0 3)
      clojure.core.Vec (vector-of :int 11 22 33)

      clojure.lang.PersistentList (list 11 22 33)
      clojure.lang.Cons (cons 11 '(22 33))

      clojure.lang.PersistentArrayMap (array-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentHashMap (hash-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentTreeMap (sorted-map :a 11 :b 22 :c 33)
      clojure.lang.PersistentStructMap (struct test-struct 11 22 33)
      fn_in.in_tests.TestRecord (->TestRecord 11 22 33)

      clojure.lang.PersistentHashSet (hash-set 11 22 33)
      clojure.lang.PersistentTreeSet (sorted-set 11 22 33)

      clojure.lang.Cycle (cycle [11 22 33])
      clojure.lang.LazySeq (interleave [11 22] [33 44])
      clojure.lang.LazySeq (interpose 22 [11 33])
      clojure.lang.LazySeq (lazy-cat [11 22] [33])
      clojure.lang.LazySeq (lazy-seq [11 22 33])
      clojure.lang.LazySeq (mapcat reverse [[22 11] [44 33]])
      clojure.lang.LongRange (range 0 4)
      clojure.lang.Range (range 11.0 44.0 11.0)
      clojure.lang.APersistentVector$RSeq (rseq [33 22 11])
      clojure.lang.PersistentVector$ChunkedSeq (seq [11 22 33])
      clojure.lang.PersistentVector$ChunkedSeq (sequence [11 22 33])

      clojure.lang.PersistentQueue (queue [11 22 33])
      clojure.lang.StringSeq (seq "abc")
      clojure.lang.PersistentVector (Tuple/create 11 22 33))))


#_(run-tests)

