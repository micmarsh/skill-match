(ns skill-match.views.components
  (:require
   [reagent.core :as r]

   ["jquery" :as jQuery]
   ;; this expects "jQuery" when imported/initialized
   ["highlight-within-textarea"]))

(def ^:private $ jQuery)


(defn highlightable-textarea [{:keys [highlights]
                               :or {highlights (r/atom true)}
                               :as props} & body]
  (let [current-highlights (r/atom false)
        update-highlights (fn [h] (reset! current-highlights h) (hash h))
        dom-id (str \# (:id props))] ;; todo throw descriptive exception if no id?
    (reagent.core/create-class
     {:component-did-mount
      (fn [_]
        (-> ($ dom-id)
            (.highlightWithinTextarea #js {:highlight #(clj->js @current-highlights)})))
      
      :component-did-update 
      (fn [_] 
        (-> ($ dom-id) (.highlightWithinTextarea "update")))
      
      :reagent-render
      (fn []
        [:div
         (apply vector :textarea
                (-> props
                    (assoc (str "data-" (update-highlights @highlights)) true)
                    (dissoc :highlights))
                body)])})))