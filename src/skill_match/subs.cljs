(ns skill-match.subs
  (:require
   [re-frame.core :as re-frame]
   [reagent.dom.server :as render]))

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

(re-frame/reg-sub
 ::skills-html
 (fn [{:keys [current-selections]} _]
   (if (every? empty? (vals current-selections))
     ""
     (render/render-to-static-markup
      [:div
       [:h2 "Skills"]
       [:div {:style {:display "flex" :justify-content "space-around"}}
        (for [[list-title list-items] current-selections]
          [:div
           [:div.header-3 list-title]
           [:ul.skills-list (map #(vector :li %) list-items)]])]]))))
