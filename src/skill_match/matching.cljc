(ns skill-match.matching 
  (:require
    [clojure.string :as str]))


(def ^:const split-characters
  [\space \& \/ \-])

(def split-regex (->>  split-characters (str/join "|") re-pattern))

(defn- clean [word]
  (str/replace word #"\(|\)|\[|\]|\," ""))

(defn skill->words [skill]
  (set (for [skill-word (str/split skill split-regex)
             :when (not (empty? skill-word))]
         (-> skill-word 
             clean 
             str/lower-case))))

(defn desc-words [description]
  (->> (str/split description  #"\s")
       (mapcat #(str/split % split-regex))
       (remove empty?)
       (map (comp clean str/lower-case))
       (set)))

;; description is raw text
;; indexed-skills is result of skills-index
(defn skill-paths [description indexed-skills]
  (let [desc-words' (desc-words description)]
    (select-keys indexed-skills desc-words')))

(comment
  (require ['skill-match.data :refer :all])

  (def indexed-skillz (into {}
                            (for [[list-name skills] skills-data
                                  [idx skill] (map-indexed #(vector %1 %2) skills)
                                  skill-word (str/split skill split-regex)
                                  :when (not (empty? skill-word))
                                  :let [clean-word (clean skill-word)]]
                              [(str/lower-case clean-word) [list-name idx]])))

  (def test-description "Knowledge/Skills/Abilities

 Strong ability to coach, mentor, and teach others, leading technical aspects for product teams, including architectural and development strategy 
Demonstrates complex decision-making capabilities, with the ability to influence both technical and business stakeholders
Expertise in full-stack architecture, system design, and multiple development languages, demonstrating mastery of syntax, idioms, and toolsets
Able to evaluate and implement best practices in software engineering, including teaching fundamentals
Strong analytical skills in assessing challenges, applying problem-solving techniques, and refining engineering approaches to enhance technical solutions
Promotes collaboration within teams, fostering a culture of technical excellence, while maintaining a learning attitude to continuously improve expertise
Able to constructively critique the code of others based on principles of craftsmanship and leverage conflict resolution as a learning opportunity rather than negotiation
Excellent written and verbal communication skills, ensuring clarity in technical discussions and stakeholder alignment

Education/Experience

Bachelor's degree in computer, engineering or related discipline with four (4) or more years of relevant coding experience
 [OR] Associate's degree in computer, engineering or related discipline with six (6) or more years of relevant coding experience 
 [OR] High School Diploma/GED with degree with eight (8) or more years of relevant coding experience 
Preferred Experience

One or more of the following skills will set you apart:

 Proven expertise in C# and .NET (Core, .NET 5) 
Proven expertise in front-end languages/frameworks (JavaScript, React, Next.js)
Skilled in API Management platforms (e.g., Apigee, Azure API Management, Mulesoft) and API security (OAuth, JWT)
Skilled with testing tools (e.g., Bruno, Postman, Blazemeter, Jmeter, Certify)
Proficiency in Microsoft Azure services (e.g., Azure AD, Data Factory, Logic Apps, Functions)
Skilled in Azure Generative AI services and methods (OpenAI, Cognitive Services, Speech Services, RAG model)
Skilled in Azure DevOps, CLI, PowerShell, and CI/CD
Knowledgeable in cloud architecture (PaaS, SaaS, IaaS, serverless)
Proven expertise in Python and experience with machine learning frameworks 
")

  (drop 100 (desc-words test-description))

  

  (skill-paths test-description indexed-skillz)

;this is some wild stuff!
; algo is done, now what
; integrate into UI, what is UI
;   was thinking about "chaining subscriptions", maybe not relevant
;     uhhhhh... just store indexed version in db? won't change, if ever does can re-calculate then
; list of everything, with checkboxes, checked items at the top
;   can check and uncheck, then also edit text boxes
;   sub data -> super-local reagent components
;   then a button or something to export  
  
  )
