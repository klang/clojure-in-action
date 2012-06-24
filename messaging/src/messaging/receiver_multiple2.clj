(ns messaging.receiver-multiple2
  (:use messaging.usage))

(defn handle-multiple-messages [handler]
  (loop [message (next-message-from "rabbitmq-test")]
    (handler message)
    (recur (next-message-from "rabbitmq-test"))))

#_(with-rabbit ["localhost" "guest" "guest"]
  (println "Waiting for messages...")
  (print-multiple-messages println))

