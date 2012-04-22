(defproject webbing "1.0.0-SNAPSHOT"
  :description "The grizzly webserver example from Clojure in Action, chapter 10"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [com.sun.grizzly/grizzly-webserver "1.9.46"]
		 [org.clojure.contrib/json "1.3.0-alpha4"]]
  :dev-dependencies [[swank-clojure "1.3.4" :exclusions [org.clojure/clojure]]]
  :jvm-opts ["-Xmx64M"]
  :extra-classpath-dirs ["classes"])
