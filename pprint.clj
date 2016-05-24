(require '[clojure.string :as str])
(def atomic? (complement coll?))
(declare convert-kv)
(def padding #(apply str (repeat % " ")))
(def strcat #(->> (apply concat %&) (apply str)))
(defn longest-key [m]
  (->> m keys (filter atomic?) (map str) (my-max-key count)))
(def length (comp count str))
(def not-map? (complement map?))
(def nested? #(some coll? %))
(def join-lines (partial str/join "\n"))
(defn my-max-key
  ([one-arg] "")
  ([pred coll] (apply max-key pred coll)))
(defn convert-vec [depth vec]
  (conj []
        (map (partial convert-items (inc depth)) vec)
        "")) ;;the string will become a newline, because convert calls join-lines
(defn convert-items
  ([thing] (convert-items -1 thing))
  ([depth thing]
   (cond (atomic? thing)
         (str (padding depth) thing)

         (not-map? thing)
         (convert-vec depth thing)

         (map? thing)
         (map (partial convert-kv (inc depth) (longest-key thing)) thing))))
(defn convert
  ([depth thing]
   (let [converted (convert-items depth thing)]
     (if (coll? converted)
       (->> converted flatten join-lines)
       converted)))
  ([thing] (convert -1 thing)))
(defn string-vertically [depth [key value]]
  (str (padding depth) (convert key) "\n"
       (padding depth) "↓" "\n"
       (padding depth) (convert value) "\n"))
(defn string-horizontally [depth longest-key [key value]]
  (let [value-align-padding (- (length longest-key) (length key))]
    (str (padding depth) key " → " (padding value-align-padding) value)))
(defn convert-kv [depth longest-key [key value]]
  (if (nested? [key value])
    (string-vertically (inc depth) [key value])
    (string-horizontally depth longest-key [key value])))
(->> "not-pprinted.clj" slurp read-string convert (spit "pprinted"))
(max-key count (map str '()))
