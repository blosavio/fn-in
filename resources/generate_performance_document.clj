(ns fn-in.generate-performance-document
  (:require
   [fn-in.core :refer [assoc*
                       update-in*]]
   [fn-in.performance.benchmark-structures :refer [vec-of-n-rand-ints
                                                   nested-vec]]
   [hiccup2.core :as h2]
   [hiccup.page :as page]
   [readmoi.core :refer [page-template
                         print-form-then-eval
                         tidy-html-document]]))


(def performance-summary-uuid #uuid "dd080235-f818-4d4c-b95c-8cfc549ef23a")
(def copyright-holder "Brad Losavio")
(def output-dir "doc/")
(def output-filename "performance_summary.html")
(def title "fn-in library performance summary")


(defn generate-html
  "Given hiccup `page-body`, and optional default, generates an html file
  summarizing benchmarking results."
  {:UUIDv4 #uuid "31f13516-89ee-4cd6-a99a-27b5b9bb01e1"
   :no-doc true}
  [page-body]
  (spit (str output-dir output-filename)
        (page-template
         title
         performance-summary-uuid
         (conj [:body] page-body)
         copyright-holder)))


(def body
  [:div
   [:h1 [:code "fn-in"] " library performance summary"]

   [:a {:href "#v4->v5"} "Discussion"] [:br]
   [:a {:href "#details"} "Details"] [:br]
   [:a {:href "#results"} "Results"] [:br]

   [:h3#v4->v5 "Version 4 to version 5"]

   [:p "Version 5 introduces type dispatch with Clojure protocols, bringing
 improved performance compared to version 4's multimethod type dispatch. Across
 a wide span of collection types and collection sizes, all eight of the public
 functions are significantly faster in at least some cases, while none regress.
 Upgrading to version 5 therefore provides improved performance for general
 uses."]

   [:h3#details "Details"]

   [:p "To ensure that any implementation change gives general performance
 improvement and not merely for one special case, we use "
    [:a {:href "https://github.com/blosavio/fastester"} "Fastester"]
    " to test a broad span of arguments. That involves running "
    [:a {:href "https://github.com/hugoduncan/criterium/"} "Criterium"]
    " benchmarks for various sizes of vectors, maps, sequences, and lists for each of "
    [:code "fn-in"]
    "'s eight functions."]

   [:p "For example, to test the performance of associating a new value into
 a flat (i.e., un-nested) vector, we do a lookup in "
    [:code "vec-of-n-rand-ints"]
    ", whose ten-element entry looks like this."]

   [:pre (print-form-then-eval "(vec-of-n-rand-ints 10)")]

   [:p "This provides the benchmark with a pre-made, one-dimensional series of
 random integers. We pre-compute the collections so that the benchmark does not
 measure the time it takes to construct the argument collection."]

   [:p "In our example, a single benchmark run takes multiple measurements of
 the time to associate a new value to the final element. Like this."]

   [:pre (print-form-then-eval "(assoc* (vec-of-n-rand-ints 10) (dec 10) :benchmark-sentinel)" 75 75)]

   [:p [:code "assoc*"]
    " replaces the "
    [:code "37"]
    " at the end of the test vector with "
    [:code ":benchmark-sentinal"]
    " in about 151 nanoseconds."]

   [:p "The benchmarks for the 'plain' functions (i.e., "
    [:code "get*"]
    ", "
    [:code "assoc*"]
    ", "
    [:code "update*"]
    ", and "
    [:code "dissoc*"]
    ") cover hashmaps, lists, sequences, and vectors, each containing up to one
 million elements. In total, over fifty benchmarks per function."]

   [:p "The "
    [:code "-in"]
    " function variants are benchmarked against nested versions of four
 collection types. For example, to test updating a value buried in a nested
 vector, we do a lookup in "
    [:code "nested-vec"]
    ", whose third entry looks like this."]

   [:pre (print-form-then-eval "(nested-vec 3)" 15 15)]

   [:p "This supplies the benchmark with a pre-made, triply-nested series of
 vectors containing a range of integers."]

   [:p "A single benchmark run takes multiple measurements of the time to
 increment the last element of the last nested vector. Like this."]

   [:pre (print-form-then-eval "(update-in* (nested-vec 3) [2 2 2] inc)" 75 15)]

   [:p [:code "update-in*"]
    " dives into the nested collection and increments the final "
    [:code "26"]
    " to a "
    [:code "27"]
    " in about 576 nanoseconds."]

   [:p "For each of the "
    [:code "-in"]
    " function variants (i.e., "
    [:code "get-in*"]
    ", "
    [:code "assoc-in*"]
    ", "
    [:code "update-in*"]
    ", and "
    [:code "dissoc-in*"]
    "), we run over fifty benchmarks, involving deeply-nested (up to six levels)
 hashmaps, lists, sequences, and vectors, whose sizes span up to one-hundred
 thousand elements."]

   [:h3#results "Benchmark results"]

   [:a {:href "get_performance.html"} [:code "get*"]] [:br]
   [:a {:href "get_in_performance.html"} [:code "get-in*"]] [:br]
   [:a {:href "assoc_performance.html"} [:code "assoc*"]] [:br]
   [:a {:href "assoc_in_performance.html"} [:code "assoc-in*"]] [:br]
   [:a {:href "update_performance.html"} [:code "update*"]] [:br]
   [:a {:href "update_in_performance.html"} [:code "update-in*"]] [:br]
   [:a {:href "dissoc_performance.html"} [:code "dissoc*"]] [:br]
   [:a {:href "dissoc_in_performance.html"} [:code "dissoc-in*"]]])


(do
  (generate-html body)
  (tidy-html-document (str output-dir output-filename)))

