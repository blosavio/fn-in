(ns fn-in-hiccup
  "Convenience functions for generating project webpage with hiccup"
  {:no-doc true}
  (:require
   [clojure.pprint :as pp]
   [clojure.string :as str]
   [clojure.test.check.generators :as gen]
   [zprint.core :as zp]
   [hiccup2.core :as h2]
   [hiccup.page :as page]
   [hiccup.element :as element]
   [hiccup.form :as form]
   [hiccup.util :as util])
  (:import java.util.Date))


;; FireFox apparently won't follow symlinks to css or font files


(def ^:dynamic *wrap-at* 80)


(defn comment-newlines
  "Given string s, arrow a, and comment symbol c, linebreak and indent the text.
   Arrow is applied at the head and any trailing newlines are indented to
   maintain formatting."
  {:UUIDv4 #uuid "3ea3a186-6870-4b3f-b569-d0d7ac90f975"}
  [s a c]
  (let [commented-arrow (str c a)
        arrow-prefixed-str (str commented-arrow s)
        equivalent-blanks (clojure.string/join "" (repeat (count a) " "))
        indent (str "\n" c equivalent-blanks)]
    (clojure.string/replace arrow-prefixed-str "\n" indent)))


(defn prettyfy
  "Apply zprint formatting to string s."
  {:UUIDv4 #uuid "a419ba9f-3aaa-4be2-837f-9cc75c51dbe9"}
  [s]
  (zp/zprint-str s {:width *wrap-at*
                    :vector {:wrap-coll? true}
                    :parse-string? true}))


(defn print-form-then-eval
  "Returns a hiccup [:code] block wrapping a Clojure stringified form str-form,
  separator sep (default ' => '), and evaluated value. `def`, `defn`, `s/def/`,
  `defmacro`, `defpred`, and `require` expressions are only evaled; their output
  is not captured.

  Note: Evaluated output can not contain an anonymous function of either
  (fn [x] ...) nor #(...) because zprint requires an internal reference
  to attempt a backtrack. Since the rendering of an anonymous function
  changes from one invocation to the next, there is no stable reference."
  {:UUIDv4 #uuid "39dcd66b-f919-41a2-8376-4c2364bf3c59"}
  ([str-form] (print-form-then-eval str-form " => "))
  ([str-form separator]
   (let [def? (re-find #"^\((s\/)?defn?(macro)?(pred)? " str-form)
         require? (re-find #"^\(require " str-form)
         form (read-string str-form)
         evaled-form (eval form)
         evaled-str (pr-str evaled-form)]
     (if (or def? require?)
       [:code (prettyfy str-form)]
       (let [combo-str (str (prettyfy str-form) " ;;" separator (prettyfy evaled-str))]
         (if (<= (count combo-str) *wrap-at*)
           [:code combo-str]
           [:code (str (prettyfy str-form)
                       "\n"
                       (comment-newlines (prettyfy evaled-str)
                                         separator
                                         ";;"))]))))))


(defn long-date
  "Long-form date+time, with time zone removed."
  {:UUIDv4 #uuid "392e226b-17ed-474e-a44d-a9efcf4b86f4"}
  []
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss") (java.util.Date.)))


(defn short-date
  "Short-form date, named month."
  {:UUIDv4 #uuid "c3c185c1-220a-4a33-838e-91784ab7380e"}
  []
  (.format (java.text.SimpleDateFormat. "yyyy LLLL dd") (java.util.Date.)))


(defn copyright
  "Formated copyright with updated year."
  []
  (let [year (.format (java.text.SimpleDateFormat. "yyyy") (java.util.Date.))]
    (str "Copyright © " (if (= "2024" year) year (str "2024–" year)) " Brad Losavio.")))


(defn page-template
  "Generate a webpage with TufteCSS, compatible nav-bar, header title t,
   hiccup/html dialect body b, and UUIDv4 uuid."
  {:UUIDv4 #uuid "80dd93eb-0c26-41a0-9e6c-2d88352ea4e5"}
  [title uuid body]
  (page/html5
   {:lang "en"}
   [:head
    (page/include-css "project.css")
    [:title title]
    [:meta {"charset"  "utf-8"
            "name" "viewport"
            "content" "width=device-width, initial-scale=1"
            "compile-date" (long-date)}]
    (conj body [:p#page-footer
                (copyright)
                [:br]
                (str "Compiled " (short-date) ".")
                [:span#uuid [:br] uuid]])]))


(defn section
  "Generate a hiccup [:section] with a supplied section-name, h# header level,
   and contents."
  {:UUIDv4 #uuid "a45cd2ee-21d6-4401-bcf2-171c03addc93"}
  [h# section-name & contents]
  (into [(keyword (str "section#" section-name)) [h# section-name]] contents))


(defn random-sentence
  "Generates a random alpha-numeric sentence."
  {:UUIDv4 #uuid "369a6a02-3f26-4ec2-b533-81594d8edcba"}
  []
  (str (->> (gen/sample gen/string-alphanumeric (+ 5 (rand-int 15)))
            (clojure.string/join " " )
            (clojure.string/trim)
            (clojure.string/capitalize)
            )
       "."))


(defn random-paragraph
  "Generate a random alpha-numberic paragraph."
  {:UUIDv4 #uuid "06d489f4-7e29-4de5-bb33-1cb8d0e72088"}
  []
  (loop [num (+ 2 (rand-int 3))
         p ""]
    (if (zero? num)
      (clojure.string/trim p)
      (recur (dec num) (str p " " (random-sentence))))))
