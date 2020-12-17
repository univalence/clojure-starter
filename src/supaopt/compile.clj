(ns supaopt.compile
  (:require [badigeon.javac :as c]))

(c/javac "javas"
         {:compile-path "output"})
