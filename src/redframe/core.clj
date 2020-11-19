(ns redframe.core)

(defmacro defgo [f & rst]
  (if (symbol? f)
    (let [args (first rst)
          body (rest rst)]
      `(defn ~f ~args
         (cljs.core.async/go
           (do ~@body))))
    `(fn ~f
       (cljs.core.async/go
         (do ~@rst)))))
