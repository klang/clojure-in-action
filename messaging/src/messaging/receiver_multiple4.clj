(ns messaging.receiver-multiple4
  (:use messaging.usage
	[clojure.string :as string :only (join)]))

(defn handle-multiple-messages [handler]
  (doseq [message (message-seq "rabbitmq-test")]
    (handler message)))

(defn print-two-messages [messages]
  (println (string/join "::" messages)))

(with-rabbit ["localhost" "guest" "guest"]
  (println "waiting for messages...")
  (let [message-pairs (partition 2 (message-seq "rabbitmq-test"))]
    (doseq [message-pair message-pairs]
      (print-two-messages message-pair))))

(comment

  (with-rabbit ["localhost" "guest" "guest"]
    (send-message "rabbitmq-test" "message 1")
    (send-message "rabbitmq-test" "message 2")
    (send-message "rabbitmq-test" "message 3")
    (send-message "rabbitmq-test" "message 4"))

  ;; -- this does not work, for some reason
  (with-rabbit ["localhost" "guest" "guest"]
    (map #(send-message "rabbitmq-test" (str "message " %)) (range 1 100)))
  ;; -- neither does this
  (for [x (range 1 100)] (send-message "rabbitmq-test" (str "message " x)))

  ;; -- of course ..
  (map #(with-rabbit ["localhost" "guest" "guest"]
	  (send-message "rabbitmq-test" (str "message " %)))
       (range 1 100))

  (time
   (for [x (range 1 100)]
     (with-rabbit ["localhost" "guest" "guest"]
       (send-message "rabbitmq-test" (str "message " x)))))
  
  )


