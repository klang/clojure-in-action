(defproject damages "1.0.0-SNAPSHOT"
  :description "The clj-record example from Clojure in Action, chapter 9"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [clj-record "1.1.1"]
		 [mysql/mysql-connector-java  "5.1.6"]]
  :dev-dependencies [[swank-clojure "1.3.4" :exclusions [org.clojure/clojure]]]
  :jvm-opts ["-Xmx64M"]
  :extra-classpath-dirs ["classes"])

