(ns sshop-demo.routes.services.main
  (:require [sshop-demo.db.core :as db]
            [ring.util.http-response :as response]
            [buddy.hashers :as hashers]
            [clojure.tools.logging :as log]))

(defn products! []
  (when-let [list (db/get-products )]
  (-> {:result :ok
       :message list}
      (response/ok)
      )))


(defn product! [id]
  (-> {:result :ok}
      (response/ok)
      ))