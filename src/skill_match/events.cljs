(ns skill-match.events
  (:require
   [re-frame.core :as re-frame]
   [skill-match.db :as db]
   [skill-match.functor :refer [fmap]]
   [skill-match.matching :as matching] 
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [clojure.set :as set]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _] db/default-db))

(defn- skills-matches-desc [desc-words]
  (fn [skill]
    (->> skill 
         matching/skill->words 
         (some desc-words))))

(defn- set-description [description db]
  (let [desc-words (matching/desc-words description)]
     (assoc db
            :job-description description
            :description-words desc-words
            ;; todo move things to on-write, clean complexity in subs, allow updating selection!??
            :current-selections (fmap 
                                 (fn [specific-skills]
                                   (->> specific-skills
                                        (map :id)
                                        (filter (skills-matches-desc desc-words))
                                        (set)))
                                 (:skills db)))))

(re-frame/reg-event-db
 ::description-update
 (fn-traced
   [db [_, description]]
   (set-description description db)))

(re-frame/reg-event-db
 ::update-selection
 (fn-traced
  [db [_ list-title selection]]
  (assoc-in db [:current-selections list-title] (set selection))))
