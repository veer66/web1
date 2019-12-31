(ns web1.create-migration
  (:require [migratus.core :as migratus]
            [hikari-cp.core :refer [make-datasource]]
            [clojure.edn :as edn]))

(def config {:db (edn/read-string (slurp "db.edn"))})

(defn -main
  [& args]
  (if-let [[name] args]
    (migratus/create config name)
    (println "Usage: clojure -m db1.create-migration <name>")))

