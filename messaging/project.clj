(defproject messaging "1.0.0-SNAPSHOT"
  :description "The RabbitMQ example from Clojure in Action, chapter 11"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [com.rabbitmq/amqp-client "2.8.2"]
		 [com.mefesto/wabbitmq "0.2.2"]])

;; erlang fetched via the repository
;; http://download.opensuse.org/repositories/devel:/languages:/erlang/openSUSE_12.1/

;; RabbitMQ server fetched and installed according to the instructions here:
;; http://www.rabbitmq.com/install-generic-unix.html

;; RabbitMQ java client fetched via marven
;; http://mvnrepository.com/artifact/com.rabbitmq/amqp-client/2.8.2