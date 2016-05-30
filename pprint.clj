(def atomic? (complement coll?))
(def padding #(apply str (repeat % " ")))
(def tabulate #(apply str (repeat % "\t")))
(def strcat #(->> (apply concat %&) (apply str)))
(defn my-max-key [x] (if (empty? x) [""] (apply (partial max-key count) x)))
(defn longest-key [m] (->> m keys (filter atomic?) (map str) my-max-key))
(def length (comp count str))
(def not-map? (complement map?))
(def nested? #(some coll? %))
(def join-lines (partial clojure.string/join "\n"))
(defn has-atomic? [coll] (some atomic? coll))
(defn diff-key-lengths [key1 key2] (- (length key1) (length key2)))
(defn convert
  ([thing] (convert -1 thing))
  ([depth thing]
   (defn convert-items []
     (defn convert-seq []
       (conj []
             (map (partial convert (inc depth)) thing)
             ""))
     (defn string-horizontally [[key value]]
       (str (tabulate (inc depth))
            key
            (padding (diff-key-lengths (longest-key thing) key))
            " → "
            value))
     (defn string-vertically [[key value]]
       (str (convert (inc depth) key) "\n"
            (convert (+ 2 depth) "↓") "\n"
            (convert (inc depth) value) "\n"))
     (defn convert-kv [[key value]]
       (if (nested? [key value])
           (string-vertically [key value])
           (string-horizontally [key value])))
     (cond (atomic? thing)
           [(str (tabulate depth) thing)]

           (not-map? thing)
           (convert-seq)

           (map? thing)
           (map convert-kv thing)))
   (->>  (convert-items) flatten join-lines)))

(def sample-input [["the first thing in this nested vector"]
                   {{"this is a key in a nested map"
                    "that points to me!!!"}
                   {"and that entire map points to this map!!!"
                    "cool!!!"
                    "but it gets cooler cause..."
                    "the value's line up!!!"}}])
(->> sample-input convert println)
