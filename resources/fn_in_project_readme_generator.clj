(ns fn-in-project-readme-generator
  "CIDER eval buffer C-c C-k generates an html page and a markdown chunk."
  (:require
   [hiccup2.core :as h2]
   [hiccup.page :as page]
   [hiccup.element :as element]
   [hiccup.form :as form]
   [hiccup.util :as util]
   [fn-in-hiccup :refer :all]))


(def readme-UUID #uuid "59ecaabc-1b75-4616-9f03-2ccde4bb8729")


(def page-body

  [:article
   [:a {:href "https://clojars.org/com.sagevisuals/fn-in"}(element/image "https://img.shields.io/clojars/v/com.sagevisuals/fn-in.svg")]
   [:br]
   [:a {:href "https://github.io/blosavio/fn-in/index.html"} "API"]
   [:br]
   [:a {:href "#examples"} "Examples"]
   [:br]
   [:a {:href "#alternatives"} "Alternatives"]
   [:br]
   [:a {:href "#glossary"} "Glossary"]

   [:h1 [:code "fn-in"]]
   [:em "Inspect, update, exchange, and remove elements from heterogeneous, arbitrarily-nested Clojure data structures with a familiar interface."]
   [:br]

   [:section#setup
    [:h2 "Setup"]
    [:h3 "Leiningen/Boot"]
    [:pre [:code "[com.sagevisuals/fn-in \"0\"]"]]
    [:h3 "Clojure CLI/deps.edn"]
    [:pre [:code "com.sagevisuals/fn-in {:mvn/version \"0\"}"]]
    [:h3 "Require"]
    [:pre (print-form-then-eval "(require '[fn-in.core :refer [get-in* assoc-in* update-in* dissoc-in*]])")]]


   [:section#ideas
    [:h2 "Ideas"]
    [:p
     [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get-in"} [:code "get-in"]]
     ", "
     [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/assoc-in"} [:code "assoc-in"]]
     ", "
     [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/update-in"} [:code "update-in"]]
     ", and "
     [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/dissoc"} [:code "dissoc"]] " are among my favorite Clojure core functions. They just make sense to my brain. However, they have some limitations that block them from serving in all the scenarios I wanted."]

    [:pre (print-form-then-eval "(get-in {:a {:b {:c [40 41 42]}}} [:a :b :c 2])")]

    "…works as I expect, but…"

    [:pre (print-form-then-eval "(get-in {:a '(40 41 42)} [:a 2])")]

    [:p "…doesn't. I envisioned a set of functions that presented a consistent, familiar interface to inspect, change, and remove " [:a {:href "#element"} "elements"] " in vectors, maps, sequences, lists, and sets, at any arbitrary level of nesting."]

    [:p "This library provides " [:em "starred"] " versions: " [:code "get-in*"] ", " [:code "assoc-in*"] ", " [:code "update-in*"] ", " [:code "dissoc-in*"] ", which all operate similar to their " [:code "clojure.core"] " namesakes, but work on any " [:a {:href "#HANDS"} "heterogeneous, arbitrarily-nested data structure"] ". Their interface is based on the concept of a "  [:a {:href "#path"} "path"] ". A path unambiguously addresses an element of a heterogeneous, arbitrarily-nested data structure. Elements in vectors, lists, and other sequences are addressed by zero-indexed integers. Map elements are addressed by their keys, and set elements are addressed by the elements themselves. "]

    [:p "Here's how paths work. Vectors are addressed by zero-indexed integers."]

    [:pre
     [:code "           [100 101 102 103]"]
     [:br]
     [:code "indexes --> 0   1   2   3"]]

    [:p "Same for lists…"]
    [:pre
     [:code "          '(97 98 99 100)"]
     [:br]
     [:code "indexes --> 0  1  2  3"]]

    [:p "…and same for other sequences, like " [:code "range"] "."]
    [:pre
     (print-form-then-eval "(range 29 33)")
     [:br]
     [:code "indexes -----------> 0  1  2  3"]]

    [:p "Maps are addressed by their keys, which are often keywords, like this."]
    [:pre
     [:code "        {:a 1 :foo \"bar\" :hello 'world}"]
     [:br]
     [:code "keys --> :a   :foo       :hello"]]

    [:p "But maps may be keyed by " [:em "any"] " value, including integers…"]
    [:pre
     [:code "        {0 \"zero\" 1 \"one\" 99 \"ninety-nine\"}"]
     [:br]
     [:code "keys --> 0        1       99"]]

    [:p "…or some other scalars…"]
    [:pre
     [:code "        {\"a\" :value-at-str-key-a 'b :value-at-sym-key-b \\c :value-at-char-key-c}"]
     [:br]
     [:code "keys --> \"a\"                     'b                     \\c"]]

    [:p "…even composite values."]
    [:pre
     [:code "        {[0] :val-at-vec-0 [1 2 3] :val-at-vec-1-2-3 {} :val-at-empty-map}"]
     [:br]
     [:code "keys --> [0]               [1 2 3]                   {}"]]

    [:p "Set elements are addressed by their identities, so they are located at themselves."]
    [:pre
     [:code "             #{42 :foo true 22/7}"]
     [:br]
     [:code "identities --> 42 :foo true 22/7"]]
    [:p "A " [:em "path"] " is a sequence of indexes, keys, or identities that allow the starred functions to dive into a nested data structure, one path element per level of nesting."]

    [:p "Let's build a path to the third element of a vector " [:code "[11 22 33]"] ". Vector elements are addressed by zero-indexed integers, so the third element is located at integer " [:code "2"] ". We invoke " [:code "get-in*"] " just like " [:code "clojure.core/get-in"] ": the collection is the first arg. We stuff that " [:code "2"] " into the second arg, the path vector."]

    [:pre (print-form-then-eval "(get-in* [11 22 33] [2])")]

    [:p "And we receive the third element, integer " [:code "33"] ". Let's get a little more fancy: a vector nested within another vector " [:code "[11 22 [33 44 55]]"] ". The nested vector is located at the third spot, index " [:code "2"] ". If we call " [:code "get-in*"] " with that path…"]

    [:pre (print-form-then-eval "(get-in* [11 22 [33 44 55]] [2])")]

    [:p "…it dutifully tells us that there's a child vector nested at that spot. To access the third element of " [:em "that"] " vector, we must append an entry onto the path."]

    [:pre (print-form-then-eval "(get-in* [11 22 [33 44 55]] [2 2])")]

    [:p "Nothing terribly special that " [:code "clojure.core/get-in"] " cant' do. But, if for some reason, that nested thing is instead a list…"]

    [:pre (print-form-then-eval "(get-in [11 22 '(33 44 55)] [2 2])")]

    [:p "…it's not quite what we wanted. But if we call the starred version…"]

    [:pre (print-form-then-eval "(get-in* [11 22 '(33 44 55)] [2 2])")]

    [:p "…all fine and dandy."]

    [:p "Let's look at maps. Map elements are addressed by keys. Let's inspect the value at key " [:code ":z"] ". We insert a " [:code ":z"] " keyword into the path arg."]

    [:pre (print-form-then-eval "(get-in* {:y 22 :z 33 :x 11} [:z]))")]

    [:p "If there's another map nested at that key, and we wanted the value at keyword " [:code ":w"] " of that nested map, we would merely append that key to the previous path vector arg."]

    [:pre (print-form-then-eval "(get-in* {:y 22 :z {:q 44 :w 55} :x 11} [:z :w])")]

    [:p "Again, that's exactly how " [:code "clojure.core/get-in"] " works, but what if we had something else nested there, like a " [:code "clojure.lang.Range"] "?"]

    [:pre (print-form-then-eval "(get-in {:y 11 :z (range 30 33)} [:z 2])")]

    [:p "That may not be useful for what you need to do. But calling the starred version…"]

    [:pre (print-form-then-eval "(get-in* {:y 11 :z (range 30 33)} [:z 2])")]

    [:p "…we get what we want."]

    [:p "Beyond inspecting a value with " [:code "get-in*"] ", the starred functions can return a modified heterogeneous, arbitrarily-nested data structure. They all consume a path exactly the way " [:code "get-in*"] " does. First, we could swap out — " [:em "associating"] " — a nested value for one we supply."]

    [:pre (print-form-then-eval "(assoc-in* {:a (list 11 (take 3 (cycle ['foo 'bar 'baz])))} [:a 1 2] :Clojure!)")]

    [:p "We could also apply a function to — " [:em "updating"] " — a nested value."]

    [:pre (print-form-then-eval "(update-in* (take 3 (repeat [11 22 33])) [2 1] #(+ % 9977))")]

    [:p "Or, we can simply " [:em "dissociate"] " a nested value, removing it entirely."]

    [:pre (print-form-then-eval "(dissoc-in* {:a (list 22 (take 3 (iterate inc 33)))} [:a 1 1])")]

    [:p "Note how the starred functions are able to dive into any of the collection types to do their jobs. These capabilities allow you to straightforwardly manipulate any Clojure data you might encounter."]

    [:p "("
     [:code "clojure.core"]
     " does "
     [:a {:href "https://ask.clojure.org/index.php/730/missing-dissoc-in"}
      "not provide"]
     " an equivalent "
     [:code "dissoc-in"]
     " companion to "
     [:code "dissoc"]
     ".)"]

    [:p "The empty vector addresses the top-level root collection of any collection type."]
    [:pre
     (print-form-then-eval "(get-in* [1 2 3] [])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* '(:foo \"bar\" 42) [])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* {:a 1 :b 2} [])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* #{:foo 42 \\z} [])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* (range 0 4) [])")]

    ] ;; end of ideas section


   [:section#examples
    [:h2 "Examples"]
    [:h3 "Getting values"]
    [:pre
     (print-form-then-eval "(get-in* [11 22 [33 44 55 [66 [77 [88 99]]]]] [2 3 1 1 1])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* {:a {:b {:c {:d 99}}}} [:a :b :c :d])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* (list 11 22 33 (list 44 (list 55))) [3 1 0])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* #{11 #{22}} [#{22} 22])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* [11 22 {:a 33 :b [44 55 66 {:c [77 88 99]}]}] [2 :b 3 :c 2])")
     [:br]
     [:br]
     (print-form-then-eval "(get-in* {:a (list {} {:b [11 #{33}]})} [:a 1 :b 1 33])")]
    [:h3 "Associating values"]
    [:pre
     (print-form-then-eval "(assoc-in* [11 [22 [33 [44 55 66]]]] [1 1 1 2] :new-val)")
     [:br]
     [:br]
     (print-form-then-eval "(assoc-in* {:a {:b {:c 42}}} [:a :b :c] 99)")
     [:br]
     [:br]
     (print-form-then-eval "(assoc-in* {:a [11 22 33 [44 55 {:b [66 {:c {:d 77}}]}]]} [:a 3 2 :b 1 :c :d] \"foo\")")]
    [:h3 "Updating values"]
    [:pre
     (print-form-then-eval "(update-in* [11 22 33 [44 [55 66 [77 88 99]]]] [3 1 2 2] inc)")
     [:br]
     [:br]
     (print-form-then-eval "(update-in* {:a [11 22 {:b 33 :c [44 55 66 77]}]} [:a 2 :c 1] #(+ 5500 %))")]
    [:h3 "Dissociating values"]
    [:pre
     (print-form-then-eval "(dissoc-in* [11 22 [33 [44 55 66]]] [2 1 1])")
     [:br]
     [:br]
     (print-form-then-eval "(dissoc-in* {:a [11 22 33 {:b 44 :c [55 66 77]}]} [:a 3 :c 0])")]
    ] ;; end of recipes section


   [:section#alternatives
    [:h2 "Alternatives"]
    [:ul
     [:li [:code "clojure.core"]
      [:p "Unless you absolutely need " [:code "fn-in"] "'s capabilities, use the built-ins."]]

     [:li "Plumatic's " [:a {:href "https://github.com/plumatic/plumbing"} "Plumbing"]
      [:p "A simple and " [:em "declarative"] " way to specify a structured computation, which is easy to analyze, change, compose, and monitor."]]

     [:li "John Newman's " [:a {:href "https://github.com/johnmn3/injest"} "Injest"]
      [:p "Path thread macros for navigating into and transforming data."]]

     [:li "Nathan Marz' " [:a {:href "https://github.com/redplanetlabs/specter"} "Specter"]
      [:p "Querying and transforming nested and recursive data with a " [:em "navigator"] " abstraction."]]]


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
       [:p "Elements of vectors, lists, and other sequences are located by zero-indexed integers. Map values are addressed by their keys, which are often keywords, but can be any data type, including integers, or composite types. (You don't often need to key a map on a multi-element, nested structure, but when you need to, it's awesome.) Set members are addressed by their identities. Nested collections contained in a set can indeed be addressed: the path vector itself contains the collections. An empty vector " [:code "[]"] " addresses the outermost, containing collection."]]
      ]] ;; end of glossary section

    [:h2 "License"]
    [:p "This program and the accompanying materials are made available under the terms of the " [:a {:href "https://opensource.org/license/MIT"} "MIT License"] "."]]
   ])


(spit "doc/readme.html"
      (page-template
       "fn-in — >>>Insert Title<<<"
       readme-UUID
       (conj [:body] page-body)))


(spit "README.md"
      (-> page-body
          h2/html
          str
          (clojure.string/replace #"</?article>" "")))