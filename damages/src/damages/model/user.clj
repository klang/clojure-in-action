(ns damages.model.user
  (:use damages.mysql-database-connection)
  (:require clj-record.boot))

(clj-record.core/init-model
 :table-name "users"
 (:associations (has-many charges)))

