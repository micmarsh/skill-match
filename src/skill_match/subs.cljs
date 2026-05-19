(ns skill-match.subs
  (:require
   [re-frame.core :as re-frame]))

(defn def-extractor [key]
  (re-frame/reg-sub
   (keyword 'skill-match.subs key)
   (fn [db _] (get db key))))


(re-frame/reg-sub
 ::skills-lists
 (fn [db _] (:skills db)))

(re-frame/reg-sub
 ::selection
 (fn [db [_ list-title]] 
   (get-in db [:current-selections list-title] #{})))

(re-frame/reg-sub
 ::ai-alert
 ;; this is a goofy way to do things: first arg is vector if multiple, otherwise is single
 ;; that's not consistent typing!
 (fn [{:keys [description-words]} _]
   (some #{"ai" "llm" "llms" "agentic"} description-words)))
