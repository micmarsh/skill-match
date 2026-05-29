(ns skill-match.views.views
  (:require
   [re-com.core :refer [selection-list]]
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [skill-match.events :as events]
   [skill-match.subs :as subs]
   [skill-match.views.components :refer [highlightable-textarea]]))

(def ^:const desc-dom-id "job-description")

(def ^:const skill-list-max 9)

(defn- skill-checkbox-lists []
  (let [skills-lists (re-frame/subscribe [::subs/skills-lists])]
    (for [list @skills-lists
          :let [list-title (key list)
                selection @(re-frame/subscribe [::subs/selection list-title])
                model (r/atom selection)]]
      ^{:key list-title}
      [:div [:h3 (str list-title " (" (count @model) "/" skill-list-max ")")]
       [selection-list
        :choices (val list)
        :model model
        :on-change (fn [m] (re-frame/dispatch [::events/update-selection list-title m]))]])))


(def ^:const textbox-size
  {:height 600 :width 475})


(defn job-description []
  [:div
   [:h3 "Paste Job Description Here"]
   (let [highlights (re-frame/subscribe [::subs/highlights])]
     [highlightable-textarea
      {:id desc-dom-id
       :style textbox-size
       :on-change #(->> % .-target .-value (vector ::events/description-update) re-frame/dispatch)
       :highlights highlights }])])

(defn- ai-alert []
  (let [has-ai? (re-frame/subscribe [::subs/ai-alert])]
    (when @has-ai?
      [:div {:style {:color "red"}} "⚠️ This Posting Refers to various AI buzzwords"])))

(defn- copy-html []
  (let [html (re-frame/subscribe [::subs/skills-html])]
    [:div
     [:button {:on-click #(js/navigator.clipboard.writeText @html)}
      "Copy"]]))

(def ^:const horizontal-row {:display "flex"})

(defn main-panel []
  [:div
   [:h1 "Skills Section Generator"]
   (into
    [:div#description-skills-matcher
     {:style horizontal-row}
     [:div
      [job-description]
      [ai-alert]]]
    (skill-checkbox-lists))
   [copy-html]
   [:span "*" skill-list-max " is the current recommended number of skills per list given formatting of rest of resume"]])
