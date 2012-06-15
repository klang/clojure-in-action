(defproject mysql "1.0.0-SNAPSHOT"
  :description "The mySql & clj-record example from Clojure in Action, chapter 9.1.
Extended with a compojure front end as well as json return value options."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 ;; chapter09.mysql
                 [clj-record "1.1.1"]
		 [mysql/mysql-connector-java  "5.1.6"]
		 ;; chapter09.compojure
		 [ring "1.1.0"]
		 [compojure "1.0.2"]
		 [hiccup "1.0.0"]
		 [org.clojure/data.json "0.1.2"]		 
		 ])