(ns chapter12.core
  (:require [redis.core :as redis]))
;; (System/getProperty "java.class.path")

(redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
  (redis/ping))

(comment
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/set "name" "deepthi"))

  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/get "name")))

(comment
  ;; strings
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/rpush "names" "adi"))
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/rpush "names" "punit"))

  ;; lists
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/lindex "names" 0))
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/lindex "names" 1))

  ;; sets
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/sadd "names-set" "amit"))
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/sadd "names-set" "adi"))
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/sadd "names-set" "adi"))

  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/smembers "names-set"))
  )

;; a redis data mapper

(use 'chapter12.redis-datamapper)

(def-redis-type consumer
  (string-type :id :merchant-id :start-time :timezone)
  (list-type :cart-items)
  (primary-key :id :merchant-id))

(consumer :name)
(consumer :format)
(consumer :key-separator)
(consumer :primary-key)

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
    (c :save!)))

;; redis 127.0.0.1:6379> get "##14##:merchant-id"
;; "14"
;; redis 127.0.0.1:6379> llen "##14##:cart-items"
;; (integer) 4
;; redis 127.0.0.1:6379> lrange "##14##:cart-items" 0 4
;; 1) "{\"sku\":\"XYZ\",\"cost\":22.4}"
;; 2) "{\"sku\":\"ABC\",\"cost\":10.95}"
;; 3) "{\"sku\":\"XYZ\",\"cost\":22.4}"
;; 4) "{\"sku\":\"ABC\",\"cost\":10.95}"

(comment
  (chapter12.redis-persistence/persistable-for c)
  
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/get "##14##:merchant-id"))
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/llen "##14##:cart-items"))
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/lrange "##14##:cart-items" 0 4))

  ((chapter12.redis-persistence/fetchers :list-type) "##14##:cart-items")

  (let [dakey "##14##:cart-items"]
    ((fn [key]
       {key {:value (redis/lrange key 0 (redis/llen key))
	     :key-type :list-type}}) dakey))

(redis/llen "##14##:cart-items")
;; definition of the server to use is simply missing?!?

  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (let [items (redis/llen "##14##:cart-items")]
      (redis/lrange "##14##:cart-items" 0 items)))



(redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
  (redis/lrange "##14##:cart-items" 0 (redis/llen "##14##:cart-items")))

(redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
  ((fn [key] {key {:value (redis/lrange key 0 (redis/llen key)) :key-type :list-type}})
   "##14##:cart-items"))

(redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
  (c :find 14 :cart-items)
  )
  



  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (redis/exists "##14##:cart-items"))
  
  )
(comment
  (chapter12.redis-persistence/persistable-for c)

  {"##14##:merchant-id" {:value "14", :key-type :string-type}, "##14##:start-time" {:value "1334415774798", :key-type :string-type}, "##14##:cart-items" {:value ("{\"sku\":\"XYZ\",\"cost\":22.4}" "{\"sku\":\"ABC\",\"cost\":10.95}"), :key-type :list-type}})


(comment
  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
    (def d (consumer :find "adi" "14")))

  (d :get :id)
  (d :get :merchant-id)
  (d :get :cart-items))


(comment
  (c :string-keys :id :merchant-id)
  )