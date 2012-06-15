(ns chapter09.view.dynamic-html-example
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css]]))

(defn dynamic-html-example [request]
  (html5
   [:head
    (include-css "/stylesheets/dynamic-add.css")]
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