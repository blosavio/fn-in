(ns fn-in.helper-tests
  "Unit tests for helper functions, i.e., functions not participating in the
  public API.

  See also `core-tests` and `in-tests`."
  (:require
   [clojure.test :refer [are
                         deftest
                         is
                         run-tests
                         run-test
                         testing]]
   [fn-in.core :refer :all]))


(deftest concat-list-tests
  (testing "one or two lists"
    (are [x y] (= x y)
      '() (concat-list '())
      '() (concat-list '() '())
      '(22) (concat-list '() '(22))
      '(11) (concat-list '(11) '())
      '(11 22) (concat-list '(11) '(22))))

  (testing "three lists"
    (are [x y] (= x y)
      '() (concat-list '() '() '())
      '(11) (concat-list '(11) '() '())
      '(22) (concat-list '() '(22) '())
      '(33) (concat-list '() '() '(33))
      '(11 22 33) (concat-list '(11) '(22) '(33))))

  (testing "two-element lists"
    (are [x y] (= x y)
      '(11 22 33 44) (concat-list '(11 22) '(33 44))
      '(11 22 33 44 55 66) (concat-list '(11 22) '(33 44) '(55 66))))

  (testing "asymmetric lists"
    (are [x y] (= x y)
      '(11 22 33) (concat-list '(11 22) '(33))
      '(11 22 33) (concat-list '(11) '(22 33))
      '(11 22 33 44 55 66) (concat-list '(11) '(22 33) '(44 55 66))))

  (testing "nested colls in a list"
    (are [x y] (= x y)
      '([] {}) (concat-list '() '([]) '({}))
      '([11] {:a 22} #{33} '(44)) (concat-list '([11]) '({:a 22}) '(#{33}) '('(44)))))

  (testing "output type"
    (are [x] (or (= x clojure.lang.PersistentList$EmptyList)
                 (= x clojure.lang.PersistentList))
      (type (concat-list '() '()))
      (type (concat-list '(11) '(22)))
      (type (concat-list '(11 22) '(33 44)))
      (type (concat-list '(11) '(33) '(33)))
      (type (concat-list '(11) '() '(22))))))


(deftest list-assoc-test
  (testing "empty lists"
    (are [x y] (= x y)
      '(99) (list-assoc '() 0 99)
      '(nil nil 99) (list-assoc '() 2 99)
      '(:empty-first) (list-assoc '() 0 :empty-first)
      '(nil nil nil :empty-fourth) (list-assoc '() 3 :empty-fourth)))
  (testing "single element lists"
    (are [x y] (= x y)
      '(99) (list-assoc '(11) 0 99)
      '(11 99) (list-assoc '(11) 1 99)
      '(11 nil 99) (list-assoc '(11) 2 99)))
  (testing "within bounds"
    (are [x y] (= x y)
      '(:new 2 3 4 5) (list-assoc '(1 2 3 4 5) 0 :new)
      '(1 2 :new 4 5) (list-assoc '(1 2 3 4 5) 2 :new)
      '(1 2 3 4 :new) (list-assoc '(1 2 3 4 5) 4 :new)))
  (testing "beyond end bound"
    (are [x y] (= x y)
      '(1 2 3 4 5 :beyond-end) (list-assoc '(1 2 3 4 5) 5 :beyond-end)
      '(1 2 3 nil nil :beyond-end) (list-assoc '(1 2 3) 5 :beyond-end)))
  (testing "sweeping through various positions"
    (are [x y] (= x y)
      '(99 22 33) (list-assoc '(11 22 33) 0 99)
      '(11 99 33) (list-assoc '(11 22 33) 1 99)
      '(11 22 99) (list-assoc '(11 22 33) 2 99)
      '(11 22 33 99) (list-assoc '(11 22 33) 3 99)
      '(11 22 33 nil 99) (list-assoc '(11 22 33) 4 99)))
  (testing "return type"
    (are [x] (or (= x clojure.lang.PersistentList$EmptyList)
                 (= x clojure.lang.PersistentList))
      (type (list-assoc '(11 22 33) 1 99)))))


(deftest vector-assoc-test
  (testing "empty vectors"
    (are [x y] (= x y)
      [99] (vector-assoc [] 0 99)
      [nil nil nil nil nil 99] (vector-assoc [] 5 99)))
  (testing "within bounds"
    (are [x y] (= x y)
      [99 22 33] (vector-assoc [11 22 33] 0 99)
      [11 99 33] (vector-assoc [11 22 33] 1 99)
      [11 22 99] (vector-assoc [11 22 33] 2 99)))
  (testing "beyond end bound"
    (are [x y] (= x y)
      [11 22 33 99] (vector-assoc [11 22 33] 3 99)
      [11 22 33 nil 99] (vector-assoc [11 22 33] 4 99)
      [11 22 33 nil nil 99] (vector-assoc [11 22 33] 5 99))))


(deftest map-assoc-test
  (testing "empty maps"
    (are [x y] (= x y)
      {:a 99} (map-assoc {} :a 99)
      {99 :nine-nine-val} (map-assoc {} 99 :nine-nine-val)
      {[11 22] :composite-keyed} (map-assoc {} [11 22] :composite-keyed)))
  (testing "non-empty maps"
    (are [x y] (= x y)
      {:a 11 :b 22 :c 33} (map-assoc {:c 33} :a 11 :b 22)
      {:a 11 :b 33} (map-assoc {:a 11 :b 22} :b 33)
      {0 "foo" 1 "bar" 2 "baz"} (map-assoc {0 "foo"} 1 "bar" 2 "baz")
      {[11 22] :val1 [33 44] :replaced-val [55 66] :new-val} (map-assoc {[11 22] :val1 [33 44] :val2} [33 44] :replaced-val [55 66] :new-val))))


(deftest non-term-assoc-tests
  (testing "clojure.lang.Cycle"
    (are [x y] (= x y)
      (take 10 (non-term-assoc (cycle [11 22 33 44 55]) 0 99)) '(99 22 33 44 55 11 22 33 44 55)
      (take 10 (non-term-assoc (cycle [11 22 33 44 55]) 1 99)) '(11 99 33 44 55 11 22 33 44 55)
      (take 10 (non-term-assoc (cycle [11 22 33 44 55]) 3 99)) '(11 22 33 99 55 11 22 33 44 55)
      (take 10 (non-term-assoc (cycle [11 22 33 44 55]) 6 99)) '(11 22 33 44 55 11 99 33 44 55)))

  (testing "clojure.lang.Iterate"
    (are [x y] (= x y)
      (take 10 (non-term-assoc (iterate inc 11) 0 99)) '(99 12 13 14 15 16 17 18 19 20)
      (take 10 (non-term-assoc (iterate inc 11) 1 99)) '(11 99 13 14 15 16 17 18 19 20)
      (take 10 (non-term-assoc (iterate inc 11) 3 99)) '(11 12 13 99 15 16 17 18 19 20)
      (take 10 (non-term-assoc (iterate inc 11) 6 99)) '(11 12 13 14 15 16 99 18 19 20)))

  (testing "clojure.lang.LazySeq"
    (are [x y] (= x y)
      (non-term-assoc (lazy-seq [11 22 33 44 55]) 0 99) '(99 22 33 44 55)
      (non-term-assoc (lazy-seq [11 22 33 44 55]) 1 99) '(11 99 33 44 55)
      (non-term-assoc (lazy-seq [11 22 33 44 55]) 3 99) '(11 22 33 99 55)
      (non-term-assoc (lazy-seq [11 22 33 44 55]) 5 99) '(11 22 33 44 55 99)
      ;; This is not my preferred behavior: I would rather nil-pad.
      ;;'(11 22 33 44 55 99) (non-term-assoc (lazy-seq [11 22 33 44 55]) 8 99)
      ))

  (testing "clojure.lang.Range"
    (are [x y] (= x y)
      (take 10 (non-term-assoc (range) 0 99)) '(99 1 2 3 4 5 6 7 8 9)
      (take 10 (non-term-assoc (range) 1 99)) '(0 99 2 3 4 5 6 7 8 9)
      (take 10 (non-term-assoc (range) 3 99)) '(0 1 2 99 4 5 6 7 8 9)))

  (testing "clojure.lang.LongRange"
    (are [x y] (= x y)
      (take 10 (non-term-assoc (range 0 10) 0 99)) '(99 1 2 3 4 5 6 7 8 9)
      (take 10 (non-term-assoc (range 0 10) 1 99)) '(0 99 2 3 4 5 6 7 8 9)
      (take 10 (non-term-assoc (range 0 10) 3 99)) '(0 1 2 99 4 5 6 7 8 9)))

  (testing "clojure.lang.Repeat"
    (are [x y] (= x y)
      (take 10 (non-term-assoc (repeat 11) 0 99)) '(99 11 11 11 11 11 11 11 11 11)
      (take 10 (non-term-assoc (repeat 11) 1 99)) '(11 99 11 11 11 11 11 11 11 11)
      (take 10 (non-term-assoc (repeat 11) 3 99)) '(11 11 11 99 11 11 11 11 11 11))))


(deftest set-assoc-tests
  (testing "empty sets"
    (are [x y] (= x y)
      #{:foo} (set-assoc #{} :foo :foo)
      #{[:foo]} (set-assoc #{} [:foo] [:foo])))
  (testing "non-empty sets"
    (are [x y] (= x y)
      #{11 :foo 33} (set-assoc #{11 22 33} 22 :foo)))
  (testing "associating non-existing val"
    (are [x y] (= x y)
      #{11 22 33 :foo} (set-assoc #{11 22 33} :not-there :foo))))


(deftest mult-asooc*-tests
  (are [x y] (= x y)
    {:a 1} (mult-assoc* {} :a 1)
    {:a 1 :b 2} (mult-assoc* {} :a 1 :b 2)
    {:a 1 :b 2 :c 3} (mult-assoc* {} :a 1 :b 2 :c 3)))


(deftest vector-dissoc-test
  (are [x y] (= x y)
    [11 22 44 55]
    (vector-dissoc [11 22 33 44 55] 2)

    [22 33 44 55]
    (vector-dissoc [11 22 33 44 55] 0)

    [11 22 33 44]
    (vector-dissoc [11 22 33 44 55] 4))
  (testing "ensure return type is a vector"
    (are [x y] (= x y)
      clojure.lang.PersistentVector
      (type (vector-dissoc [11 22 33] 1)))))


(deftest list-dissoc-test
  (are [x y] (= x y)
    '(22 33 44 55)
    (list-dissoc '(11 22 33 44 55) 0)

    '(11 22 44 55)
    (list-dissoc '(11 22 33 44 55) 2)

    '(11 22 33 44)
    (list-dissoc '(11 22 33 44 55) 4)

    '(11 22 33 44 55)
    (list-dissoc '(11 22 33 44 55) 5)
    )
  (testing "ensure return type is list"
    (are [x y] (= x y)
      clojure.lang.PersistentList$EmptyList
      (type (list-dissoc '(11) 0))

      clojure.lang.PersistentList
      (type (list-dissoc '(11 22 33) 0)))))


(deftest non-term-dissoc-tests
  (testing "various index locations"
    (are [x y] (= x y)
      (non-term-dissoc (lazy-seq []) 0) '()
      (non-term-dissoc (lazy-seq [11]) 0) '()
      (non-term-dissoc (lazy-seq [11 22 33 44 55 66]) 0) '(22 33 44 55 66)
      (non-term-dissoc (lazy-seq [11 22 33 44 55 66]) 2) '(11 22 44 55 66)
      (non-term-dissoc (lazy-seq [11 22 33 44 55 66]) 5) '(11 22 33 44 55)
      (non-term-dissoc (lazy-seq [11 22 33 44 55 66]) 6) '(11 22 33 44 55 66)
      (non-term-dissoc (lazy-seq [11 22 33 44 55 66]) 99) '(11 22 33 44 55 66)))

  (testing "non-terminating sequences"
    (are [x y] (= x y)
      (take 5 (non-term-dissoc (cycle [0 1 2 3 4 5]) 3)) '(0 1 2 4 5)
      (take 5 (non-term-dissoc (iterate inc 0) 3)) '(0 1 2 4 5)
      (take 5 (non-term-dissoc (range) 3)) '(0 1 2 4 5)
      (take 5 (non-term-dissoc (range 0 10) 3)) '(0 1 2 4 5)
      (take 5 (non-term-dissoc (repeat 3) 3)) '(3 3 3 3 3))))


#_(run-tests)

