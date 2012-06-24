(ns messaging.tutorial-one
  (:import (com.rabbitmq.client ConnectionFactory QueueingConsumer)))

(def ^:dynamic *queue-name*)

(defn connection []
  (.newConnection
   (doto (ConnectionFactory.)
     (.setHost "localhost"))))

