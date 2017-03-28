(ns sshop-demo.app
  (:require [sshop-demo.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
