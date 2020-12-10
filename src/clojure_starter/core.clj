(ns clojure_starter.core)

(do :basics

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

    (mean (range 10)))

(do :macros

    (do :unless

        ;; ce qu'on veut écrire

        '(unless (test qqchose)
                 (computate if test is false)
                 (computate if test is true))

        ;; ce qui sera émit

        '(if (not (test qqchose))
           (computate if test is false)
           (computate if test is true))

        ;; ou

        '(if (test qqchose)
           (computate if test is true)
           (computate if test is false))

        ;; implementation

        (defmacro unless
          [test false-branch true-branch]
          (list 'if
                (list `not test)
                false-branch
                true-branch))

        (macroexpand '(unless (test qqchose)
                              (computate if test is false)
                              (computate if test is true)))

        (unless (pos? -1)
                :yop
                :nop))

    (do :postfix

        (defn postfix->prefix [x]

          (if (list? x)

            (let [last-x (last x)
                  args (butlast x)]
              (cons last-x (map postfix->prefix args)))

            x))

        (postfix->prefix '(2 3 (9 (9 8 7 -) *) +))

        (defmacro postfix [x]
          (postfix->prefix x))

        (macroexpand-1
          '(postfix
             (2 3 (9 (9 8 7 -) *) +)))))

(do :map-values

    (defn map-values [f m]

      (let [entries
            (map (fn [e]
                   (vector (key e) (f (val e))))
                 (seq m))]

        (into {} entries)))

    (map-values inc {:a 1 :b 2})

    (merge {:a 1 :b 2}
           {:b 3 :c 5}
           {:c 3 :d 5}))

(do :fifo

    (def fifo ())

    (defn tak [fifo]
      [(first fifo) (rest fifo)])

    (defn put [fifo x]
      (cons x fifo))

    (defn putend [fifo x]
      (concat fifo (list x)))

    (put fifo 1)

    (def fifo2
      (-> fifo
          (put 1)
          (put 3)
          (put 8)))

    (tak fifo2))

(do :atoms

    (def my-num (atom 1))

    (deref my-num)

    (reset! my-num 10)

    (swap! my-num inc))

(do :proto

    (defprotocol IMap
      (fmap [this f]))

    (extend-protocol IMap
      clojure.lang.PersistentList
      (fmap [this f] (map f this))
      clojure.lang.PersistentVector
      (fmap [this f] (mapv f this))
      clojure.lang.PersistentArrayMap
      (fmap [this f] (map-values f this)))

    (fmap (list 1 2 3) inc)
    ;=> (list 2 3 4)

    (fmap [1 2 3] dec)
    ;; [0 1 2]

    (fmap {:a 1 :b 2} inc)
    ;;=> {:a 2 :b 3}

    (defn map-deep [this f]
      (if (satisfies? IMap this)
        (fmap this (fn [x] (map-deep x f)))
        (f this)))

    (map-deep {:a {:b {:c 1}}}
              inc)

    (map-deep [1 2 [2 3 {:a 2 :b [{:c 52} (list 1 2 3)]}]]
              inc)

    (defrecord Point [x y]
      IMap
      (fmap [this f] (Point. (f x) (f y))))

    (fmap (new Point 1 2) inc)

    (map-deep {:a {:b (new Point 1 2)}}
              inc)
    )

(do :threading-macros-examples

    (-> {}
        (assoc :a 1)
        (assoc :b 2)
        (merge {:b 3 :c 4}))

    ;; expands to

    (merge
      (assoc
        (assoc {} :a 1)
        :b 2)
      {:b 3 :c 4})


    (->> (range 0 5)
         (map inc)
         (map inc))

    ;; expand to

    (map inc (map inc (range 0 5)))

    )

;; ------------------------------------------------------------------------------------------------------------------------------------------------------------


