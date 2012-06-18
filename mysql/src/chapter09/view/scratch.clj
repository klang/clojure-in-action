(ns chapter09.view.scratch
  (:use	[hiccup core form def util]
	[hiccup.page :only [html5 include-css include-js]]
	))

;; the straight up translation of the html example
;; with the repetition of the template that html demands.
(defn dynamic-html-example [request]
  (html
   [:head
    (include-css "/stylesheets/dynamic-add.css")
    (include-js "/javascript/dynamic-add-fields.js")]
   [:body
    [:form {:method "POST" :action "/collect" }
     [:div {:id "newlink"}
      [:div
       [:table {:border 0}
	[:tr
	 [:td " Link URL: "]
	 [:td [:input {:type "text" :name "linkurl[]" :value "http://www.satya-weblog.com"}]]]
	[:tr
	 [:td " Link Description: "]
	 [:td [:textarea {:name "linkdesc[]" :cols "50" :rows "5"}]]]]]]
     [:p
      [:br]
      [:input {:type "submit" :name "submit1"}]
      " "
      [:input {:type "reset" :name "reset1"}]]
     [:p {:id "addnew"}
      [:a {:href "javascript:new_link()"} "Add New"]]]
    ;; template for new information block
    [:div {:id "newlinktpl" :style "display:none"}
     [:div
       [:table {:border 0}
	[:tr
	 [:td " Link URL: "]
	 [:td [:input {:type "text" :name "linkurl[]" :value "http://www.satya-weblog.com"}]]]
	[:tr
	 [:td " Link Description: "]
	 [:td [:textarea {:name "linkdesc[]" :cols "50" :rows "5"}]]]]]]]))

;; by separating the data-group, we only need to maintain the table once
;; and the resulting code will be easier to get right
(defn- data-group
  ([] (data-group {}))
  ([params]
     [:div
      [:table {:border 0}
       [:tr
	[:td " Link URL: "]
	[:td [:input {:type "text" :name "linkurl[]" :value (:linkurl params)}]]]
       [:tr
	[:td " Link Description: "]
	[:td [:textarea {:name "linkdesc[]" :cols "50" :rows "5"} (:linkdesc params)]]]]]))

(defn dynamic-html-example [request]
  (html5
   [:head
    (include-css "/stylesheets/dynamic-add.css")
    (include-js "/javascript/dynamic-add-fields.js")]
   [:body
    [:form {:method "POST" :action "/collect" }
     [:div {:id "newlink"}
      (data-group {:linkurl "http://www.satya-weblog.com"})]
     [:p
      [:br]
      [:input {:type "submit" :name "submit1"}]
      " "
      [:input {:type "reset" :name "reset1"}]]
     [:p {:id "addnew"}
      [:a {:href "javascript:new_link()"} "Add New"]]]
    ;; template for new information block
    [:div {:id "newlinktpl" :style "display:none"}
     (data-group)]]))

