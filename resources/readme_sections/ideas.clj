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

 [:p "Let's build a path to the third element of a vector " [:code "[11 22 33]"] ". Vector elements are addressed by zero-indexed integers, so the third element is located at integer " [:code "2"] ". We invoke " [:code "get-in*"] " just like " [:code "clojure.core/get-in"] ": the collection is the first arg. We stuff that " [:code "2"] " into the second arg, the path vector."]

 [:pre (print-form-then-eval "(get-in* [11 22 33] [2])")]

 [:p "And we receive the third element, integer " [:code "33"] ". Let's get a little more fancy: a vector nested within another vector " [:code "[11 22 [33 44 55]]"] ". The nested vector is located at the third spot, index " [:code "2"] ". If we call " [:code "get-in*"] " with that path…"]

 [:pre (print-form-then-eval "(get-in* [11 22 [33 44 55]] [2])")]

 [:p "…it dutifully tells us that there's a child vector nested at that spot. To access the third element of " [:em "that"] " vector, we must append an entry onto the path."]

 [:pre (print-form-then-eval "(get-in* [11 22 [33 44 55]] [2 2])")]

 [:p "Nothing terribly special that " [:code "clojure.core/get-in"] " can't do. But, if for some reason, that nested thing is instead a list…"]

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
  (print-form-then-eval "(get-in* (range 0 4) [])")]]