(defproject resque-worker "1.0.0"
  :description "Simple resque worker with logging, statsd and graceful shutdown support."
  :url "https://github.com/dsabanin/resque-worker"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :author "Dima Sabanin <sdmitry@gmail.com>"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [dsabanin/resque-clojure "0.3.4"]
                 [wildbit/simplelog "1.0.13"]
                 [clj-statsd "0.3.2"]])
