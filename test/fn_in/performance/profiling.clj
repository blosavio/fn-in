(ns fn-in.performance.profiling
  "Performance profiling `fn-in` public-facing functions."
  (:require
   [clj-async-profiler.core :as prof]
   [clojure.main :refer [demunge]]
   [fn-in.core :refer [assoc-in*
                       assoc*
                       dissoc-in*
                       dissoc*
                       get-in*
                       get*
                       update-in*
                       update*]]
   [fn-in.performance.get-benchmarks :refer [test-get*-list
                                             test-get*-map
                                             test-get*-seq
                                             test-get*-vec]]
   [fn-in.performance.get-in-benchmarks :refer [test-get-in*-vec
                                                test-get-in*-vec-2
                                                test-get-in*-seq
                                                test-get-in*-list
                                                test-get-in*-map]]
   [fn-in.performance.assoc-benchmarks :refer [test-assoc*-list
                                               test-assoc*-map
                                               test-assoc*-seq
                                               test-assoc*-vec]]
   [fn-in.performance.assoc-in-benchmarks :refer [test-assoc-in*-vec
                                                  test-assoc-in*-vec-2
                                                  test-assoc-in*-seq
                                                  test-assoc-in*-list
                                                  test-assoc-in*-map]]
   [fn-in.performance.update-benchmarks :refer [test-update*-list
                                                test-update*-map
                                                test-update*-seq
                                                test-update*-vec]]
   [fn-in.performance.update-in-benchmarks :refer [test-update-in*-vec
                                                   test-update-in*-vec-2
                                                   test-update-in*-seq
                                                   test-update-in*-list
                                                   test-update-in*-map]]
   [fn-in.performance.dissoc-benchmarks :refer [test-dissoc*-list
                                                test-dissoc*-map
                                                test-dissoc*-seq
                                                test-dissoc*-vec]]
   [fn-in.performance.dissoc-in-benchmarks :refer [test-dissoc-in*-vec
                                                   test-dissoc-in*-vec-2
                                                   test-dissoc-in*-seq
                                                   test-dissoc-in*-list
                                                   test-dissoc-in*-map]]))


#_(prof/serve-ui 8080) ;; Serve on port 8080
#_(clj-async-profiler.ui/stop-server)


(defn profile-benchmark-fn
  "Given benchmark function `f` and argument `arg`, profile performance by
  evaluating `(* n 1E6)` times."
  {:UUIDv4 #uuid "c61b3642-a89a-487c-88d9-1e5753bf959d"
   :no-doc true}
  [f arg n]
  (prof/profile
   {:title (str (f :f) "\n" n "E6 samples")}
   (dotimes [_ (* n 1E6)]
     ((f :f) (int arg)))))


(defn one-quick-test-profile
  "Run a quick performance profile to test `profile-benchmark-fn`. Result does
  *not* contain enough samples to be meaningful."
  {:UUIDv4 #uuid "c27f53e3-d421-4788-978b-847f41948185"
   :no-doc true}
  []
  (profile-benchmark-fn test-get*-vec 1E6 0.01))


#_(one-quick-test-profile)


(defn profile-all
  "Run performance profiles for all fn-in public functions. Each will take
  about ten seconds."
  {:UUIDv4 #uuid "651801b7-99ee-4613-8714-9c4ed771e6ca"
   :no-doc true}
  []
  (do
    (println "get* profiling")
    (profile-benchmark-fn test-get*-vec 1E6 30)
    (profile-benchmark-fn test-get*-seq 1E6 30)
    (profile-benchmark-fn test-get*-map 1E6 30)
    (profile-benchmark-fn test-get*-list 1E3 2)

    (println "get-in* profiling")
    (profile-benchmark-fn test-get-in*-vec 6 5)
    (profile-benchmark-fn test-get-in*-vec-2 1E5 5)
    (profile-benchmark-fn test-get-in*-list 4 5)
    (profile-benchmark-fn test-get-in*-seq 6 5)
    (profile-benchmark-fn test-get-in*-map 6 5)

    (println "assoc* profiling")
    (profile-benchmark-fn test-assoc*-vec 1E6 20)
    (profile-benchmark-fn test-assoc*-seq 1E6 20)
    (profile-benchmark-fn test-assoc*-map 1E6 20)
    (profile-benchmark-fn test-assoc*-list 1E3 0.00003)

    (println "assoc-in* profiling")
    (profile-benchmark-fn test-assoc-in*-vec 6 2)
    (profile-benchmark-fn test-assoc-in*-vec-2 1E5 5)
    (profile-benchmark-fn test-assoc-in*-list 4 0.25)
    (profile-benchmark-fn test-assoc-in*-seq 6 0.2)
    (profile-benchmark-fn test-assoc-in*-map 6 2)

    (println "update* profiling")
    (profile-benchmark-fn test-update*-vec 1E2 10)
    (profile-benchmark-fn test-update*-seq 1E6 10)
    (profile-benchmark-fn test-update*-map 1E6 10)
    (profile-benchmark-fn test-update*-list 1E2 0.005)

    (println "update-in* profiling")
    (profile-benchmark-fn test-update-in*-vec 6 2)
    (profile-benchmark-fn test-update-in*-vec-2 1E5 2)
    (profile-benchmark-fn test-update-in*-list 4 0.2)
    (profile-benchmark-fn test-update-in*-seq 6 0.1)
    (profile-benchmark-fn test-update-in*-map 6 2)

    (println "dissoc* profiling")
    (profile-benchmark-fn test-dissoc*-vec 1E2 0.5)
    (profile-benchmark-fn test-dissoc*-seq 1E6 0.000025)
    (profile-benchmark-fn test-dissoc*-map 1E6 10)
    (profile-benchmark-fn test-dissoc*-list 1E2 0.003)

    (println "dissoc-in* profiling")
    (profile-benchmark-fn test-dissoc-in*-vec 6 2)
    (profile-benchmark-fn test-dissoc-in*-vec-2 1E5 0.0005)
    (profile-benchmark-fn test-dissoc-in*-list 4 0.5)
    (profile-benchmark-fn test-dissoc-in*-seq 6 0.1)
    (profile-benchmark-fn test-dissoc-in*-map 6 2)

    (println "profiling complete")))


#_(profile-all)

