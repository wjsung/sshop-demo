(ns sshop-demo.validation
  (:require [struct.core :as st]
            [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn registration-errors [{:keys [pass-confirm] :as params}]
  (first
    (b/validate
      params
      :id   v/required
      :pass [v/required
             [v/min-count 4 :message "password must contain at least 4 characters"]
             [= pass-confirm :message "re-entered password does not match"]])))


(defn upload-product-errors [ {:keys [name price ] :as params}]
  (first
    (b/validate
      params
      :name   v/required
      :price   v/required
      )))