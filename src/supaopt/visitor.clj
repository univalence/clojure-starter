(ns supaopt.visitor
  (:import (org.objectweb.asm ClassReader ClassVisitor FieldVisitor MethodVisitor Opcodes)
           (org.objectweb.asm.util TraceClassVisitor)
           (java.io PrintWriter))
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pp]))



(defn file->bytes [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))

(def input (file->bytes (io/file "out/AsmHelloWorld.class")))

(def class-reader (new ClassReader input))

(def print-writer (new PrintWriter *out*))

(def trace-class-visitor (new TraceClassVisitor print-writer))

(defn method-visitor [mv]
  (proxy [MethodVisitor] [Opcodes/ASM9 mv]))

(defn field-visitor [mv]
  (proxy [FieldVisitor] [Opcodes/ASM9 mv]))

(def class-visitor
  (proxy [ClassVisitor] [Opcodes/ASM9]
    (visitField [access name desc signature value]
      (spit "field.edn" [access name desc signature value])
      (field-visitor (proxy-super visitField access name desc signature value)))
    (visitMethod [access name desc signature exceptions]
      (spit "method.edn" [access name desc signature exceptions])
      (method-visitor (proxy-super visitMethod access name desc signature exceptions)))))

(defn visit []
  (.accept class-reader class-visitor ClassReader/SKIP_DEBUG))

(comment :proxy-abstract

         (.findResource (proxy [ClassLoader] []
                          (findResource [name]
                            (java.net.URL. "http://somewhere")))
                        "doot")

         (-> (proxy [ClassLoader] []
               (findResource [name]
                 (java.net.URL. "http://somewhere")))
             (.findResource "doot")))

(comment :runnable-proxy (def prx (proxy [java.lang.Runnable] []
                                    (run
                                      ([] (println "We can use this inside here" this) 1))))

         (.run prx))

(comment :old-input (def path (new java.nio.file.Path "out/AsmHelloWorld.class"))
         (def input (java.nio.file.Files/newInputStream path []))) ;;incorrect arrity || var input = Files.newInputStream (path)

(comment :rubbish

         (reify
           ClassVisitor
           (visitField [this acess name desc signature value]
             nil #_(FieldVisitor.))
           (visitMethod [this access name desc signature exceptions]
             nil #_(MethodVisitor.)))

         (defn add-mousepressed-listener
           [component f & args]
           (let [listener (proxy [ClassVisitor] []
                            (visitField [event]
                              (apply f event args)))]
             (.addMouseListener component listener)
             listener))

         (let [p (proxy [ClassVisitor] []
                   (visitField [acess name desc signature value]
                     (FieldVisitor. Opcodes/ASM9))
                   (visitMethod [access name desc signature exceptions]
                     (MethodVisitor. Opcodes/ASM9)))]
           )

         (new (proxy [MethodVisitor] [])
              Opcodes/ASM9)

         (println Opcodes/ASM9)

         (-> (proxy [ClassLoader] []
               (findResource [name]
                 (java.net.URL. "http://somewhere")))
             (.findResource "doot"))

         (defn f [& i]
           (proxy [clojure.lang.ISeq] []
             (seq [] (sort i))
             (toString [] (apply str (interpose "-" i)))))

         (seq (f 4 3 2 1))

         (def MyMethodVisitor (get-proxy-class MethodVisitor))

         (defn metviz
           ([] (construct-proxy MyMethodVisitor Opcodes/ASM9))
           ([id] (construct-proxy MyMethodVisitor id))
           ([id mv] (construct-proxy MyMethodVisitor id mv)))

         (metviz))

(comment :snippet

         (import '[clojure.asm Opcodes Type ClassWriter]
                 '[clojure.asm.commons Method GeneratorAdapter])

         (defn make-class [name]
           (let [cw (ClassWriter. ClassWriter/COMPUTE_FRAMES)
                 init (Method/getMethod "void <init>()")
                 meth (Method/getMethod "int answerToTheUltimateQuestion()")]
             (.visit cw Opcodes/V1_6 Opcodes/ACC_PUBLIC (.replace name \. \/) nil "java/lang/Object" nil)
             (doto (GeneratorAdapter. Opcodes/ACC_PUBLIC init nil nil cw)
               (.visitCode)
               (.loadThis)
               (.invokeConstructor (Type/getType Object) init)
               (.returnValue)
               (.endMethod))
             (doto (GeneratorAdapter. Opcodes/ACC_PUBLIC meth nil nil cw)
               (.visitCode)
               (.push 42)
               (.returnValue)
               (.endMethod))
             (.visitEnd cw)
             (let [b (.toByteArray cw)
                   cl (clojure.lang.DynamicClassLoader.)]
               (.defineClass cl name b nil))))

         user=> (make-class "foo.Foo")
         foo.Foo
         user=> (construct-proxy *1)
         #'user/o
         user=> (.answerToTheUltimateQuestion o)
         42
         user=>
         )

(comment

  (def ClassVisitorProxy (get-proxy-class ClassVisitor))

  (defn class-viz
    ([] (construct-proxy ClassVisitorProxy Opcodes/ASM9))
    ([id] (construct-proxy ClassVisitorProxy id))
    ([id mv] (construct-proxy ClassVisitorProxy id mv)))

  #_(class-viz)
  )
