(ns skill-match.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [skill-match.subs :as subs]
   [skill-match.events :as events]
   [re-com.core :refer [selection-list]]
   ["react-highlight-within-textarea" :refer [HighlightWithinTextarea]]))


(defn- skill-checkbox-lists [] 
  (doall
   (let [skills-lists (re-frame/subscribe [::subs/skills-lists]) ] 
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

(def ^:const textbox-size
  {:height 600 :width 450})

(println HighlightWithinTextarea)
(js/console.log HighlightWithinTextarea)


(defn job-description [] 
  [:div
   [:h3 "Paste Job Description Here"]
   [:div {:style textbox-size}
    [:> HighlightWithinTextarea 
     {:value "a
              

Key Responsibilities

Hands-on Blazor and C# REST API development.
Translate user stories into production-quality code.
Participate in architecture discussions; propose design alternatives where it adds value.
Code review for mid-level engineers.
Partner with QA on test strategy for new components.
Identify and surface technical debt or migration risks early.


Required Qualifications

6–9 years of full-stack .NET development experience.
Production experience with Blazor or another modern web framework.
C# REST API development (ASP.NET Core, Web API).
Comfortable reading and reasoning about legacy code (VB6 a plus).
Strong Git/PR-based workflow and code-review discipline.


Nice to Have
Key Responsibilities

Hands-on Blazor and C# REST API development.
Translate user stories into production-quality code.
Participate in architecture discussions; propose design alternatives where it adds value.
Code review for mid-level engineers.
Partner with QA on test strategy for new components.
Identify and surface technical debt or migration risks early.


Required Qualifications

6–9 years of full-stack .NET development experience.
Production experience with Blazor or another modern web framework.
C# REST API development (ASP.NET Core, Web API).
Comfortable reading and reasoning about legacy code (VB6 a plus).
Strong Git/PR-based workflow and code-review discipline.


Nice to Have

Key Responsibilities

Hands-on Blazor and C# REST API development.
Translate user stories into production-quality code.
Participate in architecture discussions; propose design alternatives where it adds value.
Code review for mid-level engineers.
Partner with QA on test strategy for new components.
Identify and surface technical debt or migration risks early.


Required Qualifications

6–9 years of full-stack .NET development experience.
Production experience with Blazor or another modern web framework.
C# REST API development (ASP.NET Core, Web API).
Comfortable reading and reasoning about legacy code (VB6 a plus).
Strong Git/PR-based workflow and code-review discipline.


Nice to Have

Modernization project experience.
Test automation contributions (Selenium / Serenity).
Financial / regulated-industry experience.

"}]]
   #_(let [description (re-frame/subscribe [::subs/job-description])
           editing? (r/atom true)]
       (if @editing?
         [:textarea#job-description-editing
          {:style textbox-size
           :on-change #(->> % .-target .-value (vector ::events/description-update) re-frame/dispatch)
           :on-blur #(do (println "blurred!")
                         (reset! editing? false))}]
         
         [:div#job-description
          {:style textbox-size
           :on-focus #(do (println "focused!")
                          (reset! editing? true))}
          @description]))])

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

  
  