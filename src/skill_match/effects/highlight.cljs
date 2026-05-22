(ns skill-match.effects.highlight
  (:require
   [re-frame.core :as re-frame]
   [skill-match.matching :as matching]  
   
   ["jquery" :as jQuery]
   ;; this expects "jQuery" when imported/initialized
   ["highlight-within-textarea"]))
(def $ jQuery)

(def ^:const ai-buzzwords #{"ai" "llm" "llms" "agentic" "agents" "claude" "copilot"})

(defn- highlight-map [word] 
  {:className (if (ai-buzzwords word) "red" "green")
   :highlight word})

(re-frame/reg-fx
 ::selections
 (fn [[{:keys [current-selections description-words]} dom-id]]
   (-> ($ (str \# dom-id))
       ;; maybe calling too much (when typing) and causing focus to be lost on textarea?
       ;;    maybe is not that big a deal given use cases?
       ;; definitely some problems positioning, fixed by maybe minor resizes?
       (.highlightWithinTextarea
        (clj->js {:highlight
                  (->> (vals current-selections)
                       (apply concat)
                       (mapcat matching/skill->words)
                       (set)
                       (concat (filter ai-buzzwords description-words))
                       (map highlight-map))})))))