(ns skill-match.functor)

(defmulti fmap (fn [f data] (type data)))

(defmethod fmap (type {})
  [f map]
  (into {} (for [[k v] map] [k (f v)])))

(defmethod fmap (type (hash-map))
  [f map]
  (into {} (for [[k v] map] [k (f v)])))

(defmethod fmap :default [f data] (map f data))
