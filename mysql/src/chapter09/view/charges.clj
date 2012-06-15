(ns chapter09.view.charges
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css]]
	[chapter09.helpers [defined-params]]
	[clojure.data.json :only [json-str]]
	)
  (:require (chapter09.model [user   :as user])
	    (chapter09.model [charge :as charge])))

;;-----------------------------------------------------------------------------
;; Present a form that let's us search for a user, based on login-name
;; The full user form from view/users could be used for this, but isn't yet.
;; The event is handed off to the controller, which finds the data and the next
;; page to return.

(defn- present-find [params]
  (html5
   [:head
    [:title "charges"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"}
     [:h1 "charges"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:form {:method "POST" :action "/charges" :id "user"}
     [:table {:width "100%"}
      [:tr [:td (text-field {:placeholder "login"} :login (:login params))]]
      [:tr [:td [:button {:type "submit" :name "event" :value "find"} "find"]]]]]]
   ))

;;-----------------------------------------------------------------------------
;; Present the users that have been found by searching
;; Give the opportunity to "select" one of them
(defn- user-header []
  [:tr
   [:td "id"]
   [:td "login"]
   [:td "name" ]
   [:td "email address"]])

(defn- user-line
  ([user]
     [:tr
      [:td (:id user)]
      [:td (:login user)]
      [:td [:a {:href (url "/charges" {:id (:id user) :event "select"})}
	    (str (:first_name user) " " (:last_name user))] ]
      [:td (user :email_address)]]))

(defn- present-users [data]
  (html5
   [:head
    [:title "users"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:table {:width "100%"}
     (user-header)
     (map user-line data)]]))

(comment
  (map
   user-line
   (-> {:login "klang"}
       (user/find-records)))

  (present-users
   (-> {:login "klang"}
	       (user/find-records)))
  )

;;-----------------------------------------------------------------------------
;; Present a list of charges for a specific user
;; in due time, new charges can be added in this view

(defn- charge-header []
  [:tr
   [:td "id"]
   [:td "date"]
   [:td "vendor"]
   [:td "category"]
   [:td "amount"]])

(defn- charge-line [charge]
  [:tr
   [:td (:id charge)]
   [:td (str (:date charge))]
   [:td (:vendor_name charge)]
   [:td (:category charge)]
   [:td (str (:amount_dollars charge) "." (:amount_cents charge))]])

(defn- find-total [data]
  (apply +
	 (map
	  #(float (+ (:amount_dollars %) (/ (:amount_cents %) 100))) data)))

(defn- total-line [data]
  [:tr
   [:td {:colspan "4"} "total"]
   [:td  (format "%.2f" (float (find-total data)))]])

(comment
  (format "%.2f" 123.12341)
  (format "%.2f" (float 0.0))
  )

(defn- add-charge []
  [:tr [:td
	[:button {:type "submit" :name "event" :value "create"} "add"]]]
  )

(defn- present-charges [data]
  (html5
   [:head
    [:title "users"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"}
     [:h1 "charges"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:table {:width "100%"}
     (charge-header)
     (map charge-line data)
     (total-line data)]
    (add-charge)]))

(comment
  (map
   charge-line
   (-> 1
     (user/get-record)
     (user/find-charges)))

  (apply + 0.0
	 (map
	  #(float (+ (:amount_dollars %) (/ (:amount_cents %) 100)))
	  (-> 1
	      (user/get-record)
	      (user/find-charges))))
  
  (= (present-charges
      (-> 1
	  (user/get-record)
	  (user/find-charges)))
  
     (-> 1
	 (user/get-record)
	 (user/find-charges)
	 (present-charges)))
  )


;;-----------------------------------------------------------------------------
;; handler/controller .. it seems that we have more names for the same thing
;; this will be reduced 

(defn- handle-event [request]
  (let [event ((request :params) :event)
	params (defined-params (request :params))
	user-params (select-keys params [:login :id])
	charge-params
	(select-keys params
		     [:id :user_id :amount_dollars :amount_cents :category :vendor_name :date])]
    (try
      ;; the underlying model will complain if it is called with insufficient values
      ;; or if there are any other problems .. in either case, we don't care and return
      ;; a simple {} and return to the the data
      (cond (= event "find")   (user/find-records user-params)
	    (= event "add-charge") (charge/create charge-params)
	    (= event "update") (charge/update charge-params)
	    (= event "delete") (charge/destroy-record (select-keys params [:id])))
      (catch Exception _ {}))))


(defn controller [request]
  (let [event  ((request :params) :event)
	params (defined-params (request :params))]
    (cond
     ;; should default to present-charges if only one user is found
     (and
      (or (:login params)
	  (:id params))
      (= event "find"))   (-> (select-keys params [:login :id])
			      (user/find-records)
			      (present-users))
     
     (and
      (:id params)
      (= event "select")) (-> (:id params)
			      (user/get-record)
			      (user/find-charges)
			      (present-charges))

     :else                (-> params
			      (present-find)))))

(defn json [request]
  (json-str (handle-event request)))

(comment
  (apply
   #(-> (:id %)
	(user/get-record) (user/find-charges))
   (-> (select-keys {:login "rob"} [:login :id])
       user/find-records))


 (-> {:login "rob"}
     (user/find-records))

 (-> 1
     (user/get-record)
     (user/find-charges))
 )
