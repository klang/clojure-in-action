(ns damages.mysql-database-connection)

(def ^:dynamic db
     {:classname   "com.mysql.jdbc.Driver"
      :subprotocol "mysql"
      :user        "damages"
      :password    "secret"
      :subname     "//localhost:3306/damages_dev"})
