(ns fn-in.performance.benchmark-utils
  (:require
   [clojure.test :refer [are
                         is
                         deftest
                         run-test
                         run-tests
                         testing]]
   [fastester.measure :refer [range-pow-10]]))


;; Criterium measurements: transient construction is 5% faster than 'standard'


(defn create-seq-of-n-rand-ints
  [max-len]
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (vec (repeatedly k #(rand-int 99)))))
    (transient {})
    (range-pow-10 max-len))))


(defn create-vec-of-n-rand-ints
  [max-len]
  (persistent!
   (reduce (fn [m [k v]] (assoc! m k (vec v)))
           (transient {})
           (create-seq-of-n-rand-ints max-len))))


(defn create-list-of-n-rand-ints
  [max-len]
  (doall
   (reduce
    (fn [m k] (assoc m k (into (list) (repeatedly k #(rand-int 99)))))
    {}
    (range-pow-10 max-len))))


(defn create-map-of-n-key-vals
  [max-len]
  (persistent!
   (reduce
    (fn [m k] (assoc! m k (zipmap (range k) (repeatedly k #(rand-int 99)))))
    (transient {})
    (range-pow-10 max-len))))


(defn coll-of-n-rand-ints
  "Given collection type `coll-type`, and maximum length `max-len`, returns a
  hashmap with keys striding by factors or ten up to `max-len`, and values a
  collection of random integers.

  `coll-type` may be one of `:sequence`, `:vector`, `:list`, or `:map`."
  {:UUIDv4 #uuid "736dbfcd-1d65-49a1-a16d-1274ecb0a717"
   :no-doc true}
  [coll-type max-len]
  (case coll-type
    :sequence (create-seq-of-n-rand-ints max-len)
    :vector (create-vec-of-n-rand-ints max-len)
    :list (create-list-of-n-rand-ints max-len)
    :map (create-map-of-n-key-vals max-len)
    :no-match))


(defn set-comparator
  "Comparator for either:
  * a pair of intengers, or
  * a pair of sets.

  Examples:
  ```clojure
  (sorted-set-by set-comparator 1 2 3)
  ;; => #{1 2 3}

  (sorted-set-by set-comparator (sorted-set 2 1)
                                (sorted-set 4 3)
                                (sorted-set 6 5))
  ;; => #{#{1 2}
  ;;      #{3 4}
  ;;      #{5 6}}
  ```"
  {:UUIDv4 #uuid "6467db3e-77c1-4420-be2d-bfd66d330838"
   :no-doc true}
  [a b]
  (let [deep-first #(loop [s %]
                      (if (int? s)
                        s
                        (recur (first s))))]
    (< (deep-first a) (deep-first b))))


(defn partition-into
  "Returns a function that consumes `coll`, runs `partition` with `n` (which
  returns a sequence), then converts all sequences back to the original
  collection type.

  `coll` must be a vector, list, clojure.lang.Range, or sorted set.

  Example:
  ```clojure
  (partition-into 3 [1 2 3 4 5 6 7 8 9])
  ;; => [[1 2 3]
  ;;     [4 5 6]
  ;;     [7 8 9]]

  (partition-into 2 '(1 2 3 4))
  ;; => '((1 2) (3 4))
  ```"
  {:UUIDv4 #uuid "4b7ac9fb-6c55-4000-abbc-0de658ceabc4"
   :no-doc true}
  [n coll]
  (let [convert #(into (empty coll) %)
        coll (cond
               (vector? coll) coll
               (set? coll) coll
               :else (reverse coll))]
    (->>
     (partition n coll)
     (map convert)
     convert)))


(defn partition-map
  "Given sorted map `m` (typically with integer keys), returns a new sorted map
  with `n` partitions.

  Example:
  ```clojure
  (partition-map 2 {0 0 1 1 2 2 3 3})
  ;; => {0 {0 0
  ;;        1 1}
  ;;     1 {2 2
  ;;        3 3}}
  ```"
  {:UUIDv4 #uuid "269659f0-d52e-4d68-aa85-0a56a6630036"
   :no-doc true}
  [n m]
  (let [update-vals-f-m #(update-vals %2 %1)]
    (->> m
         (partition n)
         vec
         (reduce-kv (fn [m k v] (assoc m k v)) (sorted-map))
         (update-vals-f-m #(reduce (fn [m [k v]] (assoc m k v)) {} %))
         (into (sorted-map)))))



(deftest partition-map-tests
  (let [example-map (sorted-map 0 88, 1 89, 2 90, 3 91, 4 92, 5 93, 6 94, 7 95, 8 96, 9 97, 10 98, 11 99)]
    (are [x y] (= (partition-map x example-map) y)
      12 {0 {0 88, 1 89, 2 90, 3 91, 4 92, 5 93, 6 94, 7 95, 8 96, 9 97, 10 98, 11 99}}
      6 {0 {0 88, 1 89, 2 90, 3 91, 4 92, 5 93}, 1 {6 94, 7 95, 8 96, 9 97, 10 98, 11 99}}
      4 {0 {0 88, 1 89, 2 90, 3 91}, 1 {4 92, 5 93, 6 94, 7 95}, 2 {8 96, 9 97, 10 98, 11 99}}
      3 {0 {0 88, 1 89, 2 90}, 1 {3 91, 4 92, 5 93}, 2 {6 94, 7 95, 8 96}, 3 {9 97, 10 98, 11 99}}
      2 {0 {0 88, 1 89}, 1 {2 90, 3 91}, 2 {4 92, 5 93}, 3 {6 94, 7 95}, 4 {8 96, 9 97}, 5 {10 98, 11 99}}
      1 {0 {0 88}, 7 {7 95}, 1 {1 89}, 4 {4 92}, 6 {6 94}, 3 {3 91}, 2 {2 90}, 11 {11 99}, 9 {9 97}, 5 {5 93}, 10 {10 98}, 8 {8 96}}))
  (testing "larger map"
    (are [x y] (= x y)
      (partition-map 3 (into (sorted-map) (zipmap (range 0 27) (range 0 27))))
      {0 {0 0, 1 1, 2 2},
       1 {3 3, 4 4, 5 5},
       2 {6 6, 7 7, 8 8},
       3 {9 9, 10 10, 11 11},
       4 {12 12, 13 13, 14 14},
       5 {15 15, 16 16, 17 17},
       6 {18 18, 19 19, 20 20},
       7 {21 21, 22 22, 23 23},
       8 {24 24, 25 25, 26 26}})))


(deftest partition-into-tests
  (testing "vectors"
    (are [x y] (= (partition-into x [1 2 3 4 5 6 7 8 9 10 11 12])
                  y)
      12 [[1   2   3   4   5   6   7  8    9   10   11   12]]
      6  [[1   2   3   4   5   6] [7  8    9   10   11   12]]
      4  [[1   2   3   4] [5   6   7  8]  [9   10   11   12]]
      3  [[1   2   3] [4   5   6] [7  8    9] [10   11   12]]
      2  [[1   2] [3   4] [5   6] [7  8]  [9   10] [11   12]]
      1  [[1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12]])
    (testing "sorted sets"
      (are [x y] (= (partition-into x (sorted-set-by set-comparator 1 2 3 4 5 6 7 8))
                    y)
        8 #{(sorted-set 1 2 3 4 5 6 7 8)}
        4 (sorted-set-by set-comparator
                         (sorted-set 1 2 3 4)
                         (sorted-set 5 6 7 8))
        2 (sorted-set-by set-comparator
                         (sorted-set 1 2)
                         (sorted-set 3 4)
                         (sorted-set 5 6)
                         (sorted-set 7 8))
        1 #{#{1} #{2} #{3} #{4} #{5} #{6} #{7} #{8}}))
    (testing "lists"
      (are [x y] (= (partition-into x (list 1 2 3 4 5 6 7 8))
                    y)
        8 '((1 2 3 4 5 6 7 8))
        4 '((1 2 3 4) (5 6 7 8))
        2 '((1 2) (3 4) (5 6) (7 8))
        1 '((1) (2) (3) (4) (5) (6) (7) (8))))
    (testing "ranges"
      (are [x y] (= (partition-into x (range 1 9))
                    y)
        8 '((1 2 3 4 5 6 7 8))
        4 '((1 2 3 4) (5 6 7 8))
        2 '((1 2) (3 4) (5 6) (7 8))
        1 '((1) (2) (3) (4) (5) (6) (7) (8))))))


(defn nested-maps
  "Returns nested maps of dimension `n`.

  Example:
  ```clojure
  (nested-maps 2)
  ;; => {0 {0 0
  ;;        1 10}
  ;;     1 {2 20
  ;;        3 30}}
  ```"
  {:UUIDv4 #uuid "3b8949e3-4572-48c1-a97d-52fd22cab92d"
   :no-doc true}
  [d]
  (let [kys (range (Math/pow d d))
        vls (map #(* 10 %) kys)]
    (loop [i (dec d)
           coll (into (sorted-map) (zipmap kys vls))]
      (if (zero? i)
        coll
        (recur (dec i) (partition-map d coll))))))


(defn nested
  "Given dimension `d`, with `(<= 1 d)`, and keyword `coll-type`, returns a ball
  of nested collections of depth `d`, each with `d` elements. The return will
  contain `(Math/pow d d)` scalars.

  `coll-type` is one of `:vector`, `:map`, `:list`, `:set`, or `:sequence`.

  Example:
  ```clojure
  (nested 1 :sequence)
  ;; => (0)

  (nested 2 :set)
  ;; => #{#{0 1}
  ;;      #{2 3}}

  (nested 2 :map)
  ;; => {0 {0 0,
  ;;        1 10}
  ;;     1 {2 20
  ;;        3 30}}

  (nested 3 :vector)
  ;; => [[[0  1  2]
  ;;      [3  4  5]
  ;;      [6  7  8]]
  ;;     [[9  10 11]
  ;;      [12 13 14]
  ;;      [15 16 17]]
  ;;     [[18 19 20]
  ;;      [21 22 23]
  ;;      [24 25 26]]]```"
  {:UUIDv4 #uuid "26fc25fe-f962-49be-aa0d-0907d0285458"
   :no-doc true}
  [d coll-type]
  (if (<= 1 d)
    (if (= coll-type :map)
      (nested-maps d)
      (let [c ({:list #(into () (reverse %))
                :sequence identity
                :set #(apply sorted-set-by set-comparator %)
                :vector vec}
               coll-type)]
        (loop [i (dec d)
               coll (c (range (Math/pow d d)))]
          (if (zero? i)
            coll
            (recur (dec i) (partition-into d coll))))))
    (throw (Exception. "Dimension `d` must be ≥ 1."))))


(deftest nested-tests
  (testing "illegal dimension"
    (is (thrown? Exception ((nested 0 :vector)))))
  (testing "nested vectors"
    (are [x y] (= (nested x :vector) y)
      1 [0]
      2 [[0 1]
         [2 3]]
      3 [[[0 1 2]
          [3 4 5]
          [6 7 8]]
         [[9 10 11]
          [12 13 14]
          [15 16 17]]
         [[18 19 20]
          [21 22 23]
          [24 25 26]]]))
  (testing "nested hashmaps"
    (are [x y] (= (nested x :map) y)
      1 {0 0}

      2 {0 {0 0
            1 10}
         1 {2 20
            3 30}}

      3 {0 {0 {0 0
               1 10
               2 20}
            1 {3 30
               4 40
               5 50}
            2 {6 60
               7 70
               8 80}}
         1 {3 {9 90
               10 100
               11 110}
            4 {12 120
               13 130
               14 140}
            5 {15 150
               16 160
               17 170}}
         2 {6 {18 180
               19 190
               20 200}
            7 {21 210
               22 220
               23 230}
            8 {24 240
               25 250
               26 260}}}))
  (testing "nested lists"
    (are [x y] (= (nested x :list) y)
      1 '(0)
      2 '((0 1)
          (2 3))
      3 '(((0 1 2)
           (3 4 5)
           (6 7 8))
          ((9 10 11)
           (12 13 14)
           (15 16 17))
          ((18 19 20)
           (21 22 23)
           (24 25 26)))))
  (testing "nested sequences"
    (are [x y] (= (nested x :sequence) y)
      1 '(0)
      2 '((0 1)
          (2 3))
      3 '(((0 1 2)
           (3 4 5)
           (6 7 8))
          ((9 10 11)
           (12 13 14)
           (15 16 17))
          ((18 19 20)
           (21 22 23)
           (24 25 26)))))
  (testing "nested sets"
    (are [x y] (= (nested x :set) y)
      1 #{0}
      2 (sorted-set-by set-comparator
                       (sorted-set 0 1)
                       (sorted-set 2 3))
      3 (sorted-set-by set-comparator
                       (sorted-set-by set-comparator
                                      (sorted-set-by set-comparator 0 1 2)
                                      (sorted-set-by set-comparator 3 4 5)
                                      (sorted-set-by set-comparator 6 7 8))
                       (sorted-set-by set-comparator
                                      (sorted-set-by set-comparator 9 10 11)
                                      (sorted-set-by set-comparator 12 13 14)
                                      (sorted-set-by set-comparator 15 16 17))
                       (sorted-set-by set-comparator
                                      (sorted-set-by set-comparator 18 19 20)
                                      (sorted-set-by set-comparator 21 22 23)
                                      (sorted-set-by set-comparator 24 25 26)))))
  (testing "retrieving buried elements"
    (are [x y] (= x y)
      0 (get-in (nested 1 :vector) [0])
      3 (get-in (nested 2 :vector) [1 1])
      26 (get-in (nested 3 :vector) [2 2 2])
      255 (get-in (nested 4 :vector) [3 3 3 3])
      3124 (get-in (nested 5 :vector) [4 4 4 4 4])
      46655 (get-in (nested 6 :vector) [5 5 5 5 5 5]))))


(defn append
  "Returns `tail` appended to collection `coll` regardeless of collection type."
  {:UUIDv4 #uuid "f20bd199-80fb-423e-bc05-9011689b44c5"
   :no-doc true}
  [coll tail]
  (cond
    (set? coll) (conj coll tail)
    (vector? coll) (conj coll tail)
    (map? coll) (assoc coll (inc (apply max (keys coll))) tail)
    :else (reverse (reduce conj '() (conj (vec coll) tail)))))


(deftest append-tests
  (are [x y] (= x y)
    (append [1 2 3] [4 5 6])
    [1 2 3 [4 5 6]]

    (append '(1 2 3) '(4 5 6))
    '(1 2 3 (4 5 6))

    (append (sorted-set-by set-comparator 1 2 3)
            (sorted-set-by set-comparator 4 5 6))
    (sorted-set-by set-comparator 1 2 3 (sorted-set-by set-comparator 4 5 6))

    (append {1 10 2 20 3 30} {4 40 5 50 6 60})
    {1 10 2 20 3 30 4 {4 40 5 50 6 60}}

    (append (range 1 4) (range 4 7))
    '(1 2 3 (4 5 6))))


(defn narrow-deep
  "Returns a collection of type `:coll-type` containing `n` integers plus
  another collection of the same type at the tail, identical to the parent,
  until the leaf element, which contains only. Stops at `depth`.

  `coll-type` is one of `:vector`, `:map`, `:list`, or `:sequence`.

  Example:
  ```clojure

  ```"
  {:UUIDv4 #uuid "b69f8f6b-fbe5-4868-99dd-ad3c40a6df6e"
   :no-doc true}
  [coll-type n depth]
  (let [to-coll ({:list #(into () (reverse %))
                  :sequence identity
                  :map (fn [s] (zipmap s (map #(* 10 %) s)))
                  :vector vec}
                 coll-type)]
    (if (= 0 depth)
      (to-coll (range 0 n))
      (append (to-coll (range 0 n))
              (narrow-deep coll-type n (dec depth))))))


(deftest narrow-deep-tests
  (testing "vectors"
    (are [x y] (= x y)
      (narrow-deep :vector 3 4)
      [0 1 2
       [0 1 2
        [0 1 2
         [0 1 2
          [0 1 2]]]]]

      (narrow-deep :vector 3 3)
      [0 1 2
       [0 1 2
        [0 1 2
         [0 1 2]]]]

      (narrow-deep :vector 3 2)
      [0 1 2
       [0 1 2
        [0 1 2]]]

      (narrow-deep :vector 3 1)
      [0 1 2
       [0 1 2]]

      (narrow-deep :vector 3 0)
      [0 1 2]))
  (testing "non-vectors"
    (are [x y] (= (narrow-deep x 3 4) y)
      :map {0 0
            1 10
            2 20
            3 {0 0
               1 10
               2 20
               3 {0 0
                  1 10
                  2 20
                  3 {0 0
                     1 10
                     2 20
                     3 {0 0
                        1 10
                        2 20}}}}}

      :list
      '(0 1 2
          (0 1 2
             (0 1 2
                (0 1 2
                   (0 1 2)))))

      :sequence
      '(0 1 2
          (0 1 2
             (0 1 2
                (0 1 2
                   (0 1 2))))))))


#_(run-tests)

