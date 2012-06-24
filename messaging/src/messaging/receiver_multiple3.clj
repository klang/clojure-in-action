(ns messaging.receiver-multiple2
  (:use messaging.usage))

(defn handle-multiple-messages [handler]
  (doseq [message (message-seq "rabbitmq-test")]
    (handler message)))

#_(with-rabbit ["localhost" "guest" "guest"]
  (println "Waiting for messages...")
  (print-multiple-messages println))

