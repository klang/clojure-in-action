(defproject damages "1.0.0-SNAPSHOT"
  :description "The clj-record example from Clojure in Action, chapter 9"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [org.clojars.tavisrudd/redis-clojure "1.3.1"]
		 [org.clojure.contrib/json "1.3.0-alpha4"]
		 ]
  :dev-dependencies [[swank-clojure "1.3.4" :exclusions [org.clojure/clojure]]]
  :jvm-opts ["-Xmx64M"]
  :extra-classpath-dirs ["classes"])

