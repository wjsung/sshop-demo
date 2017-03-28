(ns sshop-demo.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [sshop-demo.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[sshop-demo started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[sshop-demo has shut down successfully]=-"))
   :middleware wrap-dev})
