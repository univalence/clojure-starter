(ns clojure_starter.core)

;; expression de bases

(+ 1 2)

;; l'operation est placée en premier (ici +) suivi des arguments (ici 1 et 2)

;; definir une variable globale

(def foo 42)

;; l'utiliser

(+ foo 1)

;; definir une fonction

(defn mult ;; le nom de la fonction
  [a b] ;; les arguments
  (* a b)) ;; le retour de la fonction

(mult 2 2)
;; => 4

;; les datastructures

;; la map (dictionaire, object)

;; notation litérale

{:a 1
 :b "hello"}

;; stockons la dans une variable

(def my-map
  {:a 1
   :b "iop"})

;; rechercher une valeur à l'intérieur d'une map
(get my-map :a)

;; syntax sugar
(:a my-map)

(assoc my-map :c 90)

(get-in {:a {:b 1}} [:a :b])

(update-in {:a {:b 1}} [:a :b] inc)

;; lists

(def my-vec [1 2 3])
(def my-list (list 1 2 3))

(map inc my-list)

(reduce + 0 my-list)

;; sets

(def my-set #{:foo :bar :baz})

(my-set :io)
(my-set :foo)

;; binding-locaux

(defn mean [xs]
  (let [count-xs (count xs)
        sum (reduce + 0 xs)]
    (/ sum count-xs)))

(mean (range 10))

(let [a 1
      b (inc a)
      a 56]
  (+ a b))

(range 10)


(1 2 +)

'(postfix
   (2 3 (9 8 *) +))

'(+ 2 3 (* 9 8))

(cons 1 (cons 2 (cons 3 nil)))

(defn postfix->prefix [x]

  (if (list? x)

    (let [last-x (last x)
          args (butlast x)]
      (cons last-x (map postfix->prefix args)))

    x))

(postfix->prefix '(2 3 (9 (9 8 7 -) *) +))




'(+ 2 3 (* 9 (- 9 8 7)))


(defmacro postfix [x]
  (postfix->prefix x))

(postfix
  (2 3 (9 (9 8 7 -) *) +))

