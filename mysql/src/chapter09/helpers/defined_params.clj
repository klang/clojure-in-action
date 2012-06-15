(ns chapter09.helpers.defined-params)

(defn defined-params
  "returns only the parameters that are defined.
nil and empty string values are filtered out"
  [params]
  (into {} (filter
	    #(not (or (nil? (val %))
		      (= "" (val %))))
	    params)))