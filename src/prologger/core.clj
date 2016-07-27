(ns prologger.core)

(use '(incanter core stats charts io))

(def filename "/Users/stuartvdg/Downloads/queue_2016_07_15-19/iress_only.txt")


(slurp filename)

(def my-keys [:date :name :length])

(defn str->date
  [str]
  (#(.parse
     (java.text.SimpleDateFormat. "yyyy,MM,dd,hh,mm,ss") %)
    (clojure.string/replace str #"t7\(" "")))


(defn str->int
  [str]
  (Integer. str))

(def conversions {:date   str->date
                  :name   identity
                  :length str->int
                  })

(defn convert
  [my-key my-value]
  ((get conversions my-key) my-value))

(defn parse
  "Convert input into rows of columns"
  [string]
  (map #(clojure.string/split % #"-")
       (clojure.string/split string #"\r\n")))




(parse (slurp filename))

;Create a filter that grabs only the queues
(defn t7-filter
       [rows]
      (filter #(clojure.string/includes? % "t7(") rows ) )



(defn mapify
  "Return a seq of maps like {:queue \"sms\" :date 10}"
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
  "Filter the records provided by ensuring that the queue count logged is greater than the minimum-count provided"
  [minimum-count records]
  (filter #(>= (:length %) minimum-count) records))


(defn date-details
  [date records]
  (filter #(= (:date %) date) records))

(defn queue-details
  [queue-name records]
  (filter #(= (:name %) queue-name) records))

(queue-details "sms_queue"
               (mapify (parse (slurp filename))))

;Find out al of the queues.
(into {} (:name  (mapify (parse (slurp filename)))))

;this gives us the total lenght for a given queue.
(reduce + (map :length (queue-details "sms_queue" (mapify (parse (slurp filename))))))

;This will produce a histogram of the queue length for a specific queue when measured
(view (histogram
        (filter #(> % 200) (map :length (queue-details "iress_intraday_portfolio_cash_create_processor_queue" (mapify (parse (slurp filename))))))))
;This isnt quite working.. im
;Need to combine


;This will produce the date we can somply use.
 (map #(.format (java.text.SimpleDateFormat. "MM/dd/yyyy HH:MM") % )
  (map :date (queue-filter 10 (mapify (parse (slurp filename)))))
   )



