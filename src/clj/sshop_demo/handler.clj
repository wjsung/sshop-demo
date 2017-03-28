(ns sshop-demo.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [sshop-demo.layout :refer [error-page]]
            [sshop-demo.routes.home :refer [home-routes]]
            [sshop-demo.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [sshop-demo.env :refer [defaults]]
            [mount.core :as mount]
            [sshop-demo.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    #'service-routes
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
