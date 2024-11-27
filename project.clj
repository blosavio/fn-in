(defproject com.sagevisuals/fn-in "4-SNAPSHOT0"
  :description "A Clojure library for manipulating heterogeneous, arbitrarily-nested data structures."
  :url "https://blosavio.github.io/fn-in"
  :license {:name "MIT License"
            :url "https://opensource.org/license/mit"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/test.check "1.1.1"]]
  :repl-options {:init-ns fn-in.core}
  :plugins []
  :profiles {:dev {:dependencies [[hiccup "2.0.0-RC3"]
                                  [zprint "1.2.9"]
                                  [com.sagevisuals/chlog "0-SNAPSHOT2"]
                                  [com.sagevisuals/readmoi "2-SNAPSHOT1"]]
                   :plugins [[dev.weavejester/lein-cljfmt "0.12.0"]
                             [lein-codox "0.10.8"]]}
             :repl {}}
  :codox {:metadata {:doc/format :markdown}
          :namespaces [#"^fn-in\.(?!scratch)"]
          :target-path "doc"
          :output-path "doc"
          :source-uri "https://github.com/blosavio/fn-in/blob/main/{filepath}#L{line}"
          :html {:namespace-list :flat
                 :transforms [[:div.sidebar.primary] [:append [:ul.index-link [:li.depth-1 [:a {:href "https://github.com/blosavio/fn-in"} "Project Home"]]]]]}
          :project {:name "fn-in" :version "version 4-SNAPSHOT0"}}
  :scm {:name "git" :url "https://github.com/blosavio/fn-in"})