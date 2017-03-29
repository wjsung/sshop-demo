(ns sshop-demo.db.core
  (:require
    [clojure.java.jdbc :as jdbc]
    [conman.core :as conman]
    [mount.core :refer [defstate]]
    [sshop-demo.config :refer [env]])
  (:import clojure.lang.IPersistentMap
           clojure.lang.IPersistentVector
           [java.sql
            BatchUpdateException
            Date
            Timestamp
            PreparedStatement]))

(defstate ^:dynamic *db*
           :start (conman/connect! {:jdbc-url (env :database-url)})
           :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")


(defn to-date [sql-date]
  (-> sql-date (.getTime) (java.util.Date.)))
