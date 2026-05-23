(ns skill-match.views.components
  (:require
   [reagent.core :as r]

   ["jquery" :as jQuery]
   ;; this expects "jQuery" when imported/initialized
   ["highlight-within-textarea"]))

(def ^:private $ jQuery)

;; obstacles to generalization in separate lib
;; * jQuery (not really, they'll just have deal with it for now)
;; * input applies class names rather than colors directly (maybe this can be fixed?)
;;   * can "jquery up" some <style> tags lol
;; * requires unique dom id per element
;;   * either needs descriptive exceptions or perhaps theres a way to go reagent -> React -> raw dom
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
      (fn [_]  (-> ($ dom-id) (.highlightWithinTextarea "update")))
      
      :component-will-unmount
      (fn [_] (-> ($ dom-id) (.highlightWithinTextarea "destroy")))
      
      :reagent-render
      (fn [] 
        (apply vector :textarea
               (-> props
                   (assoc "data-current-highlighting" (update-highlights @highlights))
                   (dissoc :highlights))
               body))})))