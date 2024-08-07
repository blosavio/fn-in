(ns fn-in-hiccup-test
  (:require
   [clojure.test :refer [deftest is are testing run-tests]]
   [fn-in-hiccup :refer :all]))


(deftest comment-newlines-tests
  (are [x y] (= x y)
    (comment-newlines "" "-->" ";;")
    ";;-->"

    (comment-newlines "abcde" " => " ";;")
    ";; => abcde"

    (comment-newlines "abcde\nfghij\nklmno" " --> " ";;")
    ";; --> abcde\n;;     fghij\n;;     klmno"))


(deftest prettyfy-tests
  (are [x y] (= x y)
    (prettyfy (str (eval (read-string "[11 22 33]"))))
    "[11 22 33]"

    (prettyfy (str (eval (read-string "(repeat 3 (repeat 3 {:a 11 :b 22 :c 33}))"))))
    "(({:a 11, :b 22, :c 33} {:a 11, :b 22, :c 33} {:a 11, :b 22, :c 33})\n  ({:a 11, :b 22, :c 33} {:a 11, :b 22, :c 33} {:a 11, :b 22, :c 33})\n  ({:a 11, :b 22, :c 33} {:a 11, :b 22, :c 33} {:a 11, :b 22, :c 33}))"

    (prettyfy (str (eval (read-string "(repeat 2 (repeat 2 {:a 11 :b 22}))"))))
    "(({:a 11, :b 22} {:a 11, :b 22}) ({:a 11, :b 22} {:a 11, :b 22}))"))


(deftest print-form-then-eval-tests
  (are [x y] (=  x y)
    (print-form-then-eval "()")
    [:code "() ;; => ()"]

    (print-form-then-eval "(+)")
    [:code "(+) ;; => 0"]

    (print-form-then-eval "(+ 1 2 3)")
    [:code "(+ 1 2 3) ;; => 6"]

    (print-form-then-eval "(map inc [11 22 33])")
    [:code "(map inc [11 22 33]) ;; => (12 23 34)"]

    (print-form-then-eval "[11 22 33]")
    [:code "[11 22 33] ;; => [11 22 33]"]

    (print-form-then-eval "(def test-def 99)")
    [:code "(def test-def 99)"]

    (print-form-then-eval "(defn test-defn [x] (* 3 x))")
    [:code "(defn test-defn [x] (* 3 x))"]

    (print-form-then-eval "(defmacro Violets-awesome-macro [x] `(+ ~x))")
    [:code "(defmacro Violets-awesome-macro [x] `(+ ~x))"]

    (print-form-then-eval "(require '[fn-in.core :as fn-in])")
    [:code "(require '[fn-in.core :as fn-in])"]

    (print-form-then-eval "(#(< % 5) 4)")
    [:code "(#(< % 5) 4) ;; => true"]

    (print-form-then-eval "(* 1 2 3)" " --->>> ")
    [:code "(* 1 2 3) ;; --->>> 6"]

    (print-form-then-eval "(map inc (range 0 30))")
    [:code "(map inc (range 0 30))\n;; => (1\n;;     2\n;;     3\n;;     4\n;;     5\n;;     6\n;;     7\n;;     8\n;;     9\n;;     10\n;;     11\n;;     12\n;;     13\n;;     14\n;;     15\n;;     16\n;;     17\n;;     18\n;;     19\n;;     20\n;;     21\n;;     22\n;;     23\n;;     24\n;;     25\n;;     26\n;;     27\n;;     28\n;;     29\n;;     30)"]

    (print-form-then-eval "(filter #(odd? %) (range 42))")
    [:code "(filter #(odd? %) (range 42))\n;; => (1 3 5 7 9 11 13 15 17 19 21 23 25 27 29 31 33 35 37 39 41)"]

    (print-form-then-eval "(get-in {:a {:x 11 :y 22 :z 33} :b {:x 11 :y 22 :z 33} :c {:x 11 :y 22 :z 33}} [:b :z])")
    [:code "(get-in\n  {:a {:x 11, :y 22, :z 33}, :b {:x 11, :y 22, :z 33}, :c {:x 11, :y 22, :z 33}}\n  [:b :z])\n;; => 33"]))


(deftest long-date-tests
  (are [x] x
    (string? (long-date))
    (some? (re-find #"^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$" (long-date)))))


(deftest short-date-tests
  (are [x] x
    (string? (short-date))
    (some? (re-find #"^\d{4} \S+ \d{2}$" (short-date)))))


(deftest copyright-tests
  (clojure.string/starts-with? (copyright) "Copyright © ")
  (clojure.string/ends-with? (copyright) " Brad Losavio.")
  (some? (re-find #"^Copyright © 20\d{2} Brad Losavio.$" (copyright))))


(run-tests)
