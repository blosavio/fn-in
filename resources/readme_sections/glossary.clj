[:section#glossary
 [:h2 "Glossary"]
 [:dl
  [:dt#element "element"]
  [:dd "A thing contained within a collection, either a scalar value or another nested collection."]

  [:dt#HANDS "heterogeneous, arbitrarily-nested data structure"]
  [:dd "Exactly one Clojure collection (vector, map, list, sequence, or set) with zero or more " [:a {:href "#element"} "elements"] ", nested to any depth."]

  [:dt#non-term-seq "non-terminating sequence"]
  [:dd "One of " [:code "clojure.lang.{Cycle,Iterate,LazySeq,LongRange,Range,Repeat}"] " that may or may not be realized, and possibly infinite. (I am not aware of any way to determine if such a sequence is infinite, so I treat them as if they are.)"]

  [:dt#path "path"]
  [:dd "A series of values that unambiguously navigates to a single " [:a {:href "#element"} "element"] " (scalar or sub-collection) in a " [:a {:href "#HANDS"} "heterogeneous, arbitrarily-nested data structure"] ". In the context of the " [:code "fn-in"] " library, the series of values comprising a path is contained in a vector passed as the second argument to the namespace's " [:code "…-in"] " functions. Almost identical to the second argument of "
   [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get-in"} [:code "clojure.core/get-in"]]
   ", but with more generality."
   [:p "Elements of vectors, lists, and other sequences are located by zero-indexed integers. Map values are addressed by their keys, which are often keywords, but can be any data type, including integers, or composite types. (You don't often need to key a map on a multi-element, nested structure, but when you need to, it's awesome.) Set members are addressed by their identities. Nested collections contained in a set can indeed be addressed: the path vector itself contains the collections. An empty vector " [:code "[]"] " addresses the outermost, containing collection."]]]]