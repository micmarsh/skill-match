(ns skill-match.events
  (:require
   [re-frame.core :as re-frame]
   [skill-match.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _] db/default-db))

(re-frame/reg-event-db
 ::description-update
 (fn-traced 
  [db [_, val] ]  
  (assoc db :job-description val)))
