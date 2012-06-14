(ns chapter10.sub4
  (:use compojure.core
	ring.adapter.jetty
	[hiccup core]
	[hiccup form]
	[hiccup.middleware :only [wrap-base-url]])
  (:require [compojure.route :as route]
	    [compojure.handler :as handler]
	    [compojure.response :as response])
  )

;;(html [:select [:option "foo"]])



(defroutes main-routes
  (GET "/" [] (html [:h1 "Hello, world!"]))
  (GET "/echo" {params :params}
       (html [:h2 "you said " (:message params)]))
  (route/not-found (html [:h1 "Page Not Found!"])))


(def app (-> (handler/site main-routes)
	     (wrap-base-url)))

(defonce server (run-jetty #'app {:port 8082 :join? false}))
(defn start [] (.start server))
(defn stop [] (.stop server))

(comment
  (html [:select {:class "foo" :name "selection[]"}
	 (select-options [["option 1" "value 1"] ["option 2" "value 2"]] "value 2")]))
