[:section#introduction

 [:h2 "Introduction"]

 [:p "Most of the time, when someone hands us a Clojure collection, we know its
 type and how best to manipulate it. If we get handed a vector, we know that "
  [:code "get"]
  " is effective at retrieving an element. Let's grab that "
  [:code "2"]
  "."]

 [:pre (print-form-then-eval "(get [0 1 2 3] 2)")]

 [:p "But sometimes, we won't know ahead of time what kind of collection
 someone might give us. Perhaps we made a commitment that our utility would
 handle "
  [:em "all"]
  " Clojure collection types. Instead of a vector, we might be given a lazy
 sequence, such as a "
  [:code "range"]
  ". Perhaps we want to pull out the third element at index "
  [:code "2"]
  "."]

 [:pre (print-form-then-eval "(get (range 4) 2)")]

 [:p "That's… not what we wanted. While "
  [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get"}
   [:code "get"]]
  ", "
  [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/assoc"}
   [:code "assoc"]]
  ", "
  [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/update"}
   [:code "update"]]
  ", and "
  [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/dissoc"}
   [:code "dissoc"]]
  " and their "
  [:code "...-in"]
  " cousins are exceedingly handy, they do not seamlessly handle every collection
 type."]

 [:p "We could certainly build up an "
  [:em "ad hoc"]
  " type dispatch right on the spot inside our utility, but we'll probably
 forget to include at least one of the collection types, and we're likely to
 miss an edge case or two."]

 [:p "This library endeavors to patch those gaps in functionality while
 maintaining a consistent, familiar interface to inspect, change, and remove "
  [:a {:href "#element"} "elements"]
  " contained in vectors, hashmaps, sequences, lists, and sets, at any arbitrary
 level of nesting."]

 [:p "Leaning on those extended capabilities, we can now retrieve that third
 element located at index "
  [:code "2"]
  " with a straightforward, drop-in replacement."]

 [:pre
  (print-form-then-eval "(require '[fn-in.core :refer [get*]])")
  [:br]
  [:br]
  (print-form-then-eval "(get* (range 4) 2)")]

 [:p [:code "get*"]
  " behaves just like "
  [:code "clojure.core/get"]
  ", except that it succeeds in extracting the third element from our "
  [:code "range"]
  " argument."]]

