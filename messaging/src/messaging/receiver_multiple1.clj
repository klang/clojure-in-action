(ns messaging.receiver-multiple1
  (:use messaging.usage))

(defn print-multiple-messages []
  (loop [message (next-message-from "rabbitmq-test")]
    (println "Message: " message)
    (recur (next-message-from "rabbitmq-test"))))

(with-rabbit ["localhost" "guest" "guest"]
  (println "Waiting for messages...")
  (print-multiple-messages))

