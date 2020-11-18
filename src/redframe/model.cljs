(ns redframe.model
  (:require
   [re-frame.core :as rf]
   [redframe.events :as events]))


(rf/reg-event-db
 ::init
 (fn [db [_ ns-key initial]]
   (assoc db ns-key initial)))

(defn- get-db-ns-key [params]
  (let [ns (:ns params)]
    (cond
      (keyword ns) ns
      (string? ns) (keyword ns)
      ;; TODO Throw Error
      :else "Throw Error")))

(defn- reg-ns-sub [ns-key]
  (rf/reg-sub
   ns-key
   (fn [db] (get db ns-key))))

(defn- init-ns-db [ns-key data]
  (rf/dispatch-sync [::init ns-key data]))

(defn- reg-subs [ns-key subs]
  (doseq [[k f] subs]
    (rf/reg-sub k (fn [db] (f (get db ns-key))))))

(defn model [params]
  (let [ns-key (get-db-ns-key params)
        init-data (:init params)
        subs (:subs params)
        db-events (:db-events params)
        fx-events (:fx-events params)]
    (init-ns-db ns-key init-data)
    (reg-ns-sub ns-key)
    (reg-subs ns-key subs)
    (events/reg-db-events ns-key db-events)
    (events/reg-fx-events ns-key fx-events)))
