(ns web1.core
  (:require [reitit.core :as r]
            [reitit.ring :as ring])
  (:gen-class))


(defn home [req]
  {:status 200
   :body "HOME"})

(defn toto [req]
  {:status 200
   :body "TOTO"})

(def router (ring/router [["/" home]
                          ["/toto" toto]]))

(comment (r/match-by-path router "/toto"))

(def app (ring/ring-handler router))
