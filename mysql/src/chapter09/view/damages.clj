(ns chapter09.view.damages
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css]]))

(defn damages []
  (html5
   [:head
    [:title "damages"]
    (include-css "/stylesheets/screen.css")]
   [:body
    [:div {:id "header"}
     [:h1 "damages"
      [:span [:a {:href "/" :id "home"} "Home"]]]]
    [:div {:id "content"}
     [:div {:id "greeting"} "Prototypes"]
     [:div {:id "linklist"}
      [:li
       [:a {:href "/"} ""]]
      [:li 
       [:a {:href (url "/" {:foo 8}) } "/?foo=8"]]
      ]]]))