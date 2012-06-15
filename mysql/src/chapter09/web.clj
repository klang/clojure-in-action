(ns web
  (:use     compojure.core
	    ring.adapter.jetty
	    [hiccup.middleware  :only [wrap-base-url]]
	    [chapter09.view     index]
	    )
  (:require [compojure.route    :as   route]
	    [compojure.handler  :as   handler]
	    [compojure.response :as   response]
	    [chapter09.view     [charges :as charges]]
	    [chapter09.view     [users   :as users]]
	    )
  )

(defroutes main-routes
  (GET "/" [] (index))
  (ANY "/user"
       {:as request} (users/controller request))
  (ANY "/charges/find"
       {:as request} (charges/controller request))
  (ANY "/charges"
       {:as request} (charges/controller request))

  ;; REST
  (ANY "/json/user"
       {:as request} (users/json request))
  (ANY "/json/charges"
       {:as request} (charges/json request))

  (route/files "/")
  (ANY "*" {:as request} (str request)))

(def app (-> (handler/site main-routes)
	     (wrap-base-url)))

(defonce server (run-jetty #'app {:port 8080 :join? false}))
(defn start [] (.start server))
(defn stop [] (.stop server))

