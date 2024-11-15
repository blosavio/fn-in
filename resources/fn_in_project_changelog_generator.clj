(ns fn-in-project-changelog-generator
  "CIDER eval buffer C-c C-k generates a 'changelog.md' in the project's
  level directory.

  NOTE: changelog aggregation, specification, validation, and webpage generation
  ought to be split out into its own library."
  {:no-doc true}
  (:require
   [fn-in.core :refer [get-in*]]
   [hiccup2.core :as h2]
   [hiccup.page :as page]
   [hiccup.element :as element]
   [hiccup.form :as form]
   [hiccup.util :as util]
   [fn-in-hiccup :refer :all]))


(def changelog-data (load-file "changelog.edn"))

(def changelog-webpage-UUID #uuid "07671ac9-ef7b-463a-baa4-7e2adec55d1c")


(defn renamed-fns
  "Given a sequence `o2n` of 'old-fn-name'-to-'new-fn-name' maps, generate a
  hiccup/html unordered list of old to new."
  {:UUIDv4 #uuid "6f04837d-8314-4ef2-b729-14a1cb31b990"}
  [o2n]
  (let [sorted-oldnames (sort-by :old-function-name o2n)]
    (reduce #(conj %1 [:li [:code (name (:old-function-name %2))] " → " [:code (name (:new-function-name %2))]]) [:ul] sorted-oldnames)))


(defn something-ed-fns
  "Given a sequence `changes` of changelog change maps, aggregate functions that
  have `change-type`, one of
  * `:added-functions`,
  * `:renamed-functions`,
  * `:moved-functions`,
  * `:removed-functions`, or
  * `:function-arguments`."
  {:UUIDv4 #uuid "d9a2782a-c903-4338-93f2-78871d352cdd"}
  [changes change-type]
  (let [aggregation (reduce #(clojure.set/union %1 (set (change-type %2))) #{} changes)]
    (if (= change-type :renamed-functions)
      [(renamed-fns aggregation)]
      (->> aggregation
           vec
           sort
           (map #(vector :code (str %)))
           (interpose ", ")))))


(comment
  (def test-changes-1 (get-in changelog-data [2 :changes]))

  (get-in test-changes-1 [0])

  (something-ed-fns test-changes-1 :added-functions)
  (something-ed-fns test-changes-1 :renamed-functions)
  (something-ed-fns test-changes-1 :moved-functions)
  (something-ed-fns test-changes-1 :removed-functions)
  (something-ed-fns test-changes-1 :altered-functions)
)


(defn change-details
  "Given a sequence of `changes`, return a hiccup/html unordered list that lists
  the changes."
  {:UUIDv4 #uuid "5ed2bc16-9d57-4a88-acb4-ee5dae218110"}
  [changes]
  (let [grouped-changes (group-by #(:breaking? %) changes)
        breaking-changes (grouped-changes true)
        non-breaking-changes (concat (grouped-changes false)
                                     (grouped-changes nil))
        issue-reference #(if (:reference %) [:a {:href (:url (:reference %))} (:source (:reference %))] nil)
        issue-reference-seperator #(if (:reference %) ": " nil)]
    [:div
     [:h4 "Breaking changes"]
     (into [:ul] (map (fn [v] [:li [:div (issue-reference v) (issue-reference-seperator v) (str (:description v))]])) breaking-changes)
     [:h4 "Non-breaking changes"]
     (into [:ul] (map (fn [v] [:li [:div (issue-reference v) (issue-reference-seperator v) (str (:description v))]])) non-breaking-changes)]))


(comment
  (def test-changes-2 (get-in changelog-data [2 :changes]))

  (change-details test-changes-2)
  )


(defn generate-version-section
  "Given a map `m` that contains data on a single changelog version, generate
  hiccup/html for a section that displays that info."
  {:UUIDv4 #uuid "6d232a01-4cc1-4b91-8b63-7a5da9a96cb3"}
  [m]
  (let [changed-function-div (fn [label change-type] (let [something-ized-fn (something-ed-fns (m :changes) change-type)]
                                                       (if (empty? something-ized-fn)
                                                         nil
                                                         (into [:div [:em (str label " functions: ")]] something-ized-fn))))]
    [:section
     [:h3 (str "version " (:version m))]
     [:p
      (str (:year (:date m)) " "
           (:month (:date m)) " "
           (:day (:date m))) [:br]
      (str (:name (:responsible m)) " (" (:email (:responsible m)) ")") [:br]
      [:em "Description: "] (str (:comment m)) [:br]
      [:em "Project status: "] [:a {:href "https://github.com/metosin/open-source/blob/main/project-status.md"} (name (:project-status m))] [:br]
      [:em "Urgency: "] (name (:urgency m)) [:br]
      [:em "Breaking: "] (if (:breaking? m) "yes" "no")]
     [:p
      (changed-function-div "added" :added-functions)
      (let [possible-renames (something-ed-fns (m :changes) :renamed-functions)]
        (if (= [[:ul]] possible-renames)
          nil
          (into [:div [:em "renamed functions: "]] possible-renames)))
      (changed-function-div "moved" :moved-functions)
      (changed-function-div "removed" :removed-functions)
      (changed-function-div "altered" :altered-functions)]
     (change-details (m :changes))
     [:hr]]))


(comment
  (get-in* changelog-data [1])
  (get-in* changelog-data [1 :date])
  (get-in* changelog-data [1 :date :day])


  (generate-version-section (get changelog-data 0))
  ;; 
  )


(def changelog-info
  [:section
   [:h4 "Changelog info"]
   [:p#info "A human- and machine-readable " [:code "changelog.edn"] " will accompany each version at the project's root directory. " [:code "changelog.edn"] " is tail-appended file constructed from all previous releases, possibly automatically-composed of per-version " [:code "changelog-v" [:em "N"] ".edn"] " files in a sub-directory."]
   [:p "A " [:code "changelog.md"] " file, intended for display on the web, is generated by a script. This script also contains specifications describing the changelog data."]
   [:p "Tentative policy: Bug fixes are non-breaking changes."]])


(def changelog-md-footer [:p#page-footer
                          (copyright)
                          [:br]
                          (str "Compiled " (short-date) ".")
                          [:span#uuid [:br] changelog-webpage-UUID]])


(spit "changelog.md"
      (h2/html
       (vec (-> [:body
                 [:h1 "fn-in library changelog"]
                 [:a {:href "#info"} "changelog meta"]]
                #_(map #(vector :section [:h3 (str "Version " %)]) [0 1 2])
                #_(generate-version-section (get changelog-data 1))
                (into (map #(generate-version-section %) (reverse changelog-data)))
                (conj changelog-info)
                (conj changelog-md-footer)))))