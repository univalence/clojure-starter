(ns clojure-starter.cons
  (:refer-clojure :exclude [cons list atom? take])
  (:require [clojure.core :as c]))

(do :init

    (defonce STATE (atom {:todos []
                          :vars #{}}))

    (doseq [v (:vars @STATE)]
      (ns-unmap *ns* v))

    (do @STATE))

(ns-publics *ns*)

(do :help

    (defn deep-contains?
      [this x]
      (if (coll? this)
        (some #(deep-contains? % x) this)
        (= this x)))

    (defmacro be [x y]
      (swap! STATE update :vars conj x)
      `(def ~x ~y))

    (defmacro is [& xs]

      ;; TODO add file location infromations along todos and failures

      ;; ensure that the expression does not contains TODOs
      (if (deep-contains? xs '?)
        (and (swap! STATE update :todos conj (c/cons 'is xs)) nil)
        `(if-let [x# ~(first xs)]
           (assert (= x# ~@(rest xs))
                   (str "not equal:\n" ~@(interpose "\n" xs)))
           (error (str (pr-str ~(first xs)) " is not")))))

    (defmacro isnt
      ([x] (or (and (symbol? x) (not (resolve x)))
               `(assert (not ~x) (str ~x " is but should not be"))))
      ([x & xs] `(assert (not= ~x ~@xs) (str "should not be the same:\n" '~x '~@xs))))

    (defn display-todos! []
      (when (seq (:todos @STATE))
        (println "\nRemaining to be done:\n")
        (mapv clojure.pprint/pprint (interpose "\n" (:todos @STATE)))
        (println)))

    (defn error [& xs]
      (throw (Exception. (apply str (interpose "\n" xs))))))

(comment :the-clojure-way

         (is (first (list 1 2 3))
             1)

         (is (rest (list 1 2 3))
             (list 2 3))

         (is (cons 1 ())
             (list 1))

         (is (cons 1 nil)
             (list 1))

         (is (cons 1 (list 2 3))
             (list 1 2 3)))

(do :scheme-way

    (defn atom? [x]
      (or (number? x)
          (symbol? x)))

    (defn car [p]
      (or (:car p)
          (error "not a pair " p)))

    (defn cdr [p]
      (or (:cdr p)
          (error "not a pair " p)))

    (defn cons [car cdr]
      {:car car :cdr cdr})

    (defn cons? [x]
      (and (map? x)
           (contains? x :car)
           (contains? x :cdr)))

    (defn list [& xs]
      (reduce (fn [l x] (cons x l))
              nil
              (reverse xs)))

    ;; to be or not to be



    (is true)

    (isnt false)



    (isnt nil)




    (is 1)

    (is 1 1)

    (isnt 1 2)

    (is (number? 1))




    (is 'a)

    (is 'foo 'foo)

    (isnt 'foo 'bar)

    (is (symbol? 'baz))

    (isnt (number? 'qux))

    (isnt (symbol? 42))



    (is (true? true))

    (is (false? false))

    (is (nil? nil))

    (isnt (false? true))

    (isnt (false? nil))


    (is (not false))

    (is (not nil))

    (isnt (not true))

    (isnt (not 42))


    (is (or true false))

    (is (or false true))

    (is (or true nil))

    (is (or nil 42) 42)

    (is (or 'foo 23) 'foo)

    (is (and true true))

    (is (and true 1 'bar) 'bar)

    (isnt (or false false))

    (isnt (and true false))

    (isnt (and 1 'foo false))




    (is (cons 3 7))

    (is (cons 'age 13))

    (is (cons 'hello 'world))

    (is (cons? (cons 11 0)))




    (is (atom? 42))

    (is (atom? 'sym))

    (isnt (atom? (cons 'car 'cdr)))




    (is (car (cons 1 2))
        1)

    (is (cdr (cons 'a 'b))
        'b)

    (is (list 1)
        (cons 1 nil))

    (is (list 1 2)
        (cons 1 (list 2))
        (cons 1 (cons 2 nil)))




    (is (fn [x] x))

    (is ((fn [x] x) 1)
        1)

    (is ((fn [x] x) 'a)
        'a)

    (is (fn [x] (cons x x)))

    (is ((fn [x] (cons x x)) 1)
        (cons 1 1))

    (is (fn [x y] (cons x y)))

    (is ((fn [x y] (cons x y)) 1 'a)
        (cons 1 'a))




    (isnt x)

    (be x 1)

    (is x)

    (is x 1)

    (is x x)

    (be y (cons x x))

    (is y (cons 1 1))

    (be f (fn [x] x))

    (is f)

    (is (f 1) 1)

    (be g (fn [x y] (cons x y)))

    (is (g 1 2)
        (cons 1 2))

    (is (g 1 (g 2 nil))
        (cons 1 (g 2 nil))
        (cons 1 (cons 2 nil))
        (list 1 2))

    (be h cons)

    (is (h 1 2)
        (cons 1 2))

    (be n number?)

    (is (n 42))



    (is (if true :ok))

    (is (if (number? 1) :ok)
        :ok)

    (isnt (if (number? 'a) :ok))

    (is (if-not false :ok)
        :ok)

    (isnt (if-not true :ok))

    (is (cond (number? 'a) :a
              (number? 1) :b)
        :b)

    (is (cond (number? 1) :a
              (symbol? 'a) :b
              (cons? 2) :c)
        :a)

    (be f (fn [x]
            (cond (number? x) [:number x]
                  (symbol? x) [:symbol x]
                  (cons? x) [:cons x]
                  :else [:unknown x])))

    (is (f 1)
        [:number 1])

    (is (f 'foo)
        [:symbol 'foo])

    (is (f (cons 1 2))
        [:cons (cons 1 2)])

    (is (f "I'm a string")
        [:unknown "I'm a string"])



    (be countdown
        (fn [n]
          (if (zero? n)
            :done
            (countdown (dec n)))))

    (is (countdown 5)
        (countdown 10)
        :done)


    (be take
        (fn [xs n]
          (cond (nil? xs) xs
                (zero? n) nil
                :else (cons (car xs)
                            (take (cdr xs) (dec n))))))

    (is (take (list 1 2 3 4) 2)
        (list 1 2))

    )

(display-todos!)