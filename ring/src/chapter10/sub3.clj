(ns chapter10.sub3
  (:use compojure.core
	ring.adapter.jetty
	[hiccup core]
	[hiccup.middleware :only [wrap-base-url]])
  (:require [compojure.route :as route]
	    [compojure.handler :as handler]
	    [compojure.response :as response])
  )

;; http://weavejester.github.com/compojure/compojure.handler.html
;; As of version 0.6.0, Compojure no longer adds default middleware to
;; routes. This means you must explicitly add the wrap-params and wrap-cookies
;; middleware to your routes.

;; https://github.com/weavejester/compojure-example/blob/master/src/compojure/example/routes.clj

(defroutes main-routes
  (GET "/" [] (html [:h1 "Hello, world!"]))
  (GET "/echo" {params :params}
       (html [:h2 "you said " (:message params)]))
  (route/not-found (html [:h1 "Page Not Found!"])))


(def app (-> (handler/site main-routes)
	     (wrap-base-url)))

;; a different way to define the server is given here.
;; the ability to start/stop several different servers is usefull
(defonce server (run-jetty #'app {:port 8081 :join? false}))
(defn start [] (.start server))
(defn stop [] (.stop server))

;; http://localhost:8080
;; returns html
;;(response)