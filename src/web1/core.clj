(ns web1.core
  (:require [reitit.core :as r]
            [reitit.ring :as ring]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

(defn home [req]
  {:status 200
   :body "HOME"})

(defn toto [req]
  {:status 200
   :body "TOTO"})

(def router (ring/router [["/" home]
                          ["/toto" toto]]))

(def app (ring/ring-handler router))

(defn -main
  []
  (run-jetty (wrap-reload #'app)
             {:port 3000}))
