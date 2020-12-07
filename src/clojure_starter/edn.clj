(ns clojure-starter.edn)

;; un copié/collé partiel de
;; https://learnxinyminutes.com/docs/edn/#:~:text=Extensible%20Data%20Notation%20(EDN)%20is,syntax%2C%20especially%20from%20untrusted%20sources.&text=The%20main%20benefit%20of%20EDN,is%20that%20it%20is%20extensible.

(comment

  ; Comments start with a semicolon.
  ; Anything after the semicolon is ignored.

  ;;;;;;;;;;;;;;;;;;;
  ;;; Basic Types ;;;
  ;;;;;;;;;;;;;;;;;;;

  nil ; also known in other languages as null

  ; Booleans
  true
  false

  ; numbers
  42
  3.14159
  1/2
  1e-10

  ; Strings are enclosed in double quotes
  "hungarian breakfast"
  "farmer's cheesy omelette"

  ; Characters are preceded by backslashes
  \g \r \a \c \e

  ; Keywords start with a colon. They behave like enums. Kind of
  ; like symbols in Ruby.
  :eggs
  :cheese
  :olives

  ; Symbols are used to represent identifiers.
  ; You can namespace symbols by using /. Whatever precedes / is
  ; the namespace of the symbol.

  spoon
  kitchen/spoon ; not the same as spoon
  kitchen/fork
  github/fork ; you can't eat with this


  ; Lists are sequences of values
  (:bun :beef-patty 9 "yum!")

  ; Vectors allow random access
  [:gelato 1 2 -2]

  ; Maps are associative data structures that associate the key with its value
  {:eggs 2
   :lemon-juice 3.5
   :butter 1}

  ; You're not restricted to using keywords as keys
  {[1 2 3 4] "tell the people what she wore",
   [5 6 7 8] "the more you see the more you hate"}

  ; You may use commas for readability. They are treated as whitespace.

  ; Sets are collections that contain unique elements.
  #{:a :b 88 "huat"})
