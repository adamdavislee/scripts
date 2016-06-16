(ns user)
(def n-doors #(cons :car (repeat % :goat)))
(n-doors 3)
(def situation (shuffle [:goat :goat :car :goat]))
(defn guess-once [situation] (rand-nth situation))
(defn keep [f & colls] (remove nil? (apply (partial map f) colls)))
(defn guess-twice [situation]
  (let
    [first-guess-index (rand-int (count situation))
     get-possible-goat-indexes (keep (fn [item index]
                                         (if (and (= item :goat) (not= first-guess-index index))
                                           index))
                                     situation
                                     (range))
     randomly-removed-goat-index (rand-nth get-possible-goat-indexes)
     second-guess-index (rand-nth (remove #{first-guess-index randomly-removed-goat-index} (range (count situation))))]
    (situation second-guess-index)))
(defn try-method [situations method]
  (->> situations
       (filter #(= (method %) :car))
       count))
(defn compare [situations]
              (str "guessing once wins a car " (try-method situations guess-once) " times."
                   "While guessing twice wins a car " (try-method situations guess-twice) " times."))
(compare (take 100 (repeatedly #(identity situation))))

(def exp #(apply * (repeat %2 %1)))
(exp 2 3)
