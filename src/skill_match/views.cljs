(ns skill-match.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [skill-match.subs :as subs]
   [skill-match.events :as events]
   [re-com.core :refer [selection-list]]))

(defn when-enter 
  "Probably not the best way to manage this, but go with it for now"
  [act]
  (fn [react-e]
    (js/console.log react-e)
    (let [e react-e #_(.-nativeEvent react-e)]
      (when (= (.-code e) "Enter")
        (.preventDefault e)
        (act)))))

(defn main-panel []
  [:div
   [:h1 "Skills Section Generator"]
   [:div#description-skills-matcher
    {:style {:display "flex"}}
    [:div
     [:div
      [:h3 "Paste Job Description Here"]
      [:textarea#job-description
       {:style {:height 600 :width 450}
        :on-change #(->> % .-target .-value (vector ::events/description-update) re-frame/dispatch)}]]]
    (let [skills-lists (re-frame/subscribe [::subs/skills-lists])]
      (for [list @skills-lists
            :let [model (r/atom #{})]]
        ^{:key (key list)} ; probably not necessary, but good practice for you
        [:div [:h3 (key list)]
         [selection-list
          :choices (val list)
          :model model
          :on-change (fn [m]
                       (reset! model m)
                       (println m))]]))]])
