(ns prologger.core)


(def filename "/tmp/smaller_file.txt" )

'I need slurp to trim the file so that it removes the t7.

(slurp filename )

(defn convert
  [my-key my-value]
  ((get conversions my-key) my-value))

(defn parse
  "Convert input into rows of columns"
  [string]
  (map #(clojure.string/split % #"-")
       (clojure.string/split string #"\r\n")))


(def my-keys [:date :name :length])

(str->date "2016/7/6")

(defn str->date
  [str]
  (#(.parse
     (java.text.SimpleDateFormat. "yyyy,MM,dd,hh,mm,ss") %)
    (clojure.string/replace str #"t7\(" "" )))


(defn str->int
  [str]
  (Integer. str))

(parse  (slurp filename))


'(replace (parse (slurp filename)))


(def conversions {:date str->date
                  :name identity
                  :length str->int
                  })




;Create a filter that grabs only the queues
(defn t7-filter
  [rows]
  (filter #(clojure.string/includes? % "t7(") rows ) )



(map t7-filter (parse (slurp filename)))

;A nother comment to check where this is saving -l

(defn mapify
    "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
    [rows]
    (map (fn [unmapped-row]
           (reduce (fn [row-map [my-key my-value]]
                     (assoc row-map my-key (convert my-key my-value)))
                   {}
                   (map vector my-keys unmapped-row)))
         (t7-filter rows)))

(parse (slurp filename))

(mapify (parse (slurp filename)))

(defn queue-filter
  [minimum-count records]
  (filter #(>= (:length %) minimum-count) records))

(queue-filter 10 (mapify (parse (slurp filename))))

(builder)