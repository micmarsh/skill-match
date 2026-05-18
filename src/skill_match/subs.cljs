(ns skill-match.subs
  (:require
   [clojure.set :as set]
   [re-frame.core :as re-frame]
   [skill-match.matching :as matching]))

(defn def-extractor [key]
  (re-frame/reg-sub
   (keyword *ns* key)
   (fn [db _] (get db key))))

(re-frame/reg-sub
 ::description-words
 (fn [db]
   (matching/desc-words (:job-description db))))

(defn- skills-matches-desc [desc-words]
  (fn [skill]
    (-> skill
        matching/skill->words
        (set/intersection desc-words)
        (empty?)
        (not))))

(re-frame/reg-sub
 ::selection
 :<- [::description-words]
 :<- [::skills-lists]
 :<- [::additional-selections]

 (fn [[desc-words skills additional-selections],
      [_ list-title]]
   (let [specific-skills (get skills list-title)]
     (->> specific-skills 
          (map :id) 
          (filter (skills-matches-desc desc-words)) 
          (set)
          (set/union (get additional-selections list-title #{}))))))

(re-frame/reg-sub
 ::skills-lists
 (fn [db _] (:skills db)))

(re-frame/reg-sub
  ::ai-alert

 :<- [::description-words]
 (fn [words _]
   (not (empty? (set/intersection words #{"ai" "llm" "llms" "agentic"})))))

(def-extractor :additional-selections)

(comment (set/union nil nil #{})
         )