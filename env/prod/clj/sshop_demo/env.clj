(ns sshop-demo.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[sshop-demo started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[sshop-demo has shut down successfully]=-"))
   :middleware identity})
