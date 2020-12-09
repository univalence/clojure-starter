(ns clojure-starter.recursion)

(comment "
For this exercise the only allowed functions are:
- `first`: takes a list in params returns the first element of a list
- `rest`: returns a list without it's first element, returns an empty list the list tail is empty.
- `next`: same as `rest but returns nil if the list's tail is empty
- `cons`: Adds an element at the head of a list | (cons :head (list :tail-1 :tail-2)) -> (:head :tail-1 :tail-2)
- `seq`: usage here -> returns nil if a list is empty | (seq ()) -> nil
- `empty?`: returns true if a list is empty
- `number?`: returns true if argument is a number
- `if`: can be used in a function |
              (defn fun [elt-that-should-equal-1]
                (if (= 1 elt-that-should-equal-1)
                  true
                  false ))

- `cond`: acts like an 'else-if' |
(defn fun [x]
  (cond
    (= 1 x) :first-case
    (= 2 x) :second-case
    :else :third-case))

- `when` acts like retrun or nil  | (defn fun [x](when (= x 1) :x-is-equal-to-one))
                                  | (fun 1) -> :x-is-equal-to-one
                                  | (fun 2) -> nil

- `fn` defines a lambda
- `defn` defines a function ")

;; test function for cond
(defn test-cond [x]
  (cond (number? x) :number
        (list? x) :list
        :else :default))
(test-cond "chose")

;; numbers? should return true if x is a list of numbers
(defn numbers? [x]
  (if (seq x)
    (when (number? (first x)) (numbers? (rest x)))
    true))

;; numbers? tests
(numbers? (list 0 1 4))
(numbers? (list "truc" 1 4))
(numbers? (list (list 0 9) 1 4))

;; member? should return true if something belongs to a list
(defn member? [list thing]
  (when (seq list)
    (or (= thing (first list))
        (member? (rest list) thing))))

;; member? tests
(member? (list 1 2 3) 2)
(member? (list 1 2 3) 6)

;; pimp-my-concat should concat two lists
(defn pimp-my-concat [list-1 list-2]
  (cond
    (empty? list-1) list-2
    (empty? list-2) list-1
    :else (cons (first list-1) (pimp-my-concat (rest list-1) list-2))))

;; pimp-my-concat tests
(pimp-my-concat (list 1 2 3) (list 4 5 6))

;; pimp-my-concat: what's happening internally
(cons 1 (pimp-my-concat (list 2 3) (list 4 5 6)))
(cons 1 (cons 2 (pimp-my-concat (list 3) (list 4 5 6))))
(cons 1 (cons 2 (cons 3 (pimp-my-concat () (list 4 5 6)))))
(cons 1 (cons 2 (cons 3 (list 4 5 6))))

;; rember? should remove the first occurence of something from a list
(defn rember? [list thing]
  (cond
    (empty? list) list
    (= (first list) thing) (rest list)
    :else (cons (first list) (rember? (rest list) thing))))

;; rember? tests
(rember? (list 1 2 3) 2)
(rember? (list 1 2 3) 4)

;; rember-all? should remove every occurence of something in a list
(defn rember-all? [list chose]
  (cond
    (empty? list) list
    (= (first list) chose) (rember-all? (rest list) chose)
    :else (cons (first list) (rember-all? (rest list) chose))))

;; rember-all? tests
(rember-all? (list 1 2 3 2) 2)
(rember-all? (list 1 2 3) 4)

;; introduction to variadic functions
(defn nom-a-la-noix
  ([arg arg2] :branch-1)
  ([arg arg2 arg3] :branch-2)
  ([arg arg2 arg3 & args] (list :branch-3 arg arg2 arg3 args))) ;; variadique

;; variadic functions test
(nom-a-la-noix 1 2)
(nom-a-la-noix 1 2 3)
(nom-a-la-noix 1 2 4 5 6 7)

;; length should return the length of an array | O(n)
(defn length [list]
  (if (empty? list) 0 (inc (length (rest list)))))

;; length tests
(length (list 1 2 3))

;; length mind helper
(inc (length (list 2 3)))
(inc (inc (length (list 3))))
(inc (inc (inc 0)))

;; map-custom should apply a function to each element of a list
(defn map-custom [fun xs]
  (if (empty? xs)
    xs
    (cons (fun (first xs)) (map-custom fun (rest xs)))))

;; map-custom tests
(map-custom inc (list 1 2 3))

;; filter-custom should return a list of values that validate a predicate
(defn filter-custom [predicate xs]
  (cond
    (empty? xs) xs
    (predicate (first xs)) (cons (first xs) (filter-custom predicate (rest xs)))
    :default (filter-custom predicate (rest xs))))

;; filter-custom test
(filter-custom odd? (range 10))                             ;; should return only odd numbers

;; remove-custom is basically the opposite of filter
(defn remove-custom [predicate xs]
  (cond
    (empty? xs) xs
    (predicate (first xs)) (remove-custom predicate (rest xs))
    :default-get-rid-of-odds (cons (first xs) (remove-custom predicate (rest xs)))))

;; remove-custom test
(remove-custom odd? (range 10))                             ;; remove odds

;; reduce-custom
(defn reduce-custom [fun acc xs]
  (if (empty? xs)
    acc
    (reduce-custom fun (fun acc (first xs)) (rest xs))))

;; reduce-custom test
(reduce-custom + 0 (list 1 2 3))
(reduce-custom + 0 ())

;; insert-at (index) hint plusieurs arités algo qui maintiennent acc arrité supp pour acc

;; insert-at-custom should insert an element to a list at a specific index
;; first solution
(defn insert-at-custom [elt-to-insert index current-index xs]
  (cond
    (empty? xs) xs
    (= index current-index) (cons elt-to-insert xs)
    :else (cons (first xs) (insert-at-custom elt-to-insert index (inc current-index) (rest xs)))))

;; tests
(insert-at-custom "inserted value" 2 0 (list 0 1 2 3 4 5))

;; help
(cons 0 (cons 1 (cons "chose" (list 3 4))))                 ;; what we want to evaluate

;; another implementation
(defn better-insert-at [elt-to-insert index xs]
  (cond
    (empty? xs) xs
    (= index 0) (cons elt-to-insert xs)
    :else (cons (first xs) (better-insert-at elt-to-insert (dec index) (rest xs)))))

;; tests
(better-insert-at "inserted value" 2 (list 0 1 2 3 4 5))

;; index-of returns the index of an element
(defn index-of-custom [elt-to-index current-index xs]
  (cond
    (empty? xs) nil
    (= elt-to-index (first xs)) current-index
    :else (index-of-custom elt-to-index (inc current-index) (rest xs))))

; tests
(index-of-custom "chose" 0 (list 0 1 "chose" 3 4 5))

;; replace replaces an element by another element in a list
(defn replace-custom [elt-to-replace replacement-elt xs]
  (cond
    (empty? xs) xs
    (= elt-to-replace (first xs)) (cons replacement-elt (rest xs))
    :else (cons (first xs) (replace-custom elt-to-replace replacement-elt (rest xs)))))

;;tests
(replace-custom 2 :chose (list 1 2 3 4))
(replace-custom 2 "chose" (list 1 2 3 4))

;; help replace 1 by "replacement"
(cons 0 (cons "replacement" (list 2 3)))