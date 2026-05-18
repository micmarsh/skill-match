(ns skill-match.db
  (:require
   [skill-match.data :refer [skills-data]]
   [skill-match.matching :as matching]
   [cljs.spec.alpha :as s]))
;; 
;; (s/def ::skill-name string?)
;; (s/def ::list-name string?)
;; (s/def ::skill-id (s/tuple ::list-name ::skill-name))
;; 
;; (s/def ::skill-selected boolean?)
;; (s/def ::skill-body (s/keys :req-un [::]))

;; You basically need to "get over" this idea of separation of data and UI? Sounds like it
;; skills data needs to be processed into what's good for UI: {"List Title" [{:name "Thing", checked: bool}], ...}
;; UI iteration id can just be strings: titie and name
;; maaaybe search can be local state?

(def default-db
  {:job-description ""
   :skills (into {} (for [[title items] skills-data]
                      [title (map #(identity {:id % :label %}) items)]))
   :additional-selections {} ; {"List Title" #{words}}
   })
