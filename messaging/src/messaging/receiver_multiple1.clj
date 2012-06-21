(ns messaging.receiver-multiple1
  (:use messaging.usage))

(with-rabbit ["localhost" "guest" "guest"]
  (println (next-message-from "rabbitmq-test")))

