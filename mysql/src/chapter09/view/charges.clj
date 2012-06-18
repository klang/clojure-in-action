(ns chapter09.view.charges
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css include-js]]
	[chapter09.helpers [defined-params]]
	[clojure.data.json :only [json-str]]
	[clojure.string :only (split triml trimr)]
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

;; dynamic html
(defn- data-group
  ([] (data-group {}))
  ([params]
     [:div
      [:table {:border 0}
       [:tr [:td "date"]     [:td [:input {:type "text" :name "date[]"}]]]
       [:tr [:td "vendor"]   [:td [:input {:type "text" :name "vendor_name[]"}]]]
       [:tr [:td "category"] [:td [:input {:type "text" :name "category[]"}]]]
       [:tr [:td "amount"]   [:td [:input {:type "text" :name "amount[]"}]]]
       ]]  
       ))

(defn- data-group
  ([] (data-group {}))
  ([params]
     [:div
      [:table
       [:tr
	[:td [:input {:type "text" :placeholder "date" :name "date[]"}]]
	[:td [:input {:type "text" :placeholder "vendor" :name "vendor_name[]"}]]
	[:td [:input {:type "text" :placeholder "category" :name "category[]"}]]
	[:td [:input {:type "text" :placeholder "amount" :name "amount[]"}]]]]]))

(defn dynamic-add-charge [data]
  [:div
   [:form {:method "POST" :action "/collect" }
    [:div {:id "newlink"} (data-group)]
    [:p
     [:button {:type "submit" :name "event" :value "create"} "add"]
     [:button {:type "reset" :name "reset"} "reset"]]
    [:p {:id "addnew"}
     [:a {:href "javascript:new_link()"} "Add New"]]]
   [:div {:id "newlinktpl" :style "display:none"} (data-group)]])

;; ----------------------------------------------------------------------------

(defn- present-charges [data]
  (html5
   [:head
    [:title "users"]
    (include-css "/stylesheets/screen.css")
    (include-css "/stylesheets/dynamic-add.css")
    (include-js "/javascript/dynamic-add-fields.js")]
   [:body
    [:div {:id "header"}
     [:h1 "charges"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:table {:width "100%"}
     (charge-header)
     (map charge-line data)
     (total-line data)
     ]
    [:hr]
    (dynamic-add-charge data)
    ]))

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

(defn split-amount [params]
  (merge (dissoc params :amount)
	 (zipmap [:amount_dollars :amount_cents]
		 (split (:amount params) #"[,.]" 2))))

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
      #_(comment
	  (= event "create")
	  (select-keys params [:date :amount :vendor_name :category])
	  ;; here, parameters are still inside their respective vectors
	  ;split-amount
	  
	  )
      
      
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

(comment
  (-> {:amount "23.23", :vendor "netto"}
      split-amount)
  (->
   {:* "/collect", :event "create", :amount ["123.45" "37.23" "13.34"], :category ["brød" "øl" "mælk"], :vendor_name ["netto" "netto" "netto"], :date ["2010-01-16" "2010-01-17" "2010-01-18"]}
   split-amount

   )  

  
  {:user_id        1
   :amount_dollars 10
   :amount_cents   0
   :vendor_name    "amazon"
   :date           "2010-01-01"}

  )
