(ns test-clojure.Dictionnary
    (:require [clojure.core.async
               :as a
               :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]]))

(comment "THEME : fish")

;; =====================CONTROL FLOW======================
(def there-are-fish true)
(def im-hungry true)
(def there-are-pirates false)
;; if
(if there-are-fish
  (println "We're lucky")
  (println "We're not lucky"))
;; do
(do
  (println "print this")
  (println "then print that"))
;; when
(when there-are-fish
      (println "blob blob"))
;; or
(or there-are-pirates there-are-fish)
;; and
(and there-are-pirates there-are-fish)
;; if-let
(defn should-i-go-swimming [pirate-presence]
      (if-let [x pirate-presence]
              "Hell no !"
              "No pirates, all good"))
(should-i-go-swimming there-are-pirates)
;; when-let
(defn speak-loudly [pirate-presence]
      (when-let [presence (not pirate-presence)]
                (println "Blablabla")
                (println presence)))

(speak-loudly there-are-pirates)

;; =====================NUMBERS======================
;; rational
(reduce + 0 (list 1/2 2/4))
;; module MATH
(Math/pow 2 5)
(Math/max 2 5)

;; =====================STRINGS======================
;; str
(require '[clojure.string :as str])
(str "I can eat " 10 " sushi")
;; blank?
(str/capitalize "hello")
(str/ends-with? "hello" "lo")
(str/escape "test" {\e "oa"})
(str/includes? "hello" "hell")
(str/index-of "hello" "e")
(str/join ", " ["hello" "world"])
(str/last-index-of "hello" "l")
(str/lower-case "HELLO")
(str/replace "I like spinach because spinach are healthy" #"spinach" "sushi")
(str/replace-first "I like spinach because spinach are healthy" #"spinach" "sushi")
(str/reverse "hello")
(str/split "hello world test" #" ")
(str/split-lines "hello world \n test")
(str/starts-with? "hello" "hel")
(str/trim "       hola  ")
(str/trim-newline "test\n\r")
(str/triml "       hola  ")
(str/trimr "       hola  ")
(str/upper-case "hello")

;; =====================MAPS======================
(def personal-taste {:california-roll true
                     :raw-squid       false
                     :lower-case-fish "fish"})
;; get
(get personal-taste :california-roll)
(:california-roll personal-taste)
;; seq -> boolean
(seq personal-taste)
;; nested maps
(def nested {:memes  true
             :nested {:ok    "boomer"
                      :bitch "please"}})
;; get-in
(get-in nested (list :nested :ok))
;; assoc -> map
(assoc personal-taste :sushi true)
;; assoc-in
(assoc-in nested [:nested :new-key] "value")
;; update
(update personal-taste :lower-case-fish #(clojure.string/capitalize %))
(update personal-taste :lower-case-fish clojure.string/capitalize)
(= (update {:a 1} :a - 2 3)
   (update {:a 1} :a #(- % 2 3)))
;; update-in
(update-in nested [:nested :ok] #(clojure.string/capitalize %))
;; merge
(merge {:a "blowfish" :b "rat"} {:b "clown-fish"})
;; zipmap
(zipmap [:a :b :c] ["blowfish" "catfish" "tuna"])
;; keys
(keys (zipmap [:a :b :c] ["blowfish" "catfish" "tuna"]))
;; vals
(vals (zipmap [:a :b :c] ["blowfish" "catfish" "tuna"]))
;; conj
(conj {:a "blowfish" :b "tuna"} {:c "catfish"})
(conj {1 2 3 4} [5 6])
;; into
(into (hash-map) [[:a "blowfish"] [:c "blowfish"] [:b "blowfish"]])
;; à implementer: deep-merge
(assert (= (merge-with + {:a 1} {:a 2})
           {:a 3}))

(merge-with conj {:a {:b 1}} {:a {:c 1}})

(defn deep-merge [m n]
      (merge-with conj m n))

(deep-merge {:a "blowfish"
             :b {:a "rat"
                 :b {:c 1}}}
            {:b {:b {:c "erased"}}})

;; =====================VECTORS=======================
(def fish-vector ["blowfish" "lowfish" "owfish"])
;; get
(get fish-vector 1)
;; map
(map clojure.string/upper-case fish-vector)
;; assoc
(assoc [:blob :bloub] 2 :blib)
;; update
(update ["bloub" "blib"] 0 str/upper-case)
;; assoc-in
(assoc-in [[1 0 0]
           [0 1 0]
           [0 0 1]] [1 1] 2)
;; update-in
(update-in [[1 0 0]
            [0 1 0]
            [0 0 1]] [1 1] inc)
;; conj
(conj [:blob :blib] :bloub)
;; into
(into [:blob :blib] (list :bloub :blips))
;; get-in
(get-in [[1 0 0]
         [0 :blob 0]
         [0 0 1]] [1 1])
;; peek
(time (last [1 2 3 4]))
(time (peek [1 2 3 4]))
;; pop
(pop [1 2 3 4])

;; =====================LISTS=====================
;; cons
(cons 1 (cons 2 (list 3 4)))
;; concat
(concat (list 1 2) (list 3 4))
;; first
(first (range 10))
;; rest
(rest (range 10))
(rest ())
;; next
(next (range 10))
(next ())
;; nth
(nth (list 1 7 9) 1)
;; conj
(conj (list 1 2 3) 0)

;; =====================SETS=====================

(require '[clojure.set :as set])

;; litteral
#{1 2}
;; ops
(hash-set 1 1 1 2)
(conj #{:fish :pirate} :fish)
;; contains
(contains? #{:fish :pirate} :fish)

;; difference
(set/difference #{:fish :pirate} #{:blowfish :pirate})
;; index
(def weights #{{:name "fish" :weight 1000}
               {:name "catfish" :weight 800}
               {:name "blowfish" :weight 1000}})

(set/index weights [:weight])
;; intersection
(set/intersection #{:fish :pirate} #{:blowfish :pirate})
;; join
(set/join #{{:a "tuna"} {:a "dorade"}} #{{:b 'salt} {:b 'pepper}})
;; map-invert
(set/map-invert {:a 1, :b 2})
;; project // projection select in sql
(set/project #{{:name "tuna" :id 33} {:name "blowfish" :id 34}} [:name])
;; rename
(set/rename #{{:name "test", :b 1} {:name "chose", :b 2}} {:name :NaMe})
;; rename-keys
(set/rename-keys {:a 1, :b 2} {:a :new-a, :b :new-b})
;; select
(set/select odd? (into #{} (range 10)))
;; subset?
(set/subset? #{2 3} #{1 2 3 4})
;; superset?
(set/superset? #{1 2 3 4} #{2 3})
;; union
(set/union #{1 2} #{2 3} #{3 4})


;; =====================BACKQUOTE=====================
;; quote
;; backquote
;; unquote
;; unquote-splice
(def expr-with-quotes (let [y '+
                            z (range 10)]
                           `(~y ~@z)))
(eval expr-with-quotes)

;; =====================ATOMS=====================
(comment "Atoms are for Uncoordinated synchronous access to a single Identity.")
;; atom
(def fish-atom (atom {}))
;; swap! & deref
(swap! fish-atom assoc :blowfish 1)
(println @fish-atom)
(swap! fish-atom assoc :blowfish (inc (:blowfish @fish-atom)))

(swap! (atom 1) - 2 3)
(swap! (atom 1) #(- % 2 3))

(assert {:a -4}
        (swap! (atom {:a 1}) update :a - 5))

;; =====================REFS=====================
(comment "Refs are for Coordinated Synchronous access to Many Identities.")
;; transaction
(def me (ref {:coins 2 :sushi 0}))
(def sushi-master (ref {:coins 0 :sushi 2}))

(defn trade [client server]
      (dosync
        (when (< 0 (:coins @me))
              (alter me assoc :coins (dec (:coins @me)))
              (alter me assoc :sushi (inc (:sushi @me)))
              (alter sushi-master assoc :coins (inc (:coins @sushi-master)))
              (alter sushi-master assoc :sushi (dec (:sushi @sushi-master))))))

(trade me sushi-master)
(println @me)
(println @sushi-master)

;; =====================FUNCTIONS ON SEQUENCES=====================
;; map
(map inc (range 3))
;; reduce
(reduce + 0 (range 3))
;; into
(into (sorted-map) [[:a "fish"] [:c "pirate"] [:b "sushi"]])
;; conj
(conj personal-taste nested)
;; concat
(concat [1 2] [3 4])
;; some
(some odd? (range 10))
;; filter
(filter odd? (range 10))
;; take
(take 5 (range))
;; drop
(drop 5 (range 10))
;; sort
(sort [2 8 9 7 0])
;; sort-by
(sort-by :fish [{:fish 2} {:fish 3} {:fish 1}])
;; identity
(identity [1 1 2 3 3 1 1 5 5])
;; apply
(require '[clojure.string :as str])
(map
  (fn [hello]
      (clojure.string/capitalize (str/join ", " hello)))
  (repeatedly 10
              (fn [] (shuffle (list "de dire bonjour" "le temps" "est venu")))))

(letfn [(shuffle-func [list-words] (shuffle list-words))
        (repeat-time [num wl] (repeatedly num (partial shuffle-func wl)))
        (cap-and-join [word-seq] (clojure.string/capitalize (str/join ", " word-seq)))]
       (map cap-and-join (repeat-time 10 (list "de dire bonjour" "le temps" "est venu"))))

;; cons
(cons 1 (cons 2 nil))
;; rest
(rest (range 3))
;; first
(first (range 3))
;; take-while
(take-while even? [2 4 6 8 9 2 6])
;; drop-while
(drop-while even? [2 4 6 8 9 2 6])
;; concat
(concat [1 2 3] [4 5 6])
;; repeat
(repeat 10 "hello")
;; repeatedly
(repeatedly 5 #(rand-int 11))

;; =====================FUNCTIONS=====================
;; partial
(def jesus-multiply-fish (partial * 100 2))
(jesus-multiply-fish 10)

;; complement
;; complement -> (1 - probability)
(def not-empty? (complement empty?))
(not-empty? [])


;; iterate
(defn is-prime
      ([number] (is-prime number 2))
      ([number index] (cond
                        (<= number 1) nil
                        (= index number) number
                        (= 0 (mod number index)) nil
                        :else (is-prime number (inc index)))))

(filter #(not (= nil %)) (map is-prime (range 100)))

(take 10 (iterate inc 0))

;; juxt
((juxt first count) (range 10))

;; comp
(def count-if (comp count filter))
(count-if is-prime (range 100))

;; =====================MACROS=====================

;; essayer d'implementer cond-let
;; implementer 'let avec uniquement 'fn (sans destructuration)

;; -----------------------------------------------

(and (pos? -1) (= 1 2))

(defmacro custom-and [cond1 cond2]
          `(if ~cond1
             ~cond2
             false))

(and (pos? -1) (+ 1 2) (+ 3 2))
(custom-and (pos? 1) (+ 1 2))

;; -----------------------------------------------
(comment :tests (macroexpand (quote (or (pos? 1) (pos? -1) (pos? 1))))
         (macroexpand (quote (or (pos? 1))))
         (not 1)

         (let* [or__5516__auto__ (pos? 1)]
           (if or__5516__auto__ or__5516__auto__
                                (clojure.core/or (pos? -1) (pos? 1))))

         (defmacro custom-or-1 [cond1 &conds]
                   `(let [current ~cond1]
                         (if ~current
                           ~current
                           (custom-or-1 ~@&conds))))


         (defmacro custom-or-2 [cond1 & conds]
                   (let [current cond1]
                        `(cond
                           ~current ~current
                           (not ~current)
                           :else (custom-or ~@conds))))

         (defn arities? ([] 0) ([x] 1) ([x & other] "more than one"))
         (arities? :hello :world))

(defmacro custom-or
          ([] nil)
          ([single] single)
          ([cond1 & others] `(if ~cond1 ~cond1 (custom-or ~@others))))

(custom-or (pos? -1) (pos? -1))

;; -----------------------------------------------
(defmacro custom-when [cond & actions]
          `(if ~cond (do ~@actions))
          )

(custom-when (pos? 1) (println "truc") (println "truc"))

;; -----------------------------------------------
(defmacro custom-when-not [cond & actions]
          `(if (not ~cond) (do ~@actions))
          )

(custom-when-not (pos? -1) (println "truc") (println "truc"))
;; -----------------------------------------------

(defmacro custom-if-let [[id test] then else]
          (let [tmp test]
               `(if ~tmp
                  (let [~id ~tmp] ~then)
                  ~else)))

(defn custom-if-let-demo [arg]
      (custom-if-let [x arg] (println x) (println "else")))

(custom-if-let-demo :hello)
;; -----------------------------------------------

(defmacro custom-when-let [[id test] & actions]
          (let [tmp test]
               `(if ~tmp
                  (let [~id ~tmp] ~@actions))))

(custom-when-let [x true] (println x) (println x))
;; -----------------------------------------------
(macroexpand (quote (cond
                      (pos? -1) (println "truc")
                      (pos? -2) (println "chose")
                      (pos? -3) (println "chose"))))

(cond
  (pos? -1) (println "truc")
  (pos? -2) (println "chose")
  (pos? -3) (println "chose"))


(defmacro custom-cond
          ([] nil)
          ([test action & others] `(if ~test ~action (custom-cond ~@others))))

(custom-cond
  (pos? 1) :chose
  (pos? 2) (println "chose"))

;; -----------------------------------------------

(defmacro infix-and-turn-plus-to-minus [operation]
          (let [[op1 fun op2] operation]
               (cond
                 (= fun '+) `(- ~op1 ~op2)
                 :else `(~fun ~op1 ~op2))))

(macroexpand '(infix-and-turn-plus-to-minus (1 + 1)))
(infix-and-turn-plus-to-minus (1 * 1))

;; =====================FUTURES===================
(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
     (println "The result is: " @result))

;; =====================DELAY=====================
(def my-delay
  (delay (let [message (+ 1 1)]
              (println "First time computing:" message)
              message)))

(force my-delay)

;; =====================PROMISES==================

(def my-promise (promise))
(deliver my-promise (+ 1 2))
(println @my-promise)

;; =====================VARS=====================
(comment "Vars are for thread local isolated identities with a shared default value.")

;; dynamic vars
(def ^:dynamic *notification-address* "dobby@elf.org")

(defn notify
      [message]
      (str "TO: " *notification-address* "\n"
           "MESSAGE: " message))

(binding [*notification-address* "test@elf.org"]
         (notify "test!"))
(notify "hello")


;; =====================LOOPS=====================
(loop [i 0]
      (when (< i 5)
            (println i)
            (recur (inc i))))

;; =====================TRANSDUCERS===============
;; =====================LAZY-SEQ==================


