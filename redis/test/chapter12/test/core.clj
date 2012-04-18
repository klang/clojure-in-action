(ns chapter12.test.core
  (:require [redis.core :as redis])
  (:use [chapter12.core]
	[clojure.test]))


(deftest consumer-test
  (is (= (consumer :name)          'consumer))
  (is (= (consumer :format)        :json))
  (is (= (consumer :key-separator) "##"))
  (is (= (consumer :primary-key)   '(:id :merchant-id) )))

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

;; redis 127.0.0.1:6379> get "##14##:merchant-id"
;; "14"
;; redis 127.0.0.1:6379> llen "##14##:cart-items"
;; (integer) 4
;; redis 127.0.0.1:6379> lrange "##14##:cart-items" 0 4
;; 1) "{\"sku\":\"XYZ\",\"cost\":22.4}"
;; 2) "{\"sku\":\"ABC\",\"cost\":10.95}"
;; 3) "{\"sku\":\"XYZ\",\"cost\":22.4}"
;; 4) "{\"sku\":\"ABC\",\"cost\":10.95}"

(redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
  (def d (consumer :find "adi" "14")))

(deftest save-and-find-test
  (is (= (c :get :id) (d :get :id)))
  (is (= (c :get :merchant-id) (d :get :merchant-id)))
  (is (= (set (c :get :cart-items)) (set (d :get :cart-items)))))

(deftest save-test
  (is (=
       (chapter12.redis-persistence/persistable-for c)
       '{"adi##14##:id" {:value "\"adi\"", :key-type :string-type}, "adi##14##:merchant-id" {:value "14", :key-type :string-type}, "adi##14##:start-time" {:value "1334764257141", :key-type :string-type}, "adi##14##:cart-items" {:value ("{\"sku\":\"XYZ\",\"cost\":22.4}" "{\"sku\":\"ABC\",\"cost\":10.95}"), :key-type :list-type}}))

  (is (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	(redis/exists "##14##:cart-items")))
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/get "##14##:merchant-id"))
	 "14"))
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/llen "##14##:cart-items"))
	 4))
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/lrange "##14##:cart-items" 0 4))
	 (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (let [items (redis/llen "##14##:cart-items")]
	     (redis/lrange "##14##:cart-items" 0 items)))
	 (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/lrange "##14##:cart-items" 0 (redis/llen "##14##:cart-items")))
	 ["{\"sku\":\"XYZ\",\"cost\":22.4}" "{\"sku\":\"ABC\",\"cost\":10.95}" "{\"sku\":\"XYZ\",\"cost\":22.4}" "{\"sku\":\"ABC\",\"cost\":10.95}"]))

  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   ((fn [key] {key {:value (redis/lrange key 0 (redis/llen key))
			    :key-type :list-type}})
	    "##14##:cart-items"))
	 (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   ((chapter12.redis-persistence/fetchers :list-type) "##14##:cart-items"))
	 {"##14##:cart-items" {:value
			       ["{\"sku\":\"XYZ\",\"cost\":22.4}" "{\"sku\":\"ABC\",\"cost\":10.95}" "{\"sku\":\"XYZ\",\"cost\":22.4}" "{\"sku\":\"ABC\",\"cost\":10.95}"],
			       :key-type :list-type}}))
  )
