(ns clojure-starter.lazy-cons)


(do :helpers

    (defn resume
      "takes a thunk and calls it"
      [f] (f))

    (defn test-thunk
      "this thunk will be useful to test our lazy-cons(es)"
      []
      (println "waiting...")
      (Thread/sleep 1000)
      (println "done")
      ;; we return nil
      nil))

(do :main-operations

    (defrecord LazyCons [head tail])

    (defn head
      "return the head of a lazy-cons"
      [p] (:head p))

    (defn tail
      "return the tail of a lazy-cons
       a valid LazyCons tail can be either a thunk or nil"
      [p]
      (when-let [r (:tail p)]
        (resume r)))

    (defn build-lazy-cons [head tail-fn]
      (new LazyCons head tail-fn))

    (defn unlazify
      "turn a lazy-cons into a non-lazy list"
      [l]
      (when l
        (cons (head l)
              (unlazify (tail l)))))

    (do :tries

        (let [l (build-lazy-cons 1 (fn [] (build-lazy-cons 2 nil)))]
          [(head l)
           (tail l)])

        (unlazify
          (build-lazy-cons 1 (fn [] (build-lazy-cons 2 (fn [] (build-lazy-cons 3 nil))))))))



(do :syntax

    ;; ce qu'on aimerait noter
    '(lazy-cons 1 (arbitrary delayed computation))

    ;; ce en quoi cela sera transformé
    '(build-lazy-cons 1 (fn [] (arbitrary delayed computation)))

    ;; implementation 1

    (defmacro lazy-cons [head tail]
      (list `build-lazy-cons
            head
            (list `fn [] tail)))

    (comment :tries1

        (macroexpand '(lazy-cons 1 (arbitrary delayed computation)))

        (let [l (lazy-cons 1 (test-thunk))]
          (unlazify l)))

    ;; it would be nice to be able to write this also
    ;; giving as many heads as needed followed by a tail

    '(lazy-cons 1 2 (test-thunk))
    '(lazy-cons 1 2 3 4 (test-thunk))
    '(lazy-cons 1 2 3 4 5 6 (test-thunk))

    ;; implementation 2

    (defmacro lazy-cons

      ([head tail]
       (list `build-lazy-cons
             head
             (list `fn [] tail)))

      ([head1 head2 & xs]
       (list `lazy-cons
             head1
             (apply list `lazy-cons head2 xs))))

    (comment :tries2

        ;; expansion steps
        '(lazy-cons 1 2 3 (test-thunk))
        '(lazy-cons 1 (lazy-cons 2 3 4 (test-thunk)))
        '(lazy-cons 1 (lazy-cons 2 (lazy-cons 3 (test-thunk))))
        ;; after this each layer has to be expanded again by the arity 2 of the macro
        '(build-lazy-cons
           1 (fn []
               (build-lazy-cons
                 2 (fn []
                     (build-lazy-cons
                       3 (fn [] (test-thunk)))))))

        ;; we can check the expansion like this

        (clojure.pprint/pprint
          (clojure.walk/macroexpand-all
            '(lazy-cons 1 2 3 (test-thunk))))

        (let [l (lazy-cons 1 2 3 (test-thunk))]
          ;; returns (1 2 3) after waiting 1 second
          (unlazify l))))

(do :lazy-range

    ;; pour nos essais il serait bien pratique d'avoir une facilité du même type que clojure.core/range
    ;; voyons comment cela pourrait être implementé au dessus de notre lazy-cons

    (defn lazy-range
      ([] (lazy-range 0))
      ([start]
       (build-lazy-cons start (fn [] (lazy-range (inc start)))))
      ([start end]
       (when (> end start)
         (build-lazy-cons start (fn [] (lazy-range (inc start) end))))))

    (unlazify (lazy-range 1 6)))

(do :lazy-take

    ;; rafraichissons nous la mémoire en reimplémentant take pour les listes non lazy

    (do :non-lazy

        (defn take-list [n l]
          (when-not (or (empty? l) (zero? n))
            (cons (first l) (take-list (dec n) (rest l)))))

        (take-list 20 (range 10)))

    ;; en partant de ce modèle nous devrions, moyennement des modifications mineures,
    ;; parvenir à la même chose sur nos lazy-cons(es)

    (defn lazy-take [n l]
      (when-not (or (empty? l) (zero? n))
        (cons (head l) (lazy-take (dec n) (tail l)))))

    (lazy-take 2 (lazy-range 10))
    (lazy-take 10 (lazy-range)))

(do :lazy-map

    (do :non-lazy

        ;; implementer map pour les listes non lazy

        (defn mymap [f l]
          (when (seq l)
            (cons (f (first l)) (mymap f (rest l)))))

        (mymap inc (range 10)))

    ;; implementation 1 (sans la macro lazy-cons)

    (defn lazy-map1 [f l]
      (when-not (empty? l)
        (build-lazy-cons (f (head l))
                         (fn [] (lazy-map1 f (tail l))))))

    (lazy-take 5 (lazy-map1 inc (lazy-range)))

    (defn lazy-map [f l]
      (when-not (empty? l)
        (lazy-cons (f (head l))
                   (lazy-map f (tail l)))))

    (lazy-take 5 (lazy-map inc (lazy-range)))

    (->> (lazy-range)
         (lazy-map inc)
         (lazy-map inc)
         (lazy-map inc)
         (lazy-map inc)
         (lazy-take 5)))
