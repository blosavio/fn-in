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
  (print-form-then-eval "(dissoc-in* {:a [11 22 33 {:b 44 :c [55 66 77]}]} [:a 3 :c 0])")]]