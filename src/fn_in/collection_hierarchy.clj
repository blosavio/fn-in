(comment
  "Referred by fn-in.core, and possibly other external libs. Used for
  multimethods, which should probably be converted to protocols. (But I think
  multimethods are prettier.)")


(derive clojure.lang.PersistentList ::list)
(derive clojure.lang.PersistentList$EmptyList ::list)

(derive clojure.lang.Cons ::list)

(derive clojure.lang.PersistentVector ::vector)
(derive clojure.lang.PersistentVector$ChunkedSeq ::vector)
(derive clojure.lang.APersistentVector$SubVector ::vector)

(derive clojure.lang.PersistentArrayMap ::map)
(derive clojure.lang.PersistentHashMap ::map)
(derive clojure.lang.PersistentTreeMap ::map)

(derive clojure.lang.MapEntry ::map-entry)

(derive clojure.lang.PersistentHashSet ::set)
(derive clojure.lang.PersistentTreeSet ::set)

(derive ::list ::non-map-entry-collection)
(derive ::vector ::non-map-entry-collection)
(derive ::map ::non-map-entry-collection)
(derive ::set ::non-map-entry-collection)

(derive ::list ::non-map)
(derive ::vector ::non-map)
(derive ::set ::non-map)

(derive clojure.lang.Cycle ::non-terminating)
(derive clojure.lang.Iterate ::non-terminating)
(derive clojure.lang.LazySeq ::non-terminating)
(derive clojure.lang.LongRange ::non-terminating)
(derive clojure.lang.Range ::non-terminating)
(derive clojure.lang.Repeat ::non-terminating)