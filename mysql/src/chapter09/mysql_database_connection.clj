(ns chapter09.mysql-database-connection)
;; in the shell, execute:
;; sudo mysql < damages.sql
(def ^:dynamic db
     {:classname   "com.mysql.jdbc.Driver"
      :subprotocol "mysql"
      :user        "damages"
      :password    "damages"
      :subname     "//localhost:3306/damages"})
