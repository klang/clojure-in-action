(ns chapter10.sub2
  ;; chapter13-ring
  (:use [ring.adapter.jetty :only [run-jetty]])
  )

(defn hello [req]
  {:body "hello world!"})

(def application-routes hello)
;; http://localhost:8080

;; a different way to define the server is given here.
;; the ability to start/stop several different servers is usefull
(defonce server (run-jetty #'application-routes {:port 8080 :join? false}))
(defn start [] (.start server))
(defn stop [] (.stop server))

(defn echo [req]
     (let [qs (:query-string req)
	   params (apply hash-map (.split qs "="))]
       {:body (params "echo")}))

(comment
  (def application-routes echo)
  ;; http://localhost:8080/?echo=hello
  )

(use 'ring.middleware.params)

(defn new-echo [req]
  #_(println "new")
  {:body (get-in req [:query-params "echo"])})

(def echo-app (wrap-params new-echo))

(comment
  (def application-routes echo-app)
  ;; http://localhost:8080/?echo=hello
  )
