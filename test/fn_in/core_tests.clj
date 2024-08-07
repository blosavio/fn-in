(ns fn-in.core-tests
  (:require
   [fn-in.core :refer :all]
   [clojure.test :refer [are is deftest testing run-tests]]
   [clojure.string :as string]))


(deftest get*-test
  (testing "empty collections"
    (are [x] (= nil x)
      (get* '() 0)
      (get* [] 0)
      (get* {} :a)
      (get* #{} 0)
      (get* (cycle []) 0)
      (get* (lazy-seq []) 0)
      (get* (interleave [] []) 0)
      (get* (interpose nil []) 0)
      (get* (lazy-cat) 0)
      (get* (mapcat + []) 0)
      (get* (zipmap [] []) :a)))

  (testing "zero-th index"
    (are [x] (= 11 x)
      (get* '(11 22 33 44 55) 0)
      (get* [11 22 33 44 55] 0)
      (get* #{11 22 33 44 55} 11)
      (get* (cycle [11 22 33 44 55]) 0)
      (get* (iterate inc 11) 0)
      (get* (lazy-seq [11 22 33 44 55]) 0)
      (get* (repeat 11) 0)
      (get* (cons 11 '(22 33 44 55)) 0)
      (get* (interleave [11 22 33] [:a :b :c]) 0)
      (get* (interpose :foo [11 22 33]) 0)
      (get* (lazy-cat [11 22 33] [44 55 66]) 0)
      (get* (mapcat reverse [[33 22 11] [66 55 44] [99 88 77]]) 0)))

  (testing "provided indexes"
    (are [x] (= 33 x)
      (get* '(11 22 33 44 55) 2)
      (get* [11 22 33 44 55] 2)
      (get* {:a 11 :b 22 :c 33 :d 44 :e 55} :c)
      (get* {:a 11 :b 22 :c 33 :d 44
             :e 55 :f 66 :g 77 :h 88 :i 99} :c)
      (get* (sorted-map :a 11 :b 22 :c 33 :d 44 :e 55) :c)
      (get* #{11 22 33 44 55} 33)
      (get* (sorted-set 11 22 33 44 55) 33)
      (get* (cycle [11 22 33 44 55]) 2)
      (get* (iterate inc 11) 22)
      (get* (lazy-seq [11 22 33 44 55]) 2)
      (get* (range) 33)
      (get* (range 0 100) 33)
      (get* (repeat 33) 2)
      (get* (cons 11 '(22 33 44 55)) 2)
      (get* (interleave [:a :b :c] [11 22 33]) 5)
      (get* (interpose :foo [11 22 33]) 4)
      (get* (lazy-cat [11 22 33] [44 55 66]) 2)
      (get* (mapcat reverse [[33 22 11] [66 55 44] [99 88 77]]) 2)
      (get* (zipmap [:a :b :c] [11 22 33]) :c)))

  (testing "composite keys on maps"
    (are [x y] (= x y)
      :vector-key
      (get* {[1 2] :vector-key
             {:a :b} :map-key
             '(11 22) :list-key} [1 2])

      :map-key
      (get* {[1 2] :vector-key
             {:a :b} :map-key
             '(11 22) :list-key} {:a :b})

      :list-key
      (get* {[1 2] :vector-key
             {:a :b} :map-key
             '(11 22) :list-key} '(11 22))))

  (testing "composite indexes on sets"
    (are [x y] (= x y)
      [22 33]
      (get* #{{:a 11} [22 33] '(44 55)} [22 33])

      {:a 11}
      (get* #{{:a 11} [22 33] '(44 55)} {:a 11})

      nil
      (get* #{{:a 11} [22 33] '(44 55)} {:b 99})

      '(44 55)
      (get* #{{:a 11} [22 33] '(44 55)} '(44 55))

      #{66 77}
      (get* #{{:a 11} [22 33] '(44 55) #{66 77}} #{66 77}))))


(deftest get-in*-test
  (testing "empty collections"
    (are [x] (= nil x)
      (get-in* [] [1 2])
      (get-in* '() [1 2])
      (get-in* {} [:a :b])
      (get-in* #{} [1 2])
      (get-in* (cycle []) [1 2])
      (get-in* (lazy-seq []) [1 2])
      (get-in* (interleave [] []) [1 2])
      (get-in* (interpose nil []) [0])
      (get-in* (lazy-cat) [0])
      (get-in* (mapcat + []) [0])
      (get-in* (zipmap [] []) [:a])))

  (testing "key/index not found"
    (are [x] (= nil x)
      (get-in* [11 22 33 44 55] [5])
      (get-in* '(11 22 33 44 55) [5])
      (get-in* {:a 11 :b 22 :c 33 :d 44 :e 55} [:f])
      (get-in* #{11 22 33 44 55} [66])
      (get-in* (lazy-seq [11 22 33 44 55]) [5])
      (get-in* (cons 11 '(22 33 44 55)) [5])))

  (testing "empty path vectors addresses the whole input collection"
    (are [x y] (= x y)
      [11 22 33] (get-in* [11 22 33] [])
      {:a 11 :b 22 :c 33} (get-in* {:a 11 :b 22 :c 33} [])
      '(11 22 33) (get-in* '(11 22 33) [])
      #{11 22 33} (get-in* #{11 22 33} [])
      '(11 22 33 44 55) (get-in* (cons 11 '(22 33 44 55)) [])))

  (testing "zero-th indexes"
    (are [x] (= 11 x)
      (get-in* [[11 22] [33 44]] [0 0])
      (get-in* '((11 22) (33 44)) [0 0])
      (get-in* {:a {:b 11 :c 22} :d {:e 33 :f 44}} [:a :b])
      (get-in* #{[11 22 33] [44 55 66]} [[11 22 33] 0])
      (get-in* (cycle [[11 22]]) [0 0])
      (get-in* (iterate #(vector (inc (% 0))) [11]) [0 0])
      (get-in* (lazy-seq [[11 22]]) [0 0])
      (get-in* (repeat [11 22]) [0 0])
      (get-in* (cons '(11) '(22 33 44 55)) [0 0])
      (get-in* (interleave [11 22 33] [:a :b :c]) [0])
      (get-in* (interpose :foo [11 22 33]) [0])
      (get-in* (lazy-cat [11 22 33] [44 55 66]) [0])
      (get-in* (mapcat reverse [[33 22 11] [66 55 44] [99 88 77]]) [0])
      (get-in* (zipmap [:a :b :c] [11 22 33]) [:a])))

  (testing "provided indexes"
    (are [x] (= 33 x)
      (get-in* [[11 22] [[33] 44]] [1 0 0])
      (get-in* {:a {:b 11 :c 22} :d {:e {:f 33 :g 44}}} [:d :e :f])
      (get-in* '(11 (22) ((33 44) 55)) [2 0 0])
      (get-in* #{true [:v \c 33] {:a 11 :b 22}} [[:v \c 33] 2])
      (get-in* (cycle [[11 [33]]]) [1 1 0])
      (get-in* (iterate #(vector "abc "\c (inc (% 2))) ["x" \q 11]) [22 2])
      (get-in* (lazy-seq [[11 22] [[33] 44]]) [1 0 0])
      (get-in* (range) [33])
      (get-in* (range 1000) [33])
      (get-in* (repeat [11 [22 [33]]]) [4 1 1 0])
      (get-in* (cons 11 '((22 (33 44 55)))) [1 1 0])
      (get-in* (interleave [:a :b :c] [[11 22 33] [44 55 66] [77 88 99]]) [1 2])
      (get-in* (interpose :foo [[11 22 33] [44 55 66] [77 88 99]]) [0 2])
      (get-in* (lazy-cat [11 [22 33]] [44 [55 66]]) [1 1])
      (get-in* (mapcat identity [[11 22 33] [[44 55 66]][77 88 99]]) [2])
      (get-in* (zipmap [:a :b :c] [[11 22 33] [44 55 66] [77 88 99]]) [:a 2])))

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
      (get-in* [11 22 {:a 33 :b (iterate inc 1)}] [2 :b 54])))

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
                                                 77]))))

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



(deftest assoc*-test
  (testing "empty collections"
    (are [x y] (= x y)
      [nil nil :new-vector-element]
      (assoc* [] 2 :new-vector-element)

      {:c :new-assoc-val}
      (assoc* {} :c :new-assoc-val)

      '(nil nil :new-list-element)
      (assoc* '() 2 :new-list-element)

      #{:new-set-element}
      (assoc* #{} 33 :new-set-element)

      '(11 22 99 44 55)
      (take 5 (assoc* (cycle [11 22 33 44 55]) 2 99))

      '(11 12 99 14 15)
      (take 5 (assoc* (iterate inc 11) 2 99))

      '(11 22 99 44 55)
      (take 5 (assoc* (lazy-seq [11 22 33 44 55]) 2 99))

      '(0 1 99 3 4)
      (take 5 (assoc* (range) 2 99))

      '(0 1 99 3 4)
      (take 5 (assoc* (range 0 1000) 2 99))

      '(11 11 99 11 11)
      (take 5 (assoc* (repeat 11) 2 99))

      '(11 22 99 44 55)
      (assoc* (cons 11 '(22 33 44 55)) 2 99)

      '(:foo)
      (assoc* (interleave [] []) 0 :foo)

      '(:baz)
      (assoc* (interpose nil []) 0 :baz)

      '(:foo)
      (assoc* (lazy-cat) 0 :foo)

      '(:foo)
      (assoc* (mapcat + []) 0 :foo)

      {:a 99}
      (assoc* (zipmap [] []) :a 99)))

  (testing "at the beginning of the collection"
    (are [x y] (= x y)
      [:new-vector-element 2 3 4 5]
      (assoc* [1 2 3 4 5] 0 :new-vector-element)

      '(:new-list-element 22 33 44 55)
      (assoc* '(11 22 33 44 55) 0 :new-list-element)

      (assoc* (interleave [:a :b :c] [1 2 3]) 0 :foo)
      '(:foo 1 :b 2 :c 3)

      (assoc* (lazy-cat [1 2 3] [4 5 6]) 0 :foo)
      '(:foo 2 3 4 5 6)

      (assoc* (mapcat reverse [[3 2 1] [6 5 4] [9 8 7]]) 0 :foo)
      '(:foo 2 3 4 5 6 7 8 9)))

  (testing "in the middle of the collection"
    (are [x y] (= x y)
      [1 2 :new-vector-element 4 5]
      (assoc* [1 2 3 4 5] 2 :new-vector-element)

      {:a 11 :b 22 :c :new-assoc-val :d 44 :e 55}
      (assoc* {:a 11 :b 22 :c 33 :d 44 :e 55} :c :new-assoc-val)

      '(11 22 :new-list-element 44 55)
      (assoc* '(11 22 33 44 55) 2 :new-list-element)

      #{11 22 :new-set-element 44 55}
      (assoc* #{11 22 33 44 55} 33 :new-set-element)

      (assoc* (interpose :foo ["a" "b" "c"]) 2 :baz)
      '("a" :foo :baz :foo "c")

      (assoc* (lazy-cat [1 2 3] [4 5 6]) 2 :foo)
      '(1 2 :foo 4 5 6)))

  (testing "at the end of the collection"
    (are [x y] (= x y)
      [1 2 3 4 :new-vector-element]
      (assoc* [1 2 3 4 5] 4 :new-vector-element)

      '(11 22 33 44 :new-list-element)
      (assoc* '(11 22 33 44 55) 4 :new-list-element)

      (assoc* (interpose :foo ["a" "b" "c"]) 4 :baz)
      '("a" :foo "b" :foo :baz)

      (assoc* (lazy-cat [1 2 3] [4 5 6]) 5 :foo)
      '(1 2 3 4 5 :foo)))

  (testing "beyond the end of the collection"
    (are [x y] (= x y)
      [11 22 33 nil nil 99]
      (assoc* [11 22 33] 5 99)

      '(11 22 33 nil nil 99)
      (assoc* '(11 22 33) 5 99)

      {:a 11 :b 22 :c 33 :d 99}
      (assoc* {:a 11 :b 22 :c 33} :d 99)

      #{11 22 33 44}
      (assoc* #{11 22 33} 44 44)

      #{11 22 33 55}
      (assoc* #{11 22 33} 44 55)

      '(11 22 33 nil nil 99)
      (assoc* (cons 11 '(22 33)) 5 99)))

  (testing "assoc-ing over a map's pre-existing key-value pair"
    (are [x y] (= x y)
      {:a 11 :b 99 :c 33}
      (assoc* {:a 11 :b 22 :c 33} :b 99)

      (assoc* (zipmap [:a :b :c] [1 2 3]) :a 99)
      {:a 99, :b 2, :c 3}))

  (testing "assoc-ing away non-unique set members"
    (are [x y] (= x y)
      #{11 33}
      (assoc* #{11 22 33} 22 33))))


(deftest update*-test
  (testing "updated values within bounds"
    (are [x y] (= x y)
      [11 22 330 44 55]
      (update* [11 22 33 44 55] 2 #(* % 10))

      '(11 22 1033 44 55)
      (update* '(11 22 33 44 55) 2 #(+ % 1000))

      {:a 11 :b 22 :c 33/10}
      (update* {:a 11 :b 22 :c 33} :c #(/ % 10))

      #{11 22 3}
      (update* #{11 22 33} 33 #(/ % 11))

      '(11 22 330 11 22 33 11 22 33)
      (take 9 (update* (cycle [11 22 33]) 2 #(* 10 %)))

      [11 12 23 14 15 16]
      (take 6 (update* (iterate inc 11) 2 #(+ 10 %)))

      [11 22 1 44 55]
      (update* (lazy-seq [11 22 33 44 55]) 2 #(/ % 33))

      [0 1 200 3 4 5]
      (take 6 (update* (range) 2 #(* % 100)))

      [0 1 "2" 3 4 5]
      (update* (range 0 6) 2 #(str %))

      '(11 11 12 11 11 11)
      (take 6 (update* (repeat 11) 2 inc))

      '(0 1 2 103 4 5)
      (take 6 (update* (range) 3 #(+ 100 %)))

      '(11 22 66 44 55)
      (update* (cons 11 '(22 33 44 55)) 2 #(+ 33 %))

      (update* (interleave [:a :b :c] [1 2 3]) 2 (constantly :bar))
      '(:a 1 :bar 2 :c 3)

      (update* (lazy-cat [1 2 3] [4 5 6]) 2 (constantly :baz))
      '(1 2 :baz 4 5 6)

      (update* (mapcat reverse [[3 2 1] [6 5 4] [9 8 7]]) 4 (constantly :foo))
      '(1 2 3 4 :foo 6 7 8 9)

      (update* (zipmap [:a :b :c] [1 2 3]) :a (constantly 'foo))
      {:a 'foo, :b 2, :c 3}))

  (testing "'updated' values beyond bounds; update function must accept nil"
    (are [x y] (= x y)
      [11 22 33 nil :beyond-end]
      (update* [11 22 33] 4 #(if (nil? %) :beyond-end))

      {:a 11 :b 22 :c 33 :d "foo"}
      (update* {:a 11 :b 22 :c 33} :d #(if (nil? %) "foo"))

      '(11 22 33 nil :past-end-of-list)
      (update* '(11 22 33) 4 #(if (nil? %) :past-end-of-list))

      #{11 22 33 :not-in-set}
      (update* #{11 22 33} 44 #(if (nil? %) :not-in-set))))

  (testing "updated set memmbers pruned because they're non-unique"
    (are [x y] (= x y)
      #{11 33}
      (update* #{11 22 33} 22 #(+ % 11))))

  (testing "testing supplied args"
    (are [x y] (= x y)
      [11 22 333 44 55]
      (update* [11 22 33 44 55] 2 + 300)

      [11 22 3333 44 55]
      (update* [11 22 33 44 55] 2 + 300 3000)

      [11 22 33333 44 55]
      (update* [11 22 33 44 55] 2 + 300 3000 30000)

      [11 22 333333 44 55]
      (update* [11 22 33 44 55] 2 + 300 3000 30000 300000)

      [0 1 2 33333 4 5]
      (update* (range 0 6) 3 + 30 300 3000 30000))))


(deftest assoc-in*-test
  (testing "un-nested collections"
    (are [x y] (= x y)
      [11 99 33]
      (assoc-in* [11 22 33] [1] 99)

      {:a 11 :b 99 :c 33}
      (assoc-in* {:a 11 :b 22 :c 33} [:b] 99)

      '(11 22 99 44 55)
      (assoc-in* '(11 22 33 44 55) [2] 99)

      #{11 22 99 44 55}
      (assoc-in* #{11 22 33 44 55} [33] 99)

      (assoc-in* (interleave [:a :b :c] [1 2 3]) [2] :foo)
      '(:a 1 :foo 2 :c 3)

      (assoc-in* (lazy-cat [1 2 3] [4 5 6]) [2] :foo)
      '(1 2 :foo 4 5 6)))

  (testing "homogeneous nested collection"
    (are [x y] (= x y)
      [11 [22 [99]]]
      (assoc-in* [11 [22 [33]]] [1 1 0] 99)

      {:a 11 :b {:c 22 :d 99}}
      (assoc-in* {:a 11 :b {:c 22 :d 33}} [:b :d] 99)

      '(11 (22 (99)))
      (assoc-in* '(11 (22 (33))) [1 1 0] 99)

      #{#{#{11 22 99}}}
      (assoc-in* #{#{#{11 22 33}}} [#{#{11 22 33}}
                                    #{11 22 33}
                                    33] 99)
      '(11 (22 (99 (44 (55)))))
      (assoc-in* (cons 11 '((22 (33 (44 (55)))))) [1 1 0] 99)

      (assoc-in* (interleave [:a :b :c] [[11 22 33] [44 55 66] [77 88 99]]) [3 2] :foo)
      '(:a [11 22 33] :b [44 55 :foo] :c [77 88 99])

      (assoc-in* (lazy-cat [11 [22 33]] [44 [55 66]]) [3 1] :foo)
      '(11 [22 33] 44 [55 :foo])

      (assoc-in* (mapcat identity [[11 22 33] [[44 55 66]][77 88 99]]) [3 2] :foo)
      '(11 22 33 [44 55 :foo] 77 88 99)))

  (testing "heterogeneous nested collection"
    (are [x y] (= x y)
      {:a 0 :b {:c 11 :d {:e [22 99]}}}
      (assoc-in* {:a 0 :b {:c 11 :d {:e [22 33]}}} [:b :d :e 1] 99)

      [11 "foo" {:a 22 :b '(33 44 #{99})}]
      (assoc-in* [11 "foo" {:a 22 :b '(33 44 #{55})}] [2 :b 2 55] 99)

      '(11 22 [33 44 (55 {:a 66, :b 99})])
      (assoc-in* '(11 22 [33 44 (55 {:a 66 :b 77})]) [2 2 1 :b] 99)

      #{'(99) [22] 11}
      (assoc-in* #{11 [22] '(33)} ['(33) 0] 99)

      #{[22] {:a 33, :b '(44 99)} 11}
      (assoc-in* #{11 [22] {:a 33 :b '(44 55)}} [{:a 33 :b '(44 55)} :b 1] 99)

      [11 22 33 [44 55 '(66 77 88 66 99 88)]]
      (assoc-in* [11 22 33 [44 55 (take 6 (cycle [66 77 88]))]] [3 2 4] 99)

      [11 22 [33 {:a 44, :b (list 11 12 13 99 15 16)}]]
      (assoc-in* [11 22 [33 {:a 44 :b (take 6 (iterate inc 11))}]] [2 1 :b 3] 99)

      {:a 11, :b [22 [33 [44 '(55 99 77)]]]}
      (assoc-in* {:a 11 :b [22 [33 [44 (lazy-seq [55 66 77])]]]} [:b 1 1 1 1] 99)

      (list 11 22 [33 '(0 1 2 99 4 5)])
      (assoc-in* (list 11 22 [33 (take 6 (range))]) [2 1 3] 99)

      {:a 11, :b :foo, :c [22 (list 0 1 99 3 4 5) 33]}
      (assoc-in* {:a 11 :b :foo :c [22 (take 6 (range 0 1000)) 33]} [:c 1 2] 99)

      #{22 [33 '(44 44 44 99 44 44)] 11}
      (assoc-in* #{11 22 [33 (take 6 (repeat 44))]} [[33 (take 6 (repeat 44))] 1 3] 99)

      '([11 22] [33 99] [11 22] [33 44])
      (take 4 (assoc-in* (cycle [[11 22] [33 44]])  [1 1] 99))

      '([11 22] [12 220] [13 99] [14 22000])
      (take 4 (assoc-in* (iterate (fn [[x y]] (vector (+ 1 x) (* 10 y))) [11 22]) [2 1] 99))

      '(11 22 [44 55 [66 99]])
      (assoc-in* (lazy-seq [11 22 [44 55 [66 77]]]) [2 2 1] 99)

      '(11 22 {:a 44 :b 55 :c [66 99]})
      (assoc-in* (lazy-seq [11 22 {:a 44 :b 55 :c [66 77]}]) [2 :c 1] 99)

      '([11 22 33] [11 22 33] [11 99 33])
      (take 3 (assoc-in* (repeat [11 22 33]) [2 1] 99)))))


(deftest update-in*-test
  (testing "homogeneous collections"
    (are [x y] (= x y)
      [11 22 33 [44 55 [66 77 0]]]
      (update-in* [11 22 33 [44 55 [66 77 88]]] [3 2 2] #(- % 88))

      {:a 11 :b 22 :c {:d 33 :e {:f 88}}}
      (update-in* {:a 11 :b 22 :c {:d 33 :e {:f 44}}} [:c :e :f] #(* % 2))

      '(11 22 33 (44 55 (66 77 111)))
      (update-in* '(11 22 33 (44 55 (66 77 88))) [3 2 2] #(+ % 23))

      #{[11 22] {:a 33} #{11}}
      (update-in* #{[11 22] {:a 33} #{44}} [#{44} 44] #(/ % 4))

      '(11 22 (33 88))
      (update-in* (cons 11 (list 22 (cons 33 (list 44)))) [2 1] #(+ 44 %))

      (update-in* (interleave [:a :b :c] [1 2 3]) [0] (constantly :bar))
      '(:bar 1 :b 2 :c 3)

      (update-in* (lazy-cat [1 2 3] [4 5 6]) [2] (constantly :baz))
      '(1 2 :baz 4 5 6)

      (update-in* (zipmap [:a :b :c] [1 2 3]) [:a] (constantly 'foo))
      {:a 'foo, :b 2, :c 3}

      (update-in* (interleave [:a :b :c] [[11 22 33] [44 55 66] [77 88 99]]) [3 2] (constantly :bar))
      '(:a [11 22 33] :b [44 55 :bar] :c [77 88 99])))

  (testing "heterogeneous collections"
    (are [x y] (= x y)
      {:a 11 :b [11 27 33]}
      (update-in* {:a 11 :b [11 22 33]} [:b 1] + 5)

      '(11 22 {:a 33 :b [44 1 66]})
      (update-in* '(11 22 {:a 33 :b [44 55 66]}) [2 :b 1] #(- % 54))

      [11 22 {:a "foo" :b {:c "baz" :d '(33 44 #{[55 66] [77 444]})}}]
      (update-in* [11 22 {:a "foo" :b {:c "baz" :d '(33 44 #{[55 66] [77 88]})}}]
                  [2 :b :d 2 [77 88] 1]
                  #(+ 440 (/ % 22)))

      #{[33 88] [55 66] [11 22]}
      (update-in* #{[11 22] [33 44] [55 66]} [[33 44] 1] #(+ % 44))

      '([11 22] [33 440] [55 66] [11 22] [33 44] [55 66])
      (take 6 (update-in* (cycle [[11 22] [33 44] [55 66]]) [1 1] #(* 10 %)))

      '([11 22] [21 122000] [31 222] [41 322] [51 422] [61 522])
      (take 6 (update-in* (iterate (fn [[x y]] (vector (+ 10 x) (+ 100 y))) [11 22]) [1 1] #(* 1000 %)))

      '(11 22 {:a 33, :b [44 [5]]})
      (update-in* (lazy-seq [11 22 {:a 33 :b [44 [55]]}]) [2 :b 1 0] #(- % 50))

      [11 22 [33 '(0 "1" 2)]]
      (update-in* [11 22 [33 (take 3 (range))]]  [2 1 1] #(str %))

      {:a 11, :b [22 '(0 101 2 3) 33]}
      (update-in* {:a 11 :b [22 (range 0 4) 33]} [:b 1 1] #(+ 100 %))

      '([11 22 33] [11 220 33] [11 22 33])
      (take 3 (update-in* (repeat [11 22 33]) [1 1] #(* 10 %)))))

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
      (update-in* #{11 22 #{33}} [#{33} 44] (fn [_] 99)))))


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


(deftest dissoc*-test
  (testing "map"
    (are [x y] (= x y)
      {:a 11 :c 33}
      (dissoc* {:a 11 :b 22 :c 33} :b)

      {:a 11 :b 22 :c 33}
      (dissoc* {:a 11 :b 22 :c 33} :d)

      (dissoc* (zipmap [:a :b :c] [1 2 3]) :a)
      {:b 2, :c 3}))

  (testing "vector"
    (are [x y] (= x y)
      [22 33 44 55]
      (dissoc* [11 22 33 44 55] 0)

      [11 22 44 55]
      (dissoc* [11 22 33 44 55] 2)

      [11 22 33 44]
      (dissoc* [11 22 33 44 55] 4)))

  (testing "list"
    (are [x y] (= x y)
      '(22 33 44 55)
      (dissoc* '(11 22 33 44 55) 0)

      '(11 22 44 55)
      (dissoc* '(11 22 33 44 55) 2)

      '(11 22 33 44)
      (dissoc* '(11 22 33 44 55) 4)))

  (testing "set"
    (are [x y] (= x y)
      #{11 22}
      (dissoc* #{11 22 33} 33)

      #{11 22 33}
      (dissoc* #{11 22 33} 44)))

  (testing "non-terminating sequences"
    (are [x y] (= x y)
      (take 5 (dissoc* (cycle [0 1 2 3 4 5]) 3)) '(0 1 2 4 5)
      (take 5 (dissoc* (iterate inc 0) 3)) '(0 1 2 4 5)
      (dissoc* (lazy-seq [11 22 33 44 55 66]) 2) '(11 22 44 55 66)
      (take 5 (dissoc* (range) 3)) '(0 1 2 4 5)
      (take 5 (dissoc* (range 0 10) 3)) '(0 1 2 4 5)
      (take 5 (dissoc* (repeat 3) 3)) '(3 3 3 3 3)))

  (testing "cons"
    (is (= (dissoc* (cons 11 '(22 33)) 1) '(11 33))))

  (testing "other sequences"
    (are [x y] (= x y)
      (dissoc* (interleave [:a :b :c] [1 2 3]) 2)
      '(:a 1 2 :c 3)

      (dissoc* (interpose :foo ["a" "b" "c"]) 4)
      '("a" :foo "b" :foo)

      (dissoc* (lazy-cat [1 2 3] [4 5 6]) 0)
      '(2 3 4 5 6)

      (dissoc* (mapcat reverse [[3 2 1] [6 5 4] [9 8 7]]) 4)
      '(1 2 3 4 6 7 8 9))))


(deftest dissoc-in*-test
  (testing "un-nested"
    (are [x y] (= x y)
      [11 33]
      (dissoc-in* [11 22 33] [1])

      '(11 33)
      (dissoc-in* '(11 22 33) [1])

      {:a 11 :c 33}
      (dissoc-in* {:a 11 :b 22 :c 33} [:b])

      #{11 33}
      (dissoc-in* #{11 22 33} [22])

      '(11 33)
      (dissoc-in* (lazy-seq [11 22 33]) [1])

      '(11 22 (33 (44)))
      (dissoc-in* (cons 11 (list 22 (list 33 (list 44 55)))) [2 1 1])

      (dissoc-in* (interleave [:a :b :c] [1 2 3]) [2])
      '(:a 1 2 :c 3)

      (dissoc-in* (lazy-cat [1 2 3] [4 5 6]) [2])
      '(1 2 4 5 6)

      (dissoc-in* (mapcat reverse [[3 2 1] [6 5 4] [9 8 7]]) [4])
      '(1 2 3 4 6 7 8 9)))

  (testing "preserve empty containing collections"
    (are [x y] (= x y)
      []
      (dissoc-in* [11] [0])

      '()
      (dissoc-in* '(11) [0])

      {}
      (dissoc-in* {:a 11} [:a])

      #{}
      (dissoc-in* #{11} [11])

      [11 22 [33 44 []]]
      (dissoc-in* [11 22 [33 44 [55]]] [2 2 0])

      {:a 11 :b 22 :c {:d 33 :e 44 :f {}}}
      (dissoc-in* {:a 11 :b 22 :c {:d 33 :e 44 :f {:g 55}}} [:c :f :g])

      '(11 22 (33 44 ()))
      (dissoc-in* '(11 22 (33 44 (55))) [2 2 0])

      #{11 #{}}
      (dissoc-in* #{11 #{22}} [#{22} 22])

      '()
      (dissoc-in* (lazy-seq [11]) [0])

      '(11 22 ())
      (dissoc-in* (cons 11 (list 22 (list 33))) [2 0])))

  (testing "nested homogeneous"
    (are [x y] (= x y)
      [11 22 [33 44 [55]]]
      (dissoc-in* [11 22 [33 44 [55 66]]] [2 2 1])

      {:a 11 :b {:c 22 :d {:e 33}}}
      (dissoc-in* {:a 11 :b {:c 22 :d {:e 33 :f 44}}} [:b :d :f])

      '(11 22 (33 44 (66)))
      (dissoc-in* '(11 22 (33 44 (55 66))) [2 2 0])

      #{11 #{22 #{33}}}
      (dissoc-in* #{11 #{22 #{33 44}}}
                  [#{22 #{33 44}}
                   #{33 44}
                   44])

      '(11 22 [33 44 [55]])
      (dissoc-in* (lazy-seq [11 22 [33 44 [55 66]]]) [2 2 1])

      (dissoc-in* (interleave [:a :b :c] [[11 22 33] [44 55 66] [77 88 99]]) [3 2])
      '(:a [11 22 33] :b [44 55] :c [77 88 99])

      (dissoc-in* (interpose :foo [[11 22 33] [44 55 66] [77 88 99]]) [2 2])
      '([11 22 33] :foo [44 55] :foo [77 88 99])

      (dissoc-in* (lazy-cat [11 [22 33]] [44 [55 66]]) [3 1])
      '(11 [22 33] 44 [55])

      (dissoc-in* (mapcat identity [[11 22 33] [[44 55 66]][77 88 99]]) [3 2])
      '(11 22 33 [44 55] 77 88 99)))

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
      {:a [11 33], :b [44 55 66], :c [77 88 99]})))


(run-tests)