(ns chapter12.test.general-redis
  (:require [redis.core :as redis])
  (:use [chapter12.core]
	[clojure.test]))

(deftest ping
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/ping))
	 "PONG")))

(deftest vars
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/set "name" "deepthi"))
	 "OK"))

  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/get "name"))
	 "deepthi")))


(deftest strings
  (is (> (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/rpush "names" "adi"))
	 1))
  (is (> (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/rpush "names" "punit"))
	 2))
  )
(deftest lists
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/lindex "names" 0))
	 "adi"))
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/lindex "names" 1))
	 "punit"))
  )
  
  

  
(deftest sets
  (doall [(redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	    (redis/sadd "names-set" "amit"))
	  (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	    (redis/sadd "names-set" "adi")) ])
  (is (not (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	    (redis/sadd "names-set" "adi"))))
  (is (= (redis/with-server {:host "127.0.0.1" :port 6379 :db 0}
	   (redis/smembers "names-set"))
	 #{"adi" "amit"}))
  )