(ns clojure-starter.dictionary
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

;; TODO if-let when-let

;; =====================NUMBERS======================
;; rational
(reduce + 0 (list 1/2 2/4))

;; module MATH

;; =====================STRINGS======================
;; str
(str "I can eat " 10 " sushi")

;; TODO see clojure.string

;; =====================MAPS======================
(def personal-taste {:california-roll true
                     :raw-squid       false})
;; get
(get personal-taste :california-roll)
(:california-roll personal-taste)
;; seq
(seq personal-taste)
;; nested maps
(def nested {:memes  true
             :nested {:ok    "boomer"
                      :bitch "please"}})
;; get-in
(get-in nested (list :nested :ok))

assoc

assoc-in

update
update-in

merge

zipmap

keys
vals

conj
into

;; à implementer: deep-merge

;; =====================VECTORS=======================
(def fish-vector ["blowfish" "lowfish" "owfish"])
;; get
(get fish-vector 1)
;; map
(map clojure.string/upper-case fish-vector)

assoc
update
assoc-in
update-in
conj
into
get-in
peek
pop

;; =====================SYMBOLS=====================

;; TODO une section pour backquote avec plus d'exemple

;; quote backquote unquote unquote-splice
(def expr-with-quotes (let [y '+
                            z (range 10)]
                           `(~y ~@z)))
(eval expr-with-quotes)

;; =====================SETS=====================
;; TODO voir clojure.set
;; litteral
#{1 2}
;; ops
(hash-set 1 1 1 2)
(conj #{:fish :pirate} :fish)
;; contains
(contains? #{:fish :pirate} :fish)

;; =====================ATOMS=====================
(comment "Atoms are for Uncoordinated synchronous access to a single Identity.")
;; atom
(def fish-atom (atom {}))
;; swap! & deref
(swap! fish-atom assoc :blowfish 1)
(println @fish-atom)
(swap! fish-atom assoc :blowfish (inc (:blowfish @fish-atom)))

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

;; =====================VARS=====================
;; dynamic vars

;; =====================LISTS=====================
cons
concat
first rest next

;; ---------cons nth conj ----------

;; =====================CORE FUNCTIONS=====================
;; TODO bouger la plupart dans une partie "Sequences"
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
(map
  (fn [hello]
      (clojure.string/capitalize (apply str hello)))
  (repeatedly 10
              (partial shuffle (list "de vous dire bonjour, " "le temps, " "est venu, "))))
;; partial
(def jesus-multiply-fish (partial * 100))
(jesus-multiply-fish 10)
;; complement (1 - probability)
(def not-empty? (complement empty?))
(not-empty? [])
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

;; =====================MACROS=====================

;; TODO  reimplementer: or, and,  when, when-not, if-let, when-let, cond
;; essayer d'implementer cond-let
;; implementer 'let avec uniquement 'fn (sans destructuration)

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

;; =====================CORE/ASYNC================
;; --------- >! <! >!! <!! go go-loop chan buffer close! thread alts! alts!! timeout----------

(def number-chan (chan 100))
(def sum (atom 0))

(dotimes [_ 100]
         (go
           (loop []
                 (let [number (<! number-chan)]
                      (swap! sum + number))
                 (recur))))

(go
  (doseq [x (range 1000000)]
         (>! number-chan x)))

;; =====================LAZY-SEQ=====================
;; =====================DEALING WITH LIBS=====================
;; =====================KEYWORDS=====================
;; =====================FUNCTIONS=====================
partial
complement
iterate
juxt
comp
;; ---------Multiple arrity----------
;; =====================LET=====================
;; =====================LAMBDA=====================
;; =====================LOOPS=====================
;; =====================REGEX=====================
;; =====================REDUCERS=====================
;; ==================INTEROP========================