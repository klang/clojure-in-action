(ns chapter09.view.index
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css]]))

(defn index []
  (html5
   [:head
    [:title "I'm on a Web"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"}
     [:h1 "Basic Compojure Application with Styles"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:div {:id "content"}
     [:div {:id "greeting"} "Prototypes"]
     [:div {:id "linklist"}
      [:li [:a {:href "/user"} "user"]]
      [:li [:a {:href "/charges"} "charges"]]      
      ]]]))