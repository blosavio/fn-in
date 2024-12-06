(defproject com.sagevisuals/fn-in "4"
  :description "A Clojure library for manipulating heterogeneous, arbitrarily-nested data structures."
  :url "https://github.com/blosavio/fn-in"
  :license {:name "MIT License"
            :url "https://opensource.org/license/mit"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.12.0"]]
  :repl-options {:init-ns fn-in.core}
  :plugins []
  :profiles {:dev {:dependencies [[org.clojure/test.check "1.1.1"]
                                  [hiccup "2.0.0-RC3"]
                                  [zprint "1.2.9"]
                                  [com.sagevisuals/chlog "1-SNAPSHOT0"]
                                  [com.sagevisuals/readmoi "3-SNAPSHOT0"]]
                   :plugins [[dev.weavejester/lein-cljfmt "0.12.0"]
                             [lein-codox "0.10.8"]]}
             :repl {}}
  :codox {:metadata {:doc/format :markdown}
          :namespaces [#"^fn-in\.(?!scratch)"]
          :target-path "doc"
          :output-path "doc"
          :source-uri "https://github.com/blosavio/fn-in/blob/main/{filepath}#L{line}"
          :html {:transforms [[:div.sidebar.primary] [:append [:ul.index-link [:li.depth-1 [:a {:href "https://github.com/blosavio/fn-in"} "Project Home"]]]]]}
          :project {:name "fn-in" :version "version 4"}}
  :scm {:name "git" :url "https://github.com/blosavio/fn-in"})