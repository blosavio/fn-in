(ns fn-in.core
  "This namespace provides functions to:

  * inspect an element (`get`)
  * change an element (`assoc`)
  * apply a function to an element (`update`)
  * remove an element (`dissoc`)

  from **any** of Clojure's collection types (plus non-terminating sequences),
  similarly to their `clojure.core` namesakes. The `...-in` variants operate on
  any heterogeneous, arbitrarily-nested data structure.

  * Elements contained in vectors, lists, and other sequences are addressed by
    zero-indexed integers.
  * Elements contained in maps are addressed by key, which may be any valid
    Clojure value, including a composite.
  * Elements contained in sets are addressed by the element's value, which may
    be a composite.

  Conventions:

  * `c` collection
  * `i` index/key
  * `x` value
  * `f` function
  * `arg1`, `arg2`, `arg3` optional args to multi-arity function `f`.")


(load "collection_hierarchy")


(comment ;; dev-time convenience functions
  ;;  (ns-unmap *ns* 'get*) ; (defmulti) macro only binds the var within (when-not), so must manually unmap it to update it
  ;;  (remove-all-methods get*)
  )


(defmulti get*
  "`(get* c i)`

  Inspect the value at location `i` within a collection `c`. Similar to
  [`clojure.core/get`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get)
  . Returns `nil` if not found.

  Note: `get*` does not offer a `not-found` arity.

  Examples:
  ```clojure
  (get* [11 22 33 44 55] 2) ;; => 33
  (get* {:x 11 :y 22 :z 33} :y) ;; => 22
  (get* (list 11 22 33 44 55) 2) ;; => 33
  (get* #{11 22 33} 22) ;; => 22
  (get* (range 99) 3) ;; => 3
  (get* (cycle [11 22 33]) 5) ;; => 33

  ;; non-keyword key
  (get* {[11 22 33] 'val-1 99 'val-2} 99) ;; => val-2
  ```"
  {:UUIDv4 #uuid "4cbfffbd-b67c-4a22-b6ca-983e6b62e091"}
  (fn [c _] (type c)))

(defmethod get* ::map [c i] (c i))
(defmethod get* ::vector [c i] (get c i))
(defmethod get* ::list [c i] (nth c i nil))
(defmethod get* ::set [c x] (if (c x) x nil))
(defmethod get* ::non-terminating [c i] (nth c i nil))
(defmethod get* nil [c i] nil)


(defn get-in*
  "Inspects the value in heterogeneous, arbitrarily-nested collection `c` at
  `path`, a vector of indexes/keys. This version of
  [`clojure.core/get-in`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get-in)
  operates on all Clojure collections. An empty `path` vector returns the
  original collection `c`. Performance is not optimized, so it might steal your
  lunch money.

  Examples:
  ```clojure
  (get-in* [11 22 33 [44 [55]]] [3 1 0]) ;; => 55
  (get-in* {:a 11 :b {:c 22 :d {:e 33}}} [:b :d :e]) ;; => 33
  (get-in* (list 11 22 [33 (list 44 (list 55))]) [2 1 1 0]) ;; => 55
  (get-in* #{11 [22 [33]]} [[22 [33]] 1 0]) ;; => 33

  ;; empty path addresses root collection
  (get-in* [11 22 33] []) ;; => [11 22 33]

  ;; heterogeneous, nested collections; return may be a collection
  (get-in* {:a [11 22 {:b [33 [44] 55 [66]]}]} [:a 2 :b 3]) ;; => [66]

  ;; address of a nested set; compare to next example
  (get-in* [11 {:a [22 #{33}]}] [1 :a 1   ]) ;; => #{33}

  ;; address of an element contained within a nested set; compare to previous example
  (get-in* [11 {:a [22 #{33}]}] [1 :a 1 33]) ;; => 33

  ;; non-terminating sequence
  (get-in* (repeat [11 22 33]) [3 1]) ;; => 22

  ;; nested non-terminating sequences
  (get-in* (repeat (cycle [:a :b :c])) [99 5]) ;; => :c
  ```"
  {:UUIDv4 #uuid "0e59acfb-ec3a-450c-99f6-8e1f8c01e4c5"}
  [c path]
  (reduce get* c path))


(defn concat-list
  "Returns a (concat)-ed list of the supplied lists.
   clojure.core/concat defaults to returning a lazy sequence.
   So explicitly stuff that lazy seq back into an empty list,
   then reverse it because (conj) on a list occurs at its head."
  {:UUIDv4 #uuid "dc0c3cd4-10b6-480c-ade1-159c8d4c2425"
   :no-doc true}
  [& lists]
  (reverse (into '() (apply concat lists))))


(defn list-assoc
  "Returns a new list with the element at index idx replaced with value x.
   If idx is beyond the end, nil entries are appended."
  {:UUIDv4 #uuid "f73dc5d7-87e1-4bb7-8c36-6ec41fa38052"
   :no-doc true}
  [coll idx val]
  ((fn assoc-sub [head tail k]
     (if (zero? k)
       (concat-list head (list val) (rest tail))
       (assoc-sub (concat-list head (list (first tail)))
                  (rest tail)
                  (dec k))))
   (empty coll) coll idx))


(defn vector-assoc
  "Returns a new vector with the element at index i replaced with value x."
  {:UUIDv4 #uuid "4d4a4386-d08c-4501-9b52-67f7eaf685cf"
   :no-doc true}
  [c i x]
  (let [length (count c)]
    (if (< i length)
      (assoc c i x)
      (concat c (repeat (- i length) nil) [x]))))


(defn map-assoc
  "Returns a new map with the element at keyword i replaced with value x."
  {:UUIDv4 #uuid "6f5044bc-acab-4d23-b92d-5b304e859420"
   :no-doc true
   :implementation-note "Inspired by built-in (assoc)."}
  ([c i x] (assoc c i x))
  ([c i x & i_s]
   (let [a (assoc c i x)]
     (if i_s
       (recur a (first i_s) (second i_s) (nnext i_s))
       a))))


(defn non-term-assoc
  "Returns a new (possibly) non-terminating sequence with the element at i
  replaced with value x. clojure.lang.{Cycle,Iterate,LazySeq,Range,LongRange,Repeat}

   Note: assoc*-ing a value at any index beyond the end of a LazySeq will always
   insert it at the nth+1 index, instead of my preferred nil-padding. It is
   impossible to determine a LazySeq's count without realizing it, and even then
   it might be infinite."
  {:UUIDv4 #uuid "fee4b366-b8e3-477c-bfe4-2e7b6defc014"
   :no-doc true}
  [c i x]
  (lazy-cat (take i c) (vector x) (nthrest c (inc i))))


;; (ns-unmap *ns* 'assoc*)
;; (remove-all-methods assoc*)


(defmulti assoc*
  "`(assoc* c i val)`

  Returns a new collection `c` with the key/index `i` associated with the
  supplied value `val`. Similar to
  [`clojure.core/assoc`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/assoc)
  , but operates on all Clojure collections. Indexes beyond the end of a
  sequence are padded with `nil`.

  Note: Because set members are addressed by their value, the `assoc*`-ed value
  may match a pre-existing set member, and the returned set may have one fewer
  members.

  Examples:
  ```clojure
  (assoc* [11 22 33] 1 99) ;; => [11 99 33]
  (assoc* {:a 11 :b 22} :b 99) ;; => {:a 11, :b 99}
  (assoc* (list 11 22 33) 1 99) ;; => (11 99 33)
  (assoc* #{11 22 33} 22 99) ;; => #{99 33 11}
  (assoc* (range 3) 1 99) ;; => (0 99 2)
  (assoc* (take 6 (iterate dec 10)) 3 99) ;; => (10 9 8 99 6 5)

  ;; associating an existing set member reduces the size of the set
  (assoc* #{11 22 33} 22 33) ;; => #{33 11}

  ;; associating a value into a non-terminating sequence
  (take 5 (assoc* (repeat 3) 2 99)) ;; => (3 3 99 3 3)

  ;; associating a value beyond a sequence's bounds causes nil-padding
  (assoc* [11 22 33] 5 99) ;; => (11 22 33 nil nil 99)
  ```"
  {:UUIDv4 #uuid "f73dc5d7-87e1-4bb7-8c36-6ec41fa38052"}
  (fn [c _ _] (type c)))

(defmethod assoc* ::map [c i x] (assoc c i x))
(defmethod assoc* ::vector [c i x] (vector-assoc c i x))
(defmethod assoc* ::list [c i x] (list-assoc c i x))
(defmethod assoc* ::set [c i x] (conj (disj c i) x))
(defmethod assoc* ::non-terminating [c i x] (non-term-assoc c i x))
(defmethod assoc* nil [c _ _] nil)


(defn assoc-in*
  "Returns collection `c` with a new value `x` associated at path vector of
  `i` elements. Similar to [`clojure.core/assoc-in`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/assoc-in)
  , but operates on any heterogeneous, arbitrarily-nested collections. Supplying
  an empty path throws an exception. Associating beyond the end of sequence
  results in `nil`-padding.

  Examples:
  ```clojure
  (assoc-in* [11 22 33 [44 55 [66 77]]] [3 2 1] :foo) ;; => [11 22 33 [44 55 [66 :foo]]]
  (assoc-in* {:a {:b {:c 42}}} [:a :b :c] 99) ;; => {:a {:b {:c 99}}}
  (assoc-in* (list 11 22 [33 44 (list 55)]) [2 1] :foo) ;; => (11 22 [33 :foo (55)])
  (assoc-in* #{11 [22 #{33}]} [[22 #{33}] 1 33] :hello) ;; => #{11 [22 #{:hello}]}

  ;; heterogeneous, nested collections
  (assoc-in* [11 {:a [22 {:b 33}]}] [1 :a 1 :b] 42) ;; => [11 {:a [22 {:b 42}]}]
  (assoc-in* {'foo (list 11 {22/7 'baz})} ['foo 1 22/7] :new-val) ;; => {foo (11 {22/7 :new-val})}

  ;; associating beyond nested sequence's bounds causes nil-padding
  (assoc-in* [11 22 [33 44]] [2 3] 99) ;; => [11 22 (33 44 nil 99)]

  ;; associating a non-existent key-val in a map merely expands the map
  (assoc-in* {:a 11 :b {:c 22}} [:b :d] 99) ;; => {:a 11, :b {:c 22, :d 99}}
  ```"
  {:UUIDv4 #uuid "e32d8f77-84d3-4830-82ad-369682e9eb9b"}
  [c [i & i_s] x]
  (if i_s
    (assoc* c i (assoc-in* (get* c i) i_s x))
    (assoc* c i x)))


(defn update*
  "Returns a new collection `c` with function `f` applied to the value at
  location `i`. If the location doesn't exist, `nil`
  is passed to `f`. Additional arguments `args` may be supplied trailing `f`.
  Similar to [`clojure.core/update`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/update)
  , but operates on all Clojure collections.

  Note: Because set members are addressed by their value, the `update*`-ed value
  may match a pre-existing set member, and the returned set may have one fewer
  members.

  Examples:
  ```clojure
  (update* [10 20 30] 1 dec) ;; => [10 19 30]
  (update* {:a 10} :a dec) ;; => {:a 9}
  (update* (list 10 20 30) 1 dec) ;; => (10 19 30)
  (update* #{10 20 30} 20 dec) ;; => #{19 30 10}

  ;; function handles nil if no value exists
  (update* [11 22 33] 4 (constantly :updated-val)) ;; => (11 22 33 nil :updated-val)

  ;; additional args
  (update* {:a 99} :a #(/ %1 %2) 9) ;; => {:a 11}

  ;; update absorbs existing set member resulting in a smaller set
  (update* #{32 33} 33 dec) ;; => #{32}
  ```"
  {:UUIDv4 #uuid "a55f146f-51a1-46ff-87b0-af188a86aa7e"}
  ([c i f] (assoc* c i (f (get* c i))))
  ([c i f arg1] (assoc* c i (f (get* c i) arg1)))
  ([c i f arg1 arg2] (assoc* c i (f (get* c i) arg1 arg2)))
  ([c i f arg1 arg2 arg3] (assoc* c i (f (get* c i) arg1 arg2 arg3)))
  ([c i f arg1 arg2 arg3 & more] (assoc* c i (apply f (get* c i) arg1 arg2 arg3 more))))


(defn update-in*
  "Returns a new collection `c` with the value at path vector `ks` updated by
  applying function `f` to the previous value. Similar to
  [`clojure.core/update-in`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/update-in)
  , but operates on any heterogeneous, arbitrarily-nested collection. Additional
  arguments `args` may be supplied trailing `f`. If location `ks` does not exist,
  `f` must handle `nil`.

  Note: Updating a set member to another previously-existing set member will
  decrease the size of the set.

  Examples:
  ```clojure
  (update-in* [11 [22 [33]]] [1 1 0] inc) ;; => [11 [22 [34]]]
  (update-in* {:a {:b {:c 99}}} [:a :b :c] inc) ;; => {:a {:b {:c 100}}}
  (update-in* (list 11 [22 (list 33)]) [1 1 0] inc) ;; => (11 [22 (34)])
  (update-in* #{11 [22 #{33}]} [[22 #{33}] 1 33] inc) ;; => #{[22 #{34}] 11}

  ;; heterogeneous nested collections
  (update-in* [11 {:a 22 :b (list 33 44)}] [1 :b 1] inc) ;; => [11 {:a 22, :b (33 45)}]
  (update-in* {:a [11 #{22}]} [:a 1 22] #(* % 2)) ;; => {:a [11 #{44}]}

  ;; beyond end of sequence
  (+ nil) ;; => nil
  (update-in* [11 22 33] [3] +) ;; => (11 22 33 nil)

  ;; non-existent key-val
  (not nil) ;; => true
  (update-in* {:a {:b 11}} [:a :c] not) ;; => {:a {:b 11, :c true}}

  ;; updating a set member to an existing value
  (update-in* #{11 12} [11] inc) ;; => #{12}

  ;; additional args
  (update-in* [11 [22 [99]]] [1 1 0] #(/ %1 %2) 3) ;; => [11 [22 [33]]]
  ```"
  {:UUIDv4 #uuid "cc817a8e-2ba4-433d-b085-74da32747e29"}
  ([m ks f & args]
   (let [up (fn up [m ks f args]
              (let [[k & ks] ks]
                (if ks
                  (assoc* m k (up (get* m k) ks f args))
                  (assoc* m k (apply f (get* m k) args)))))]
     (up m ks f args))))


(defn vector-dissoc
  "Remove element at index `i` from vector `v`"
  {:UUIDv4 #uuid "70e34715-9354-4614-9623-655c7378f769"
   :no-doc true}
  [v i]
  (into [] (concat (subvec v 0 i) (subvec v (+ i 1)))))


(defn list-dissoc
  "Remove element at index `i` from list `lst`."
  {:UUIDv4 #uuid "dc78c19c-8727-45e1-bcf2-bd17ad424b0b"
   :no-doc true}
  [lst i]
  ((fn dissoc-sub [head tail k]
     (if (zero? k)
       (concat-list head (rest tail))
       (dissoc-sub (concat-list head (list (first tail)))
                  (rest tail)
                  (dec k))))
   (empty lst) lst i))


(defn non-term-dissoc
  "Remove element at index `i` from non-terminating sequence `c`.
   If the `c` happens to have a terminus, out-of-bounds `i` returns the original
   sequence."
  {:UUIDv4 #uuid "bdae0204-ea2b-4cc3-bae7-a24e4e50c336"
   :no-doc true}
  [c i]
  (lazy-cat (take i c) (nthrest c (inc i))))


;; (ns-unmap *ns* 'dissoc*)
;; (remove-all-methods dissoc*)


(defmulti dissoc*
  "`(dissoc* c i)`

  Returns a new collection `c` with the value located at key/index `i` removed.
  Similar to
  [`clojure.core/dissoc`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/dissoc)
  , but operates on all Clojure collections. `i` must be within the bounds of a
  sequence. If element at `i` does not exist in a map or set, the new returned
  collection is identical. Similarly, `dissoc`-ing an element from a
  `clojure.lang.Repeat` returns an indistinguishable sequence.

  Examples:
  ```clojure
  (dissoc* [11 22 33] 1) ;; => [11 33]
  (dissoc* {:a 11 :b 22} :a) ;; => {:b 22}
  (dissoc* (list 11 22 33) 1) ;; => (11 33)
  (dissoc* #{11 22 33} 22) ;; => #{33 11}
  (dissoc* (take 3 (cycle [:a :b :c])) 1) ;; => (:a :c)

  ;; non-existent entity
  (dissoc* #{11 22 33} 99) ;; => #{33 22 11}
  (dissoc* {:a 11 :b 22} :c) ;; => {:a 11, :b 22}
  ```"
  {:UUIDv4 #uuid "7433bb55-ecfb-4ce8-bd10-bbf8a5dbd0ed"}
  (fn [c _] (type c)))

(defmethod dissoc* ::map [c i] (dissoc c i))
(defmethod dissoc* ::vector [c i] (vector-dissoc c i))
(defmethod dissoc* ::list [c i] (list-dissoc c i))
(defmethod dissoc* ::set [c x] (disj c x))
(defmethod dissoc* ::non-terminating [c x] (non-term-dissoc c x))
(defmethod dissoc* nil [c _ _] nil)


(defn dissoc-in*
  "Remove element located at `i` from an arbitrarily-nested collection `c`. Any
  containing collections are preserved if `i` addresses a single scalar. If `i`
  addresses a nested collection, all children are removed.

  Examples:
  ```clojure
  (dissoc-in* [11 [22 [33 44]]] [1 1 0]) ;; => [11 [22 [44]]]
  (dissoc-in* {:a 11 :b {:c 22 :d 33}} [:b :c]) ;; => {:a 11, :b {:d 33}}
  (dissoc-in* (list 11 (list 22 33)) [1 0]) ;; => (11 (33))
  (dissoc-in* #{11 [22 33]} [[22 33] 0]) ;; => #{[33] 11}
  (dissoc-in* [11 (range 4)] [1 2]) ;; => [11 (0 1 3)]

  ;; heterogeneous, nested collections
  (dissoc-in* [11 {:a (list 22 [33 44])}] [1 :a 1 0]) ;; => [11 {:a (22 [44])}]
  (dissoc-in* {:a 11 :b [22 #{33 44}]} [:b 1 33]) ;; => {:a 11, :b [22 #{44}]}

  ;; dissociating an element that is itself a nested collection; containers are dissociated
  (dissoc-in* [11 [22]] [1]) ;; => [11]
  (dissoc-in* {:a {:b {:c 99}}} [:a :b]) ;; => {:a {}}

  ;; dissociating a scalar element; containers are preserved
  (dissoc-in* [11 [22 [33]]] [1 1 0]) ;; => [11 [22 []]]
  (dissoc-in* [11 {:a 22}] [1 :a]) ;; => [11 {}]
  (dissoc-in* [11 22 #{33}] [2 33]) ;; => [1 2 #{}]
  ```

  See also:

  * [`clojure.core.incubator/dissoc-in`](https://github.com/clojure/core.incubator/blob/4f31a7e176fcf4cc2be65589be113fc082243f5b/src/main/clojure/clojure/core/incubator.clj#L63-L75)
  * [`taoensso.encore/dissoc-in`](https://taoensso.github.io/encore/taoensso.encore.html#var-dissoc-in)
  * [`clj-http.client/dissoc-in`](https://cljdoc.org/d/clj-http/clj-http/3.13.0/api/clj-http.client?q=dissoc#dissoc-in)
  * [`medley.core/dissoc-in`](https://weavejester.github.io/medley/medley.core.html#var-dissoc-in)
  * [`plumbing.core/dissoc-in`](http://plumatic.github.io/plumbing/plumbing.core.html#var-dissoc-in)"
  {:UUIDv4 #uuid "af3593d8-5470-4369-b0c4-9de941e26611"}
  [c [i & i_s]] (if i_s
                  (assoc* c i (dissoc-in* (get* c i) i_s))
                  (dissoc* c i)))