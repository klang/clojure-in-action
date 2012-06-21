(ns messaging.receiver
  (:use messaging.usage))

(println "waiting ...")

(with-rabbit ["localhost" "guest" "guest"]
  (println (next-message-from "rabbitmq-test")))

