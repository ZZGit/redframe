(ns redframe.core
  (:require
   [redframe.model :as m]
   [cljs.core.async :as async])
  (:require-macros redframe.core))

(def reg-model m/reg-model)

(def <! async/<!)
