(ns messaging.receiver-multiple4
  (:use messaging.usage
	clojure.string :only [join]))

(defn handle-multiple-messages [handler]
  (doseq [message (message-seq "rabbitmq-test")]
    (handler message)))

#_(with-rabbit ["localhost" "guest" "guest"]
  (println "Waiting for messages...")
  (print-multiple-messages println))

