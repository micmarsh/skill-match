(ns skill-match.events
  (:require
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]
   [skill-match.db :as db]
   [skill-match.functor :refer [fmap]]
   [skill-match.matching :as matching]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _] db/default-db))

(defn- skills-matches-desc [desc-words]
  (fn [skill]
    (->> skill 
         matching/skill->words 
         (some desc-words))))

(defn- set-description [description db]
  (let [desc-words (matching/desc-words description)]
     (assoc db
            :job-description description
            :description-words desc-words
            :current-selections (fmap 
                                 (fn [specific-skills]
                                   (->> specific-skills
                                        (map :id)
                                        (filter (skills-matches-desc desc-words))
                                        (set)))
                                 (:skills db)))))

(re-frame/reg-event-db
 ::description-update
 (fn-traced
   [db [_, description]]
   (set-description description db)))

(re-frame/reg-event-db
 ::update-selection
 (fn-traced
  [db [_ list-title selection]]
  (assoc-in db [:current-selections list-title] (set selection))))

(re-frame/reg-event-fx
 ::render-resume
 (fn-traced [{:keys [db]}]
   {:http-xhrio {:method :post
                 ;; todo somehow manage this!
                 :url "localhost:8000/render_resume" 
                 :format (ajax/json-request-format)
                 :response-format {:content-type "application/pdf"}
                 :params (:current-selections db)
                 :on-success [::rendered-resume]
                 :on-failure [::render-resume-fail]}}))

;; strictly speaking this should be reg-fx in effects.cljs
(re-frame/reg-event-fx
 ::render-resume
 (fn [arg1 arg2 arg3]
   (js/console.log arg1 arg2 arg3)
   {}))

(re-frame/reg-event-fx
 ::render-resume-fail
 (fn [arg1 arg2]
   (js/console.error arg1 arg2)
   {}))
