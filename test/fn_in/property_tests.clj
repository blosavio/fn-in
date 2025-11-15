(ns fn-in.property-tests
  "Property tests for the `fn-in` library."
  (:require
   [clojure.test.check :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [fn-in.core :refer :all]))


(def num-checks 200)


(def get*-returns-get
  (prop/for-all
   [[v idx] (gen/let [x (gen/not-empty (gen/vector gen/any-printable))
                      i (gen/choose 0 (dec (count x)))]
              [x i])]
   (= (get v idx)
      (get* v idx))))


(tc/quick-check num-checks get*-returns-get)


(defn in?
  "Returns `true` if `item` is an element of `coll`"
  {:UUIDv4 #uuid "4968e041-b14b-442c-8277-d5830ae5a7fd"
   :no-doc true}
  [coll item]
  (boolean (some #{item} coll)))


(def getted-val-in-original-vector
  (prop/for-all
   [[v idx] (gen/let [x (gen/not-empty (gen/vector gen/small-integer))
                      i (gen/choose 0 (dec (count x)))]
              [x i])]
   (in? v (get* v idx))))


(tc/quick-check num-checks getted-val-in-original-vector)


(def getted-val-in-original-hashmap
  (prop/for-all
   [[m k] (gen/let [x (gen/map gen/keyword-ns gen/large-integer {:min-elements 1})
                    i (gen/elements (keys x))]
            [x i])]
   (in? (vals m) (get* m k))))


(tc/quick-check num-checks getted-val-in-original-hashmap)


(def assoc*-returns-assoc
  (prop/for-all
   [[m k] (gen/let [x (gen/map gen/keyword-ns gen/large-integer {:min-elements 1})
                    i (gen/elements (keys x))]
            [x i])
    v gen/large-integer]
   (= (assoc m k v)
      (assoc* m k v))))


(tc/quick-check num-checks assoc*-returns-assoc)


(def only-one-assoc*-ed
  (prop/for-all
   [[v idx] (gen/let [x (gen/not-empty (gen/vector gen/any-printable))
                      i (gen/choose 0 (dec (count x)))]
              [x i])
    z gen/large-integer]
   (= (dec (count v))
      (count (filter true? (map = v (assoc* v idx z)))))))


(tc/quick-check num-checks only-one-assoc*-ed)


(def update*-returns-update
  (prop/for-all
   [[m k] (gen/let [x (gen/map gen/keyword-ns gen/large-integer {:min-elements 1})
                    i (gen/elements (keys x))]
            [x i])]
   (= (update m k inc)
      (update* m k inc))))


(tc/quick-check num-checks update*-returns-update)


(def only-one-update*-ed
  (prop/for-all
   [[v idx] (gen/let [x (gen/not-empty (gen/vector gen/large-integer))
                      i (gen/choose 0 (dec (count x)))]
              [x i])]
   (= (dec (count v))
      (count (filter true? (map = v (update* v idx inc)))))))


(tc/quick-check num-checks only-one-update*-ed)


(def dissoc*-returns-dissoc
  (prop/for-all
   [[m k] (gen/let [x (gen/map gen/keyword-ns gen/large-integer {:min-elements 1})
                    i (gen/elements (keys x))]
            [x i])]
   (= (dissoc m k)
      (dissoc* m k))))


(tc/quick-check num-checks dissoc*-returns-dissoc)


(def return-coll-is-smaller
  (prop/for-all
   [[v idx] (gen/let [x (gen/not-empty (gen/vector gen/any-printable))
                      i (gen/choose 0 (dec (count x)))]
              [x i])]
   (= (dec (count v))
      (count (dissoc* v idx)))))


(tc/quick-check num-checks return-coll-is-smaller)

