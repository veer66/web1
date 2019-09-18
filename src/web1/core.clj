(ns web1.core
  (:gen-class))

(defn app [request]
  {:status 200
   :body "@@@@@@@@@@@@@@@@@@ Hi @@@@@@@@@@@@@@@@@"})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
