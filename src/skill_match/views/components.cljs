(ns skill-match.views.components
  (:require
   [reagent.core :as r]

   ["jquery" :as jQuery]
   ;; this expects "jQuery" when imported/initialized
   ["highlight-within-textarea"]))

(def ^:private $ jQuery)


(defn highlightable-textarea [{:keys [highlights] 
                               :or {highlights (atom false)} 
                               :as props} & body]
  (let [current-highlights (r/atom false)
        update-highlights (fn [h] (reset! current-highlights h) (hash h))
        dom-id (str \# (:id props))] ;; todo throw descriptive exception if no id?
    (reagent.core/create-class
     {:component-did-mount
      (fn [this]
        (js/console.log this)
        (-> ($ dom-id) ;; todo throw descriptive exception?
            (.highlightWithinTextarea #js {:highlight #(clj->js @current-highlights)})))
      :component-did-update #(-> ($ dom-id) (.highlightWithinTextarea "update"))
      :reagent-render
      (fn [] 
        [:div 
         [:textarea  
          (assoc props (str "data-" (update-highlights @highlights)) true) 
          body]])})))