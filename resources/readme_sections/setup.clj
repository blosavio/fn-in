[:section#setup
 [:h2 "Setup"]
 [:h3 "Leiningen/Boot"]
 [:pre [:code (str "[" *project-group* "/" *project-name* " \"" *project-version* "\"]")]]
 [:h3 "Clojure CLI/deps.edn"]
 [:pre [:code (str *project-group* "/" *project-name* " {:mvn/version \"" *project-version* "\"}")]]
 [:h3 "Require"]
 [:pre (print-form-then-eval "(require '[fn-in.core :refer [get-in* assoc-in* update-in* dissoc-in*]])")]]