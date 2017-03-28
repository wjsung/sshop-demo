(ns user
  (:require [mount.core :as mount]
            [sshop-demo.figwheel :refer [start-fw stop-fw cljs]]
            sshop-demo.core))

(defn start []
  (mount/start-without #'sshop-demo.core/http-server
                       #'sshop-demo.core/repl-server))

(defn stop []
  (mount/stop-except #'sshop-demo.core/http-server
                     #'sshop-demo.core/repl-server))

(defn restart []
  (stop)
  (start))


