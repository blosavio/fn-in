(defproject com.sagevisuals/fn-in "7-SNAPSHOT1"
  :description "A Clojure library for manipulating heterogeneous, arbitrarily-nested data structures."
  :url "https://github.com/blosavio/fn-in"
  :license {:name "MIT License"
            :url "https://opensource.org/license/mit"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.12.3"]]
  :repl-options {:init-ns fn-in.core}
  :plugins []
  :profiles {:dev {:dependencies [[com.clojure-goes-fast/clj-async-profiler "1.6.2"]
                                  [com.sagevisuals/chlog "5"]
                                  [com.sagevisuals/extended-extend-protocol "0-SNAPSHOT1"]
                                  [com.sagevisuals/fastester "0"]
                                  [com.sagevisuals/readmoi "6"]
                                  [hiccup "2.0.0-RC3"]
                                  [org.clojure/test.check "1.1.1"]]
                   :plugins [[dev.weavejester/lein-cljfmt "0.12.0"]
                             [lein-codox "0.10.8"]]
                   :jvm-opts ["-Djdk.attach.allowAttachSelf"
                              "-XX:+UnlockDiagnosticVMOptions"
                              "-XX:+DebugNonSafepoints"
                              "-Dclj-async-profiler.output-dir=./resources/profiler_data/"]}
             :benchmark {:jvm-opts ["-XX:+TieredCompilation"
                                    "-XX:TieredStopAtLevel=4"]}
             :repl {}}
  :aliases {"readmoi" ["run" "-m" "readmoi-generator"]
            "chlog" ["run" "-m" "chlog-generator"]}
  :codox {:metadata {:doc/format :markdown}
          :namespaces [#"^fn-in\.(?!scratch)"]
          :target-path "doc"
          :output-path "doc"
          :doc-files []
          :source-uri "https://github.com/blosavio/fn-in/blob/master/{filepath}#L{line}"
          :html {:transforms [[:div.sidebar.primary] [:append [:ul.index-link [:li.depth-1 [:a {:href "https://github.com/blosavio/fn-in"} "Project Home"]]]]]}
          :project {:name "fn-in" :version "version 7"}}
  :scm {:name "git" :url "https://github.com/blosavio/fn-in"})

