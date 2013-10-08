# resque-worker

A library that allows you to easily create a new resque worker in Clojure. Uses [resque-clojure](https://github.com/jxa/resque-clojure) underneath.

Features:

* Logging when job starts/ends, time it took.
* Exception boxing - no error is allowed to kill the runtime. Show must go on.
* Exception is a job dispatcher exception - it kills the runtime if redis is unavailable. You need to have monit/god watching the process.
* Optional statsd integration.
* Graceful termination on runtime shutdown.

## Latest version on [Clojars](https://clojars.org/resque-worker)

```clojure
[resque-worker "1.0.1"]
```

## Usage

```clojure
(ns example1
  (require [resque-worker.core :refer [job] :as resque]))

(job welcome-email
  [user-name email]
  (send-email email (str "Hello " user-name)))

(defn start
  []
  (resque/configure :host "my-redis.local"
                    :password "ABCDEFGHIJ")
  (resque/listen "emails"))
```

Statsd integration is part of the resque-worker package, so you just need to configure the statsd package. 
If you are already using statsd in your project through clj-statsd, it should work without any configuration.

```clojure
(ns example2
  (require [resque-worker.core :refer [job] :as resque]
           [clj-statsd :as statsd]))

(defn start
  []
  (resque/configure)
  (statsd/setup "127.0.0.1" 8125)
  (resque/listen "emails"))
```

## License

Copyright © 2013 Dima Sabanin

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

