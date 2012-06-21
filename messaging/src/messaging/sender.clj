(ns messaging.sender
  (:use messaging.usage))

(println "sending ...")

(with-rabbit ["localhost" "guest" "guest"]
  (println (send-message "rabbitmq-test" "mq first message")))
(println "done!")