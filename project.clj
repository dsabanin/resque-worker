(defproject resque-worker "0.1.0-SNAPSHOT"
  :description "Simple resque worker with logging, statsd and graceful shutdown support."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [dsabanin/resque-clojure "0.3.4"]
                 [wildbit/simplelog "1.0.13"]
                 [clj-statsd "0.3.2"]])
