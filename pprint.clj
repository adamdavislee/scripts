(def atomic? (complement coll?))
(def padding #(apply str (repeat % " ")))
(def strcat #(->> (apply concat %&) (apply str)))
(defn my-max-key [x] (if (empty? x) [""] (apply (partial max-key count) x)))
(defn longest-key [m] (->> m keys (filter atomic?) (map str) my-max-key))
(def length (comp count str))
(def not-map? (complement map?))
(def nested? #(some coll? %))
(def join-lines (partial clojure.string/join "\n"))
(defn has-atomic? [coll] (some atomic? coll))
(declare convert-items convert)
(defn convert-seq [depth vec] (conj []
                                    (map (partial convert-items (inc depth)) vec)
                                    ""))
(defn string-vertically [depth [key value]]
  (str (convert depth key) "\n"
       (convert (inc depth) "↓") "\n"
       (convert depth value) "\n"))
(defn diff-key-lengths [key1 key2]
  (- (length key1) (length key2)))
(defn string-horizontally [depth longest-key [key value]]
  (str (padding depth) key (padding (diff-key-lengths longest-key key)) " → " value))
(defn convert-kv [depth longest-key [key value]]
  (if (nested? [key value])
    (string-vertically depth [key value])
    (string-horizontally depth longest-key [key value])))
(defn convert-items
  ([depth thing]
   (cond (atomic? thing)
         [(str (padding depth) thing)]

         (not-map? thing)
         (convert-seq depth thing)

         (map? thing)
         (map (partial convert-kv (inc depth) (longest-key thing)) thing)))
  ([thing] (convert-items -1 thing)))
(defn convert
  ([depth thing] (->> thing (convert-items depth) flatten join-lines))
  ([thing] (convert -1 thing)))
(def sample-input [:first {:k :v {:sample :nested :map :test :points :to} {:this :other :nice :map}}])
(->> sample-input convert println)
