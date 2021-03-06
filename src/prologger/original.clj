(ns prologger.core)


(def filename "/tmp/cleaned_it.txt" )

(slurp filename )

(defn parse
  "Convert input into rows of columns"
  [string]
  (map #(clojure.string/split % #"-")
       (clojure.string/split string #"\r\n")))


(def my-keys [:name :length])


(defn str->int
  [str]
  (Integer. str))

(parse (slurp filename))


(replace (parse (slurp filename)))


(def conversions {:name identity
                  :length str->int
                   })





(defn mapit
  [rows]
  (#(assoc {} (keyword (first %) ) (Integer. (second %)))
    (clojure.string/split rows #"-")
      )

(defn convert
  [my-key my-value]
  ((get conversions my-key) my-value))


(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [my-key my-value]]
                   (assoc row-map my-key (convert my-key my-value)))
                 {}
                 (map vector my-keys unmapped-row)))
       rows))



(first (mapify (parse (slurp filename))))

