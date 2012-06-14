(ns hello-world-hiccup
  (:use compojure.core, ring.adapter.jetty, hiccup.core)
  (:require [compojure.route :as route]))

(defroutes main-routes
  (GET "/" [] (html [:h1 "Hello World"]))
  (route/not-found (html [:h1 "Page not found"])))

;;(run-jetty main-routes {:port 8080})