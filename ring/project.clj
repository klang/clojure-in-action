(defproject ring "1.0.0-SNAPSHOT"
  :description "The ring example from Clojure in Action, chapter 10.2 and 10.3"
  :dependencies [[clojure "1.3.0"]
		 [ring "1.1.0"]
		 [compojure "1.0.2"]
		 [hiccup "1.0.0"]
		 ]
  :dev-dependencies [[swank-clojure "1.3.4" :exclusions [clojure]]]
  :jvm-opts ["-Xmx64M"]
  :extra-classpath-dirs ["classes"])
