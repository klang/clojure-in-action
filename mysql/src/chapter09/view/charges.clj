(ns chapter09.view.charges
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css]]
	[chapter09.helpers [defined-params]]
	[clojure.data.json :only [json-str]]
	)
  (:require (chapter09.model [user   :as user])
	    (chapter09.model [charge :as charge])))

(defn- present-find [params]
  (html5
   [:head
    [:title "charges"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"}
     [:h1 "charges"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:form {:method "POST" :action "/charges/find" :id "user"}
     [:table
      [:tr [:td (text-field {:placeholder "login"} :login (:login params))]]
      [:tr [:td [:button {:type "submit" :name "event" :value "find"} "find"]]]]]]
   ))

(defn- charges [params]
  (html5
   [:head
    [:title "users"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"}
     [:h1 "charges"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    ;; lookup user, add charge to the user
    ;; lookup all charges
    [:form {:method "POST" :action "/user" :id "user"}
     [:table
      [:tr [:td (text-field {:placeholder "login"} :login (:login params))]]
      [:tr [:td [:button {:type "submit" :name "event" :value "find"} "find"]]]]]]
   ))

(defn- present-user [user]
  [:tr 
   [:td (:id user)]
   [:td (:login user)]
   [:td (str (:first_name user) " " (:last_name user)) ]
   [:td (:email_address user)]])

(comment
  (apply
   present-user
   (-> {:login "rob"}
       (user/find-records)))
  )

(defn- charge-header []
  [:tr 
   [:td "id"]
   [:td "date"]
   [:td "vendor"]
   [:td "category"]
   [:td "amount"]]
  )

(defn- charge-line [charge]
  [:tr 
   [:td (:id charge)]
   [:td (str (:date charge))]
   [:td (:vendor_name charge)]
   [:td (:category charge)]
   [:td (str (:amount_dollars charge) "." (:amount_cents charge))]]
  )


(comment
  (map
   charge-line
   (-> 1
     (user/get-record)
     (user/find-charges)))  
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
    [:table
     (charge-header)
     (map charge-line (:charges data))
     ]]
   )
  )


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
    (cond (= event "find") (-> (select-keys params [:login :id])
			       (user/find-records)
			       )
	  :else
	  (present-find params)
	  (comment (= event "select")) (-> (:id params)
					  (user/get-record)
					  (user/find-charges)
					  #_(present-charges)))
    ))

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
