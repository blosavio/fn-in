(ns readmoi-generator
  "Script to load options and perform actions.

  CIDER eval buffer C-c C-k generates an html page and a markdown chunk."
  {:no-doc true}
  (:require
   [hiccup2.core :refer [raw]]
   [readmoi.core :refer [*project-group*
                         *project-name*
                         *project-version*
                         generate-all
                         prettyfy
                         print-form-then-eval]]
   [fn-in.core :refer :all]))


(def project-metadata (read-string (slurp "project.clj")))
(def readmoi-options (load-file "resources/readmoi_options.edn"))


(generate-all project-metadata readmoi-options)


(defn -main
  []
  {:UUIDv4 #uuid "bfc5ebcd-7540-487d-8c8b-82167dcec22a"}
  (println "generated fn-in ReadMe docs"))