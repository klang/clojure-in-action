(ns chapter09.view.users
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css]]
	[chapter09.helpers [defined-params]]
	[clojure.data.json :only [json-str]]
	)
  (:require (chapter09.model [user   :as user-model])))

(defn- user-line [user]
  [:tr 
   [:td (:id user)]
   [:td (:login user)]
   [:td [:a {:href (url "/user" {:id (:id user) :event "find"})}
	 (str (:first_name user) " " (:last_name user))] ]
   [:td (user :email_address)]])

(defn- users [params]
  (html5
   [:head
    [:title "users"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:table {:width "100%"}
     [:tr 
      [:td "id"]
      [:td "login"]
      [:td "name" ]
      [:td "email address"]]
     (map #(user-line %) params)]]))

(defn- user [params]
  (html5
   [:head
    [:title "users"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"} [:h1 "users" [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:form {:method "POST" :action "/user" :id "user"}
     [:table
      [:tr [:td (text-field {:placeholder "id for lookup"} :id (:id params))]]
      [:tr [:td (text-field {:placeholder "login"} :login (:login params))]]
      [:tr [:td (text-field {:placeholder "first name"} :first_name (:first_name params))]]
      [:tr [:td (text-field {:placeholder "last name"} :last_name (:last_name params))]]
      [:tr [:td (password-field {:placeholder "password"} :password (:password params))]]
      [:tr [:td (text-field {:placeholder "email address"} :email_address (:email_address params))]]
      [:tr [:td
	    [:button {:type "submit" :name "event" :value "create"} "create"]
	    [:button {:type "submit" :name "event" :value "find"} "find"]
	    [:button {:type "submit" :name "event" :value "update"} "update"]
	    [:button {:type "submit" :name "event" :value "delete"} "delete"]]]]]
    [:fieldset [:legend "params"] [:pre (str params)]]]))

;;;----------------------------------------------------------------------------

(defn- handle-event [request]
  (let [event ((request :params) :event)
	params (defined-params (request :params))
	model-params (select-keys
		      params
		      [:email_address :password :last_name :first_name :login :id])]
    (try
      ;; the underlying model will complain if it is called with insufficient values
      ;; or if there are any other problems .. in either case, we don't care and return
      ;; a simple {} and return to the the data
      (cond (= event "find")   (user-model/find-records model-params)
	    (= event "create") (user-model/create (dissoc model-params :id))
	    (= event "update") (user-model/update model-params)
	    (= event "delete") (user-model/destroy-record (select-keys params [:id])))
      (catch Exception _ {}))))

(defn controller [request]
  (let [data (handle-event request)]
    (if (seq? data)
      (if (< 1 (count data))
	(users data)
	(user (first data)))
      (user data) #_(user (merge (request :params) data)))))

(defn json [request]
  (json-str (handle-event request)))


;;;----------------------------------------------------------------------------

(comment
  (def params {:email_address "karstenlang@gmail.com", :password "kalp909", :last_name "Lang", :first_name "Karsten", :login "klang", :id 7})

  (dissoc  (select-keys params [:email_address :password :last_name :first_name :login :id]) :id)
  
  (def data (user-model/create params))
  (def model-params (defined-params
 		      (select-keys params
 				   [:email_address :password :last_name :first_name :login :id])))
  
  (def find (user-model/find-records model-params))

  (seq? (user-model/find-records {:id 14}))
  (< 1 (count (user-model/find-records {:id 14})) )
  (:id (first (user-model/find-records {:id 14})) )
  )
 