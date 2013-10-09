(ns resque-worker.core
  (:require [resque-clojure.core :as resque]
            [resque-clojure.supervisor :as resque-super]
            [resque-clojure.resque :as resque-internal]
            [clj-statsd :as stats]
            [simplelog.use :refer :all]))

(defn configure
  [& {:keys [host port timeout password namespace max-workers poll-interval max-shutdown-wait]
      :or {host "127.0.0.1"
           port 6379
           timeout 300
           max-workers (.. (Runtime/getRuntime) availableProcessors)
           poll-interval 1000
           max-shutdown-wait 300}}]
  (resque/configure {:host host
                     :port port
                     :timeout timeout
                     :password password
                     :namespace namespace
                     :max-workers max-workers
                     :poll-interval poll-interval
                     :max-shutdown-wait max-shutdown-wait}))


(defn stop-workers
  [queues]
  (info "Signal received. Stopping workers for" (clojure.string/join ", " queues) "and exiting.")
  (try
    (resque-internal/unregister queues)
    (resque/stop)
    (catch java.lang.ClassCastException _)))

(defn stop-workers-on-shutdown!
  [queues]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. (partial stop-workers queues))))

(defmacro job
  [job-name args & body]
  `(defn ~job-name ~args
     (try
       (info "Executing job" ~(str job-name) "with" ~args)
       (stats/increment ~(str job-name))
       (benchmark ~(str job-name "-bench")
                  (do ~@body))
       (catch Exception e#
         (print-exception e#)
         (stats/increment ~(str job-name "-exception"))
         (throw e#)))))

(defn default-dispatch-error
  [exc]
  (print-exception exc)
  (System/exit 1))

(defn listen
  ([queues]
     (listen queues {}))
  ([queues opts]
     (info "Starting workers for" (clojure.string/join ", " queues))
     (stop-workers-on-shutdown! queues)
     (resque-super/start queues (merge {:dispatcher-error-handler default-dispatch-error} opts))))
