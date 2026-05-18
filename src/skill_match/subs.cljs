(ns skill-match.subs
  (:require
   [clojure.set :as set]
   [re-frame.core :as re-frame]
   [skill-match.matching :as matching]))

(re-frame/reg-sub
 ::test-job-desc
 (fn [db]
   (:job-description db)))

(defn- skills-matches-desc [desc-words]
  (fn [skill]
    (-> skill
        matching/skill->words
        (set/intersection desc-words)
        (empty?)
        (not))))

(re-frame/reg-sub
 ::selection
 
 (fn [{:keys [job-description skills additional-selections]},
      [_ list-title]]
   (let [specific-skills (get skills list-title)
         desc-words (matching/desc-words job-description)]
     (->> specific-skills 
          (map :id) 
          (filter (skills-matches-desc desc-words)) 
          (set)
          (set/union (get additional-selections list-title #{}))))))

(re-frame/reg-sub
 ::skills-lists
 (fn [db _] (:skills db)))

(comment (set/union nil nil #{})
         )