(ns skill-match.subs
  (:require
   [re-frame.core :as re-frame]
   [reagent.dom.server :as render]
   [skill-match.matching :as matching]
   [skill-match.effects.highlight :as highlight]))

(defn def-extractor [full-key]
  (re-frame/reg-sub 
   full-key
   (fn [db _] (get db (keyword (name full-key))))))

(def-extractor ::job-description)

(re-frame/reg-sub
 ::skills-lists
 (fn [db _] (:skills db)))

(re-frame/reg-sub
 ::selection
 (fn [db [_ list-title]] 
   (get-in db [:current-selections list-title] #{})))

(re-frame/reg-sub
 ::current-selections-words
 (fn [{:keys [current-selections]}]
   (->> current-selections
        (vals)
        (apply concat)
        (mapcat matching/skill->words)
        (set))))

(re-frame/reg-sub
 ::ai-words
 ;; this is a goofy way to do things: first arg is vector if multiple, otherwise is single
 ;; that's not consistent typing!
  (fn [{:keys [description-words]} _]
    (->> description-words 
         (filter highlight/ai-buzzwords)
         (set))))


(defn- highlight-map [color word]
  {:className color :highlight word})

(re-frame/reg-sub
 ::highlights
 :<- [::current-selections-words]
 :<- [::ai-words]
 (fn [[skill-words ai-words] _]
   (concat (map (partial highlight-map "red") ai-words)
           (map (partial highlight-map "green") skill-words))))

(re-frame/reg-sub
 ::ai-alert
 :<- [::ai-words]
 (fn [words _] (not-empty words)))

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
