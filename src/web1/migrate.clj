(ns web1.migrate
  (:require [migratus.core :as migratus]
            [next.jdbc :as j]
            [clojure.edn :as edn]))

(prn "XXXXXX")


(println (clojure.edn/read-string (slurp "db.edn")))

(def db-spec (-> "db.edn"
                 slurp
                 read-string))
(prn db-spec)
(prn "---------------------------")
(def config {:store :database
             :db (j/get-datasource db-spec)})

(defn -main
  []
  (println "@@@@@@@@@@@@")
  (migratus/migrate config))

