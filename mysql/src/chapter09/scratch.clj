(ns chapter09.scratch
  (:use [clojure.data.json :only [read-json json-str]]))


(defn d [params] params)
(d {:params {"id" 123}}) ;;=> {:params {"id" 123}}

(defn d [{params :params}] params)
(d {:params {"id" 123}}) ;;=> {"id" 123}

(defn d  [{{id "id"} :params}] id)
(d {:params {"id" 123}}) ;;=> 123

;; http://briancaper.net/blog/493/clojure-orm-ish-stuff

(def foo [{:id 1 :foo 123} {:id 2 :foo 456}])
(def bar [{:foo_id 1 :bar 111} {:foo_id 1 :bar 222}])

(defn one-to-many
  ([xs name ys f]
     (for [x xs :let [ys (filter (partial f x) ys)]]
       (assoc x name ys))))
(comment
  (one-to-many foo :bars bar #(= (:id %1) (:foo_id %2)))
  ({:bars ({:bar 111,
	    :foo_id 1}
	   {:bar 222,
	    :foo_id 1}),
    :foo 123,
    :id 1}
   {:bars (),
    :foo 456,
    :id 2})
  )

(defn key=
  ([xkey ykey]
     #(= (xkey %1) (ykey %2))))

(comment
  (one-to-many foo :bars bar (key= :id :foo_id))
  ({:bars ({:bar 111,
	    :foo_id 1}
	   {:bar 222,
	    :foo_id 1}),
    :foo 123,
    :id 1}
   {:bars (),
    :foo 456,
    :id 2}))

(def baz [{:foo_id 1 :baz 555} {:foo_id 2 :baz 999}])

(-> foo
    (one-to-many :bars bar (key= :id :foo_id))
    (one-to-many :bazzes baz (key= :id :foo_id)))

(comment
  ({:bazzes ({:foo_id 1, :baz 555}),
    :bars ({:bar 111,
	    :foo_id 1}
	   {:bar 222,
	    :foo_id 1}),
    :foo 123,
    :id 1}
   {:bazzes ({:foo_id 2,
	      :baz 999}),
    :bars (),
    :foo 456,
    :id 2})
  )

;; TODO: one-to-one
;; TODO: many-to-many (via cross-table / "join" table)