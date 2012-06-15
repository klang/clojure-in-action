(ns chapter09.model.user
  (:use chapter09.mysql-database-connection)
  (:require clj-record.boot))

(clj-record.core/init-model
 :table-name "users"
 (:associations (has-many charges)))

