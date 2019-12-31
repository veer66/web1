(ns web1.migrate
  (:require [migratus.core :as migratus]
            [next.jdbc :as j]
            [clojure.edn :as edn]))

(def db-spec (-> "db.edn"
                 slurp
                 read-string))

(def config {:store :database
             :db (j/get-datasource db-spec)})

(defn -main
  []
  (migratus/migrate config))

