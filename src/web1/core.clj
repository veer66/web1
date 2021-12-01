(ns web1.core
  (:require [reitit.core :as r]
            [reitit.ring :as ring]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [next.jdbc :as j]
            [jsonista.core :as json]
            [next.jdbc.result-set :refer [ReadableColumn]]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m])
  (:import (org.postgresql.util PGobject)
           (com.zaxxer.hikari HikariDataSource))
  (:gen-class))

(defn load-ds []
  (let [db-spec (-> "db.edn"
                slurp
                read-string)]
    (doto (HikariDataSource.)
      (.setJdbcUrl (str "jdbc:"
                        (:dbtype db-spec)
                        "://"
                        (:host db-spec)
                        "/"
                        (:dbname db-spec)))
      (.setUsername (:user db-spec)))))

(defn >jsonb [d]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (json/write-value-as-string d))))

(defn read-pgobject
  [x]
  (condp = (.getType x)
      "jsonb" (->> x
                   .getValue
                   json/read-value)
      x))

(extend-protocol ReadableColumn
  PGobject
  (read-column-by-label [x _]
    (read-pgobject x))
  (read-column-by-index [x _2 _3]
    (read-pgobject x)))


(defonce ds
  (delay (load-ds)))

(defn home [req]
  {:status 200
   :body "HOME"})

(defn list-meta [req]
  {:status 200
   :content-type "application/json"
   :body (json/write-value-as-string
          {:meta (into []
                       (map :metadata)
                       (j/plan @ds ["SELECT * FROM meta1"]))})})

(defn save-meta [req]
  (let [{metadata :metadata id :id} (:body-params req)]
    (j/execute! @ds ["INSERT INTO meta1(id, metadata) VALUES (?, ?)"
                     id (>jsonb metadata)])
    {:status 200
     :content-type "application/json"
     :body (json/write-value-as-string
            {:result "OK"})}))

(comment (j/execute! @ds ["INSERT INTO meta1(id, metadata) VALUES (?, ?)"
                  10 (>jsonb {:k 10})]))

(def router
  (ring/router
   [["/" {:no-doc true
          :get home}]  
    ["/api" ;;{:data {:interceptors [(intercept-api)]}}
     ["/meta" {:get {:handler list-meta}
               :post {:handler save-meta}}]]
    ["/swagger.json" {:no-doc true
                      :get (swagger/create-swagger-handler)}]]
   {:data {:coercion reitit.coercion.spec/coercion
           :muuntaja m/instance
           :middleware [muuntaja/format-negotiate-middleware
                        muuntaja/format-response-middleware
                        muuntaja/format-request-middleware]}}))

(def app (ring/ring-handler router
                            (ring/create-default-handler)))

(defn -main
  []
  (run-jetty (wrap-reload #'app)
             {:port 3000}))
