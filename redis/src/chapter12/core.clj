(ns chapter12.core
  (:require [redis.core :as redis])
  (:use [chapter12.redis-datamapper]))

;; (System/getProperty "java.class.path")

(def-redis-type consumer
  (string-type :id :merchant-id :start-time :time-zone)
  (list-type :cart-items)
  (primary-key :id :merchant-id)
  (format :json)
  (key-separator "##"))

(comment
  (def c (consumer :new))
  (c :set! :id "adi")
  (c :set! :merchant-id 14)
  (c :set! :start-time (System/currentTimeMillis))
  (c :add! :cart-items {:sku "ABC" :cost 10.95})
  (c :add! :cart-items {:sku "XYZ" :cost 22.40})
  (c :get :merchant-id)
  (c :get :cart-items)
  
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (c :save!))

  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (def d (consumer :find "adi" "14")))

  (d :get :id)
  (d :get :merchant-id)
  (d :get :cart-items))


