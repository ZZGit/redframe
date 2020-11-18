(ns redframe.core
  (:require
   [clojure.core.async :refer [chan go >!]]))

(defmacro gofn [f & rst]
  (if (symbol? f)
    (let [args (first rst)
          body (rest rst)]
      `(defn ~f ~args
         (go
           (do ~@body))))
    `(fn ~f
       (go
         (do ~@rst)))))
