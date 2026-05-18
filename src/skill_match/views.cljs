(ns skill-match.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [skill-match.subs :as subs]
   [skill-match.events :as events]
   [re-com.core :refer [selection-list]]
   [clojure.string :as str]
   [skill-match.matching :as matching]))

(defn when-enter 
  "Probably not the best way to manage this, but go with it for now"
  [act]
  (fn [react-e]
    (js/console.log react-e)
    (let [e react-e #_(.-nativeEvent react-e)]
      (when (= (.-code e) "Enter")
        (.preventDefault e)
        (act)))))



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
    (skill-checkbox-lists)]])


(comment 
  (def sample-desc-words #{"ability"
                           "capabilities"
                           "more"
                           "within"
                           "implement"
                           "critique"
                           "development"
                           "degree"
                           "skills"
                           "architecture"
                           "solving"
                           "leverage"
                           "craftsmanship"
                           "six"
                           "excellent"
                           "negotiation"
                           "challenges"
                           "techniques"
                           "of"
                           "4"
                           "high"
                           "education"
                           "learning"
                           "8"
                           "culture"
                           "enhance"
                           "stakeholders"
                           "influence"
                           "assessing"
                           "practices"
                           "teams"
                           "verbal"
                           "approaches"
                           "experience"
                           "design"
                           "associate's"
                           "alignment"
                           "related"
                           "ensuring"
                           "than"
                           "Python"
                           "software"
                           "idioms"
                           "others"
                           "refining"
                           "solutions"
                           "technical"
                           "computer"
                           "constructively"
                           "business"
                           "continuously"
                           "coding"
                           "evaluate"
                           "both"
                           "stakeholder"
                           "improve"
                           "full"
                           "discussions"
                           "maintaining"
                           "conflict"
                           "or"
                           "diploma"
                           "fundamentals"
                           "based"})
  (def sample-list [{:id "Java", :label "Java"}
                    {:id "C#/.NET", :label "C#/.NET"}
                    {:id "Git", :label "Git"}
                    {:id "JUnit Testing Framework", :label "JUnit Testing Framework"}
                    {:id "Kubernetes & Docker", :label "Kubernetes & Docker"}
                    {:id "JSON", :label "JSON"}
                    {:id "XML", :label "XML"}
                    {:id "gRPC/ProtoBuf", :label "gRPC/ProtoBuf"}
                    {:id "Kafka", :label "Kafka"}
                    {:id "JetBrains IntelliJ", :label "JetBrains IntelliJ"}
                    {:id "JetBrains Rider", :label "JetBrains Rider"}
                    {:id "SQL", :label "SQL"}
                    {:id "Microsoft SQL Server (TSQL)", :label "Microsoft SQL Server (TSQL)"}
                    {:id "MongoDB", :label "MongoDB"}
                    {:id "GitHub", :label "GitHub"}
                    {:id "Azure DevOps", :label "Azure DevOps"}
                    {:id "Linux", :label "Linux"}
                    {:id "PostgreSQL", :label "PostgreSQL"}
                    {:id "PostGIS", :label "PostGIS"}
                    {:id "React", :label "React"}
                    {:id "React.js", :label "React.js"}
                    {:id ".NET MVC", :label ".NET MVC"}
                    {:id ".NET Core", :label ".NET Core"}
                    {:id "LINQ", :label "LINQ"}
                    {:id "Visual Studio", :label "Visual Studio"}
                    {:id "Python", :label "Python"}])
  
  (filter (comp sample-desc-words str/lower-case :id) sample-list))
  
  