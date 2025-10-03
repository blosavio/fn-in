[:section#performance
 [:h2 "Performance"]

 [:p "The "
  [:code "fn-in"]
  " library endeavors to be a drop-in replacement for "
  [:code "get-in"]
  ", "
  [:code "assoc-in"]
  ", and friends, mimicking their signatures, semantics, etc. Where possible, "
  [:code "fn-in"]
  " delegates to those core functions, thus closely matching the "
  [:a {:href "https://blosavio.github.io/fn-in/performance_summary.html"}
   "performance"]
  " expectations of the core functions, plus a smidgen of overhead for type
 dispatch. "]

 [:p "Some operations, such as manipulating the end of a list, will inevitably
 be slow. But even if not performant, at least the operation is "
  [:em "possible"]
  "."]]

