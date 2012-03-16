(ns chapter09.usage
  (:require (chapter09.model [user   :as user])
	    (chapter09.model [charge :as charge])))

;; http://localhost/phpmyadmin

(comment
  
  (user/create {:login "rob"
		:first_name "Robert"
		:last_name "Berger"
		:password "secret"
		:email_address "rob@runa.com"})

  (user/create {:login "rob2"
		:first_name "Robert"
		:last_name "Stevenson"
		:password "friday"
		:email_address "rob@crusoe.com"})

  (user/get-record 1)
  (user/find-records {:first_name "Robert"})
  (user/update {:login "stevenson" :id 2})
  (user/get-record 2)
  (user/destroy-record {:id 2})
  (user/find-records {:id 2})

  (charge/create {:user_id        1
		  :amount_dollars 11
		  :amount_cents   50
		  :category       "books"
		  :vendor_name    "amazon"
		  :date           "2010-01-15"})

  (charge/create {:user_id        1
		  :amount_dollars 27
		  :amount_cents   91
		  :category       "meals"
		  :vendor_name    "stacks"
		  :date           "2010-01-15"})

  (let [rob (user/get-record 1)]
    (user/find-charges rob))
  
  (let [errors (charge/validate {:amount_dollars 0
				 :amount_cents -10
				 :date "2010-01-10"
				 :vendor_name "amazon"})]
    (println errors))
  
  (charge/create {:user_id        1
		  :amount_dollars 10
		  :amount_cents   0
		  :vendor_name    "amazon"
		  :date           "2010-01-01"})
  )