(ns clojure-starter.hello-asm

  (:import [clojure.asm Opcodes Type ClassWriter]
           [clojure.asm.commons Method GeneratorAdapter]))

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

(comment
  user=> (make-class "foo.Foo")
  foo.Foo
  user=> (def o (construct-proxy *1))
  #'user/o
  user=> (.answerToTheUltimateQuestion o)
  42
  user=>
  )