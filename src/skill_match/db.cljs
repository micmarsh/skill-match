(ns skill-match.db
  (:require
   [skill-match.functor :refer [fmap]]
   [skill-match.data :refer [skills-data]]))


(def default-db
  {:job-description ""
   
   :description-words #{}

   :skills (fmap (fn [items] (map #(identity {:id % :label %}) items)) 
                 skills-data) 
   
   :current-selections (fmap (fn[_] #{}) skills-data) ; {"Title" #{}}
   })

