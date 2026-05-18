(ns skill-match.subs
  (:require
   [re-frame.core :as re-frame]
   [skill-match.matching :as matching]))

(re-frame/reg-sub
 ::test-job-desc
 (fn [db]
   (:job-description db)))

(re-frame/reg-sub
 ::matched-skills
 (fn [{:keys [job-description indexed-skills]} _]
   (seq (matching/skill-paths job-description indexed-skills))))

(re-frame/reg-sub
 ::description-words
 (fn [db _](-> db :job-description matching/desc-words)))

(re-frame/reg-sub
 ::skills-lists
 (fn [db _] (:skills db)))