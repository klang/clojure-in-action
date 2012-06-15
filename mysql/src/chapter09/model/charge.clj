(ns chapter09.model.charge
  (:use chapter09.mysql-database-connection)
  (:require clj-record.boot))

(clj-record.core/init-model
 :table-name "charges"
 (:associations (belongs-to user))
 (:validation
  (:amount_dollars "Must be positive!" #(>= % 0))
  (:amount_cents "Must be positive!" #(>= % 0)))
 (:callbacks
  (:before-save (fn [record]
		  (if-not (:category record)
		    (assoc record :category "uncategorized")
		     record)))))

