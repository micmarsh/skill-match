(ns skill-match.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [skill-match.subs :as subs]
   [skill-match.events :as events]
   [re-com.core :refer [selection-list]]
   
   ["jquery" :as jQuery] 
   ["highlight-within-textarea"]))

(defn- skill-checkbox-lists []
  (doall
   (let [skills-lists (re-frame/subscribe [::subs/skills-lists])]
     (for [list @skills-lists
           :let [list-title (key list)
                 selection @(re-frame/subscribe [::subs/selection list-title])
                 model (r/atom selection)]]
       ^{:key list-title}
       [:div [:h3 (str list-title " (" (count @model) ")")]
        [selection-list
         :choices (val list)
         :model model
         :on-change (fn [m] (re-frame/dispatch [::events/update-selection list-title m]))]]))))

(def $ jQuery)

(println $)

(def ^:const textbox-size
  {:height 600 :width 450})

(defn job-description [] 
  (r/create-class
   {:component-did-mount
    (fn [_]
      (-> ($ "#job-description")
          (.highlightWithinTextarea #js {:highlight "foo"})))
    :reagent-render
    (fn []
      [:textarea#job-description
       {:style textbox-size
        :on-change #(->> % .-target .-value (vector ::events/description-update) re-frame/dispatch)}])}))

(defn- ai-alert []
  (let [has-ai? (re-frame/subscribe [::subs/ai-alert])]
    (when @has-ai?
      [:span {:style {:color "red"}} "⚠️ This Posting Refers to various AI buzzwords"])))

(defn- copy-html []
  (let [html (re-frame/subscribe [::subs/skills-html])]
    [:div
     [:button {:on-click #(js/navigator.clipboard.writeText @html)}
      "Copy"]]))

(defn main-panel []
  [:div
   [:h1 "Skills Section Generator"]
   [:div#description-skills-matcher
    {:style {:display "flex"}}
    [:div
     [job-description]
     [ai-alert]]
    (skill-checkbox-lists)]
   [copy-html]])


